package com.asyncapi.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.burningwave.core.classes.AnnotationSourceGenerator;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.GenericSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.asyncapi.parser.java.model.AsyncAPI;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.UUID;


//todo refactor
public class KafkaDefaultSourceConfigGenerator extends KafkaBaseGenerator {

    public KafkaDefaultSourceConfigGenerator(AsyncAPI model, File sourceDir, File propertiesFolder,
            String basePackage, String channel, String defaultNaming) {
        super(model, sourceDir, propertiesFolder, basePackage, channel, defaultNaming);
    }

    public void generate() {
        generateProps();
        if (consumerExist || producerExist) {
            generateModel();
            generatePropertyClass();
            generateConfigClass();
        }
        if (producerExist) generateProducerClass();
        if (consumerExist) generateConsumerClass();
    }

    private void generateProducerClass() {
        var className = getClassNamePrefix(topic) + PRODUCER_SUFFIX;
        var packageName = basePackage + DOT + PRODUCER_PACKAGE;
        var generatedUnit = UnitSourceGenerator.create(packageName);
        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        generatedUnit.addImport(KafkaTemplate.class, LoggerFactory.class, UUID.class, ProducerRecord.class);
        if (headerExist) {
            generatedUnit.addImport(JsonProcessingException.class, Map.class, SerializationException.class, RecordHeaders.class);
        }
        generatedUnit.addImport(producerPayloadMessage, producerMessage, propertyClassName);
        clazz
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Component.class))
                .addAnnotation(conditionalOnProperty(true))
                .addField(loggerField(className))
                .addField(VariableSourceGenerator.create(
                        TypeDeclarationSourceGenerator
                                .create(KafkaTemplate.class)
                                .addGeneric(GenericSourceGenerator.create(STRING),
                                        GenericSourceGenerator.create(getShortName(producerPayloadMessage))
                                ),
                        KAFKA_TEMPLATE_FIELD
                ).addModifier(Modifier.PRIVATE).addModifier(Modifier.FINAL))
                .addField(kafkaPropertyField());

        if (headerExist) clazz.addField(headerMapperField()).addConstructor(FunctionSourceGenerator
                .create()
                .addParameter(VariableSourceGenerator.create(
                        TypeDeclarationSourceGenerator
                                .create(KafkaTemplate.class)
                                .addGeneric(GenericSourceGenerator.create(STRING),
                                        GenericSourceGenerator.create(getShortName(producerPayloadMessage))
                                ),
                        KAFKA_TEMPLATE_FIELD
                ))
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(getShortName(propertyClassName)),
                        KAFKA_PROPERTY_FIELD
                ))
                .addParameter(VariableSourceGenerator
                        .create(ObjectMapper.class, HEADER_MAPPER_BEAN)
                        .addAnnotation(AnnotationSourceGenerator
                                .create(Qualifier.class)
                                .addParameter("value", VariableSourceGenerator.create(String.format("\"%s\"", HEADER_MAPPER_BEAN)))))
                .addBodyCodeLine("this.kafkaTemplate = kafkaTemplate;")
                .addBodyCodeLine("this.kafkaProperties = kafkaProperties;")
                .addBodyCodeLine("this.headerMapper = headerMapper;")
                .addModifier(Modifier.PUBLIC));
        else clazz.addAnnotation(AnnotationSourceGenerator.create(RequiredArgsConstructor.class));

        var method = FunctionSourceGenerator
                .create(model.getChannels().get(channel).getSubscribe().getOperationId())
                .setReturnType("void")
                .addModifier(Modifier.PUBLIC)
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(producerMessage, getShortName(producerMessage)),
                        StringUtils.uncapitalize(getShortName(producerMessage))
                ))
                .addBodyCode(createProducerBody(StringUtils.uncapitalize(getShortName(producerMessage))));

        clazz.addMethod(method);

        if (headerExist) {
            var mapMethod = FunctionSourceGenerator.create("objectToByte")
                    .setReturnType("byte[]")
                    .addModifier(Modifier.PRIVATE)
                    .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create("Object"), "object"))
                    .addBodyCode(createProducerMapMethodBody());

            clazz.addMethod(mapMethod);
        }
        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private String createProducerMapMethodBody() {
        return "\ttry {\n" + "\t\treturn headerMapper.writeValueAsBytes(object);\n" + "\t} catch (JsonProcessingException e) {\n" +
               "\t\tthrow new SerializationException(\"Ошибка сериализации заголовков\", e);\n" + "\t}";
    }

    private String createProducerBody(String producerMessage) {
        var key = "\tvar key = UUID.randomUUID().toString();\n";
        var body = headerExist ? String.format(
                "\tMap<String, Object> map = headerMapper.convertValue(%1$s.getHeaders(), Map.class);\n" + "\tvar headers = new RecordHeaders();\n" +
                "\tmap.forEach((key, value) -> headers.add(key, objectToByte(value)));\n" + key +
                "\tvar record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, %1$s.getPayload(), headers);\n",
                producerMessage
        ) : key + String.format("\tvar record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, %1$s.getPayload(), null);\n",producerMessage);
        return body + "\tkafkaTemplate.send(record)\n" +
               "\t\t\t.addCallback(result -> LOGGER.info(\"Сообщение {} отправлено в топик {} и получение подтверждено брокером\",\n" +
               "\t\t\t\t\t\t\trecord.key(),\n" + "\t\t\t\t\t\t\t\trecord.topic()\n" + "\t\t\t\t\t),\n" +
               "\t\t\t\t\te -> LOGGER.error(\"Ошибка отправки {} в {}\", record.key(), kafkaProperties.getTopic(), e)\n" + "\t\t\t);";
    }

    private void generateConsumerClass() {

        var className = getClassNamePrefix(topic) + CONSUMER_SUFFIX;
        var topicProperty = propertiesPrefix + DOT + TOPIC_PACKAGE;
        var groupIdProperty = propertiesPrefix + DOT + CONSUMER_PACKAGE + DOT + ConsumerConfig.GROUP_ID_CONFIG;

        var generatedUnit = UnitSourceGenerator.create(basePackage + DOT + CONSUMER_PACKAGE);
        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        generatedUnit.addImport(LoggerFactory.class);
        generatedUnit.addImport(consumerPayloadMessage);

        clazz.addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(RequiredArgsConstructor.class))
                .addAnnotation(AnnotationSourceGenerator.create(Component.class))
                .addAnnotation(conditionalOnProperty(true))
                .addField(VariableSourceGenerator.create(Logger.class, "LOGGER")
                        .addModifier(Modifier.PUBLIC)
                        .addModifier(Modifier.STATIC)
                        .addModifier(Modifier.FINAL)
                        .setValue(String.format("LoggerFactory.getLogger(%s.class)", className)));

        var listenerAnnotation = AnnotationSourceGenerator.create(KafkaListener.class)
                .addParameter("topics", VariableSourceGenerator.create(String.format(VALUE_INJECT_PATTERN, topicProperty)));
        listenerAnnotation.addParameter(annotationParameter("groupId", groupIdProperty));
        var method = FunctionSourceGenerator.create(model.getChannels()
                        .get(channel)
                        .getPublish()
                        .getOperationId())
                .setReturnType(VOID)
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(listenerAnnotation)
                .addParameter(VariableSourceGenerator.create(
                        TypeDeclarationSourceGenerator.create(ConsumerRecord.class)
                                .addGeneric(GenericSourceGenerator.create(STRING),
                                        GenericSourceGenerator.create(getShortName(consumerPayloadMessage))
                                ),
                        "record"
                ))
                .addBodyCode("LOGGER.info(\"Key: {}, Payload: {}, Timestamp: {}, Partition: {}\", " +
                             "record.key(), record.value(), record.timestamp(), record.partition());");
        clazz.addMethod(method);
        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private void generateConfigClass() {
        var className = getClassNamePrefix(topic) + CONFIG_SUFFIX;
        var generatedUnit = UnitSourceGenerator.create(basePackage + DOT + CONFIG_PACKAGE);
        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        generatedUnit.addImport(KafkaTemplate.class, DefaultKafkaConsumerFactory.class, DefaultKafkaProducerFactory.class);
        if (headerExist) {
            generatedUnit.addImport(DeserializationFeature.class,
                    MapperFeature.class,
                    ObjectMapper.class,
                    SerializationFeature.class,
                    JsonMapper.class,
                    JavaTimeModule.class
            );
        }
        generatedUnit.addImport(consumerPayloadMessage, producerPayloadMessage);
        clazz
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(RequiredArgsConstructor.class))
                .addAnnotation(AnnotationSourceGenerator.create(Configuration.class))
                .addAnnotation(conditionalOnProperty(true));
        clazz.addField(defaultKafkaPropertyField()).addField(kafkaPropertyField());

        if (consumerExist)
            clazz.addMethod(consumerFactory().addModifier(Modifier.PUBLIC).addAnnotation(AnnotationSourceGenerator.create(Bean.class)));
        if (producerExist) clazz
                .addMethod(producerFactory().addModifier(Modifier.PUBLIC).addAnnotation(AnnotationSourceGenerator.create(Bean.class)))
                .addMethod(kafkaTemplateMethod());
        if (headerExist) clazz.addMethod(headerMapper());
        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    protected FunctionSourceGenerator consumerFactory() {
        var kafkaConsumerFactory= getMethodNamePrefix(topic) + KAFKA_SUFFIX + CONSUMER_SUFFIX + FACTORY_SUFFIX;
        return FunctionSourceGenerator
                .create(kafkaConsumerFactory)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(ConsumerFactory.class)
                        .addGeneric(GenericSourceGenerator.create(STRING), GenericSourceGenerator.create(getShortName(consumerPayloadMessage))))
                .setBody(consumerFactoryBody());
    }

    protected FunctionSourceGenerator producerFactory() {
        var kafkaProducerFactory = getMethodNamePrefix(topic) + KAFKA_SUFFIX + PRODUCER_SUFFIX + FACTORY_SUFFIX;
        return FunctionSourceGenerator
                .create(kafkaProducerFactory)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(ProducerFactory.class)
                        .addGeneric(GenericSourceGenerator.create(STRING), GenericSourceGenerator.create(getShortName(producerPayloadMessage))))
                .setBody(producerFactoryBody());
    }
}