package com.asyncapi.generator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.burningwave.core.classes.AnnotationSourceGenerator;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.GenericSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dispatcher.RoundRobinLoadBalancingStrategy;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.dsl.KafkaProducerMessageHandlerSpec;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.support.DefaultErrorMessageStrategy;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StringUtils;
import com.asyncapi.parser.java.model.AsyncAPI;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;

//todo refactor
public class KafkaStreamSourceConfigGenerator extends KafkaBaseGenerator {

    private static final String LOCAL_ERROR_CHANNEL = "localErrorChannel";
    private static final String CONSUMER_FACTORY = StringUtils.uncapitalize(CONSUMER_SUFFIX) + FACTORY_SUFFIX;
    private static final String LISTENER = "Listener";
    private static final String STREAM = "Stream";
    private static final String SERVICE = "service";
    private static final String SEND = "send";
    private static final String ADAPTER = "adapter";
    private static final String CONSUMER_MESSAGE_CHANNEL_ID = "CONSUMER_MESSAGE_CHANNEL_ID";
    private static final String ERROR_MESSAGE_CHANNEL = "ERROR_MESSAGE_CHANNEL";
    private static final String IMPL_SUFFIX = "Impl";
    private static final String CHANNEL_ADAPTER = CHANNEL.toLowerCase(Locale.ROOT) + StringUtils.capitalize(ADAPTER);

    private String producerAdapter;
    private String producerServiceInterface;
    private String configClass;

    private String adapterSendMethod;

    public KafkaStreamSourceConfigGenerator(
            AsyncAPI model, File sourceDir, File propertiesFolder,
            String basePackage, String channel, String defaultNaming
    ) {
        super(model, sourceDir, propertiesFolder, basePackage, channel, defaultNaming);
    }

    public void generate() {
        generateProps();
        if (consumerExist || producerExist) {
            generateModel();
            generatePropertyClass();
            generateConfigClass();
        }
        if (producerExist) {
            generateProducerServiceInterface();
            generateProducerServiceClass();
        }
    }

    private VariableSourceGenerator createPropertyVar() {
        return VariableSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(propertyClassName, getShortName(propertyClassName)), KAFKA_PROPERTY_FIELD)
                .addModifier(Modifier.PRIVATE)
                .addModifier(Modifier.FINAL);
    }

    private void generateProducerAdapterInterface() {
        adapterSendMethod = SEND + getClassNamePrefix(topic).replace(KAFKA_SUFFIX, "");
        var className = getClassNamePrefix(topic).replace(KAFKA_SUFFIX, CHANNEL + StringUtils.capitalize(ADAPTER));
        var packageName = basePackage + DOT + SERVICE;
        producerAdapter = packageName + DOT + className;

        var generatedUnit = UnitSourceGenerator.create(packageName);
        generatedUnit.addImport(producerPayloadMessage);
        generatedUnit.addStaticImport(configClass + DOT + getStaticName());
        var clazz = ClassSourceGenerator.createInterface(TypeDeclarationSourceGenerator.create(className));
        clazz.addModifier(Modifier.PUBLIC).addAnnotation(AnnotationSourceGenerator.create(MessagingGateway.class));

        var method = FunctionSourceGenerator
                .create(adapterSendMethod)
                .setReturnType(VOID)
                .addAnnotation(AnnotationSourceGenerator
                        .create(Gateway.class)
                        .addParameter("requestChannel", VariableSourceGenerator.create(getStaticName())))
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator
                        .create(GenericMessage.class)
                        .addGeneric(GenericSourceGenerator.create(getShortName(producerPayloadMessage))), getMethodNamePrefix(topic)));
        clazz.addMethod(method);

        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private void generateProducerServiceInterface() {
        var className = getClassNamePrefix(topic) + StringUtils.capitalize(SEND) + StringUtils.capitalize(SERVICE);
        var packageName = basePackage + DOT + SERVICE;
        producerServiceInterface = packageName + DOT + className;

        var generatedUnit = UnitSourceGenerator.create(packageName);
        generatedUnit.addImport(producerMessage);

        var clazz = ClassSourceGenerator.createInterface(TypeDeclarationSourceGenerator.create(className));
        clazz.addModifier(Modifier.PUBLIC);
        var method = FunctionSourceGenerator
                .create(adapterSendMethod)
                .setReturnType(VOID)
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(getShortName(producerMessage)),
                        StringUtils.uncapitalize(getShortName(producerMessage))
                ));
        clazz.addMethod(method);

        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private void generateProducerServiceClass() {
        var className = getClassNamePrefix(topic) + StringUtils.capitalize(SEND) + StringUtils.capitalize(SERVICE) + IMPL_SUFFIX;
        var generatedUnit = UnitSourceGenerator.create(basePackage + DOT + SERVICE);

        generatedUnit.addImport(GenericMessage.class);
        generatedUnit.addImport(producerMessage);
        if (headerExist) generatedUnit.addImport(Map.class);

        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        clazz
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(component())
                .addAnnotation(requiredArgsConstructor())
                .addConcretizedType(TypeDeclarationSourceGenerator.create(getShortName(producerServiceInterface)))
                .addField(VariableSourceGenerator
                        .create(TypeDeclarationSourceGenerator.create(getShortName(producerAdapter)), CHANNEL_ADAPTER)
                        .addModifier(Modifier.PRIVATE)
                        .addModifier(Modifier.FINAL));
        if (headerExist) clazz.addField(headerMapperField());

        var method = FunctionSourceGenerator
                .create(adapterSendMethod)
                .setReturnType(VOID)
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create("Override"))
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(getShortName(producerMessage)),
                        StringUtils.uncapitalize(getShortName(producerMessage))
                ));
        if (headerExist) method.addBodyCodeLine(String.format("Map<String, Object> map = %s.convertValue(%s.getHeaders(), Map.class);",
                HEADER_MAPPER_BEAN,
                StringUtils.uncapitalize(getShortName(producerMessage))
        )).addBodyCodeLine(String.format("%s.%s(new GenericMessage<>(%s.getPayload(), map));",
                CHANNEL_ADAPTER,
                adapterSendMethod,
                StringUtils.uncapitalize(getShortName(producerMessage))
        ));
        else method.addBodyCodeLine(String.format("%s.%s(new GenericMessage<>(%s.getPayload()));",
                CHANNEL_ADAPTER,
                adapterSendMethod,
                StringUtils.uncapitalize(getShortName(producerMessage))));

        clazz.addMethod(method);

        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private void generateConfigClass() {
        var className = getClassNamePrefix(topic) + CONFIG_SUFFIX;
        var packageName = basePackage + DOT + CONFIG_PACKAGE;
        configClass = packageName + DOT + className;

        var generatedUnit = UnitSourceGenerator.create(packageName);
        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        generatedUnit.addImport(DefaultKafkaConsumerFactory.class,
                DefaultKafkaProducerFactory.class,
                RoundRobinLoadBalancingStrategy.class,
                ObjectToJsonTransformer.class,
                Transformers.class,
                IntegrationMessageHeaderAccessor.class,
                KafkaMessageDrivenChannelAdapter.class,
                ContainerProperties.class,
                ErrorMessageSendingRecoverer.class,
                DefaultErrorMessageStrategy.class,
                DirectChannel.class,
                MessageChannels.class,
                Kafka.class,
                DefaultKafkaHeaderMapper.class,
                IntegrationFlow.class,
                IntegrationFlows.class
        );
        if (headerExist) {
            generatedUnit.addImport(DeserializationFeature.class,
                    MapperFeature.class,
                    ObjectMapper.class,
                    SerializationFeature.class,
                    JsonMapper.class,
                    JavaTimeModule.class
            );
        }
        if (backoffExist) generatedUnit.addImport(RetryTemplate.class);
        generateProducerAdapterInterface();
        generatedUnit.addImport(producerAdapter);

        clazz
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(RequiredArgsConstructor.class))
                .addAnnotation(AnnotationSourceGenerator.create(Configuration.class))
                .addAnnotation(AnnotationSourceGenerator
                        .create(IntegrationComponentScan.class)
                        .addParameter("basePackageClasses",
                                VariableSourceGenerator.create(String.format("%s.class", getShortName(producerAdapter)))
                        ));

        clazz
                .addField(staticString(getStaticName(), getMethodNamePrefix(topic).concat(INPUT).concat(CHANNEL)))
                .addField(staticString(CONSUMER_MESSAGE_CHANNEL_ID, propPrefix + DOT + "messageHandler"))
                .addField(staticString(ERROR_MESSAGE_CHANNEL, propPrefix + DOT + "errorChannel"))
                .addField(VariableSourceGenerator
                        .create(KafkaProperties.class, DEFAULT_KAFKA_PROPERTY_FIELD)
                        .addModifier(Modifier.PRIVATE)
                        .addModifier(Modifier.FINAL))
                .addField(createPropertyVar());

        if (consumerExist || producerExist) clazz.addMethod(stubInputFlow()).addMethod(inputChannel()).addMethod(localErrorChannel());
        if (consumerExist) clazz.addMethod(inputFlow()).addMethod(messageHandler()).addMethod(producerFactory().addModifier(Modifier.PRIVATE));
        if (producerExist) clazz.addMethod(kafkaListenerStream()).addMethod(consumerFactory().addModifier(Modifier.PRIVATE));
        if (headerExist) clazz.addMethod(headerMapper());
        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    private FunctionSourceGenerator kafkaListenerStream() {
        var method = FunctionSourceGenerator.create(getMethodNamePrefix(topic) + LISTENER + STREAM)
                                            .addAnnotation(bean())
                                            .addAnnotation(conditionalOnProperty(true))
                                            .setReturnType(IntegrationFlow.class)
                                            .addModifier(Modifier.PUBLIC)
                                            .addBodyCodeLine("return IntegrationFlows")
                                            .addBodyCodeLine("\t.from(Kafka")
                                            .addBodyCodeLine(String.format("\t\t.messageDrivenChannelAdapter(%s(),", CONSUMER_FACTORY))
                                            .addBodyCodeLine("\t\t\tKafkaMessageDrivenChannelAdapter.ListenerMode.record,")
                                            .addBodyCodeLine("\t\t\tkafkaProperties.getTopic()")
                                            .addBodyCodeLine("\t\t)")
                                            .addBodyCodeLine("\t\t.configureListenerContainer(container -> container")
                                            .addBodyCodeLine("\t\t\t.ackMode(ContainerProperties.AckMode.RECORD)")
                                            .addBodyCodeLine("\t\t\t.syncCommits(true)")
                                            .addBodyCodeLine("\t\t\t.id(CONSUMER_MESSAGE_CHANNEL_ID)")
                                            .addBodyCodeLine("\t\t\t.get())")
                                            .addBodyCodeLine(String.format(
                                                    "\t\t.recoveryCallback(new ErrorMessageSendingRecoverer(%s(), new DefaultErrorMessageStrategy()))",
                                                    LOCAL_ERROR_CHANNEL
                                            ));

        if (backoffExist) {
            method.addBodyCodeLine("\t\t.retryTemplate(RetryTemplate.builder()")
                  .addBodyCodeLine("\t\t\t.maxAttempts(kafkaProperties.getBackoffRetries())")
                  .addBodyCodeLine("\t\t\t.exponentialBackoff(kafkaProperties.getBackoffInitial(),")
                  .addBodyCodeLine("\t\t\t\tkafkaProperties.getBackoffMultiplier(),")
                  .addBodyCodeLine("\t\t\t\tkafkaProperties.getBackoffMax())")
                  .addBodyCodeLine("\t\t\t.build())");
        }

        method.addBodyCodeLine("\t\t.get())")
              .addBodyCodeLine("\t.transform(Transformers.fromJson())")
              .addBodyCodeLine("\t.logAndReply();");
        return method;
    }

    private FunctionSourceGenerator messageHandler() {
        return FunctionSourceGenerator
                .create("messageHandler")
                .addModifier(Modifier.PRIVATE)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(KafkaProducerMessageHandlerSpec.class)
                        .addGeneric(GenericSourceGenerator.create(INTEGER))
                        .addGeneric(GenericSourceGenerator.create(STRING))
                        .addGeneric(GenericSourceGenerator.create(WILDCARD)))
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator
                        .create(PRODUCER_SUFFIX + FACTORY_SUFFIX)
                        .addGeneric(GenericSourceGenerator.create(INTEGER))
                        .addGeneric(GenericSourceGenerator.create(STRING)), PRODUCER_PACKAGE + FACTORY_SUFFIX))
                .addParameter(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(STRING), TOPIC_PROPERTY))
                .addBodyCode(messageHandlerBody());
    }

    private String messageHandlerBody() {
        return String.format("return Kafka.outboundChannelAdapter(producerFactory)\n" +
                             "\t.messageKey(m -> m.getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))\n" +
                             "\t.headerMapper(new DefaultKafkaHeaderMapper())\n" + "\t.topic(%1$s)\n" +
                             "\t.configureKafkaTemplate(t -> t.id(\"kafkaTemplate:\" + %1$s));", TOPIC_PROPERTY);
    }

    private FunctionSourceGenerator localErrorChannel() {
        return FunctionSourceGenerator
                .create(LOCAL_ERROR_CHANNEL)
                .addAnnotation(bean(ERROR_MESSAGE_CHANNEL))
                .addModifier(Modifier.PUBLIC)
                .setReturnType(MessageChannel.class)
                .addBodyCodeLine("return new DirectChannel(new RoundRobinLoadBalancingStrategy());");
    }

    private FunctionSourceGenerator inputFlow() {
        return FunctionSourceGenerator
                .create(getMethodNamePrefix(topic) + INPUT + FLOW)
                .addModifier(Modifier.PUBLIC)
                .setReturnType(IntegrationFlow.class)
                .addAnnotation(bean())
                .addAnnotation(conditionalOnProperty(true))
                .addBodyCodeLine(String.format("return IntegrationFlows.from(%s)", getStaticName()))
                .addBodyCodeLine("\t.transform(Transformers.toJson(ObjectToJsonTransformer.ResultType.STRING))")
                .addBodyCodeLine(
                        "\t.publishSubscribeChannel(c -> c.subscribe(f -> f.handle(messageHandler(producerFactory(), kafkaProperties.getTopic()))))")
                .addBodyCodeLine("\t.get();");
    }

    private FunctionSourceGenerator stubInputFlow() {
        return FunctionSourceGenerator
                .create(STUB + getClassNamePrefix(topic).replace("Kafka", "").concat(INPUT).concat(FLOW))
                .addModifier(Modifier.PUBLIC)
                .setReturnType(IntegrationFlow.class)
                .addAnnotation(conditionalOnProperty(false))
                .addAnnotation(bean())
                .addBodyCodeLine(String.format("return IntegrationFlows.from(%s).nullChannel();", getStaticName()));
    }

    private FunctionSourceGenerator inputChannel() {
        return FunctionSourceGenerator
                .create(getMethodNamePrefix(topic).concat(CHANNEL))
                .addModifier(Modifier.PUBLIC)
                .setReturnType(MessageChannel.class)
                .addAnnotation(bean(getStaticName()))
                .addBodyCodeLine("return MessageChannels.direct().get();");
    }

    protected FunctionSourceGenerator consumerFactory() {
        String kafkaConsumerFactory = StringUtils.uncapitalize(CONSUMER_SUFFIX) + FACTORY_SUFFIX;
        return FunctionSourceGenerator
                .create(kafkaConsumerFactory)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(ConsumerFactory.class)
                        .addGeneric(GenericSourceGenerator.create(INTEGER), GenericSourceGenerator.create(STRING)))
                .setBody(consumerFactoryBody());
    }


    protected FunctionSourceGenerator producerFactory() {
        var kafkaProducerFactory = StringUtils.uncapitalize(PRODUCER_SUFFIX) + FACTORY_SUFFIX;
        return FunctionSourceGenerator
                .create(kafkaProducerFactory)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(ProducerFactory.class)
                        .addGeneric(GenericSourceGenerator.create(INTEGER), GenericSourceGenerator.create(STRING)))
                .setBody(producerFactoryBody());
    }
}