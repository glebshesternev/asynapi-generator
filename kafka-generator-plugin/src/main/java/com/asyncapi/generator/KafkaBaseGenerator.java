package com.asyncapi.generator;

import com.asyncapi.parser.java.binding.channel.kafka.KafkaChannelBinding;
import com.asyncapi.parser.java.binding.operation.kafka.KafkaOperationBinding;
import com.asyncapi.parser.java.binding.operation.kafka.Property;
import com.asyncapi.parser.java.binding.server.kafka.KafkaServerBinding;
import com.asyncapi.parser.java.model.AsyncAPI;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.ChannelItem;
import com.asyncapi.parser.java.model.channel.message.Message;
import com.asyncapi.parser.java.schema.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.logging.log4j.util.Strings;
import org.burningwave.core.classes.AnnotationSourceGenerator;
import org.burningwave.core.classes.BodySourceGenerator;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.GenericSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.asyncapi.parser.java.binding.operation.kafka.KafkaOperationBinding.BACKOFF_RETRIES;

//todo refactor
public class KafkaBaseGenerator {

    public static final String SSL_PROPERTY = "ssl";

    public static final String DOT_DELIMINATOR = "\\.";

    public static final String BOOTSTRAP_SERVER_FIELD = "bootstrapServers";

    public static final String KAFKA_BINDING = "kafka";

    public static final String DELIMINATOR = ",";

    public static final String INPUT = "Input";

    public static final String FLOW = "Flow";

    public static final String STUB = "stub";

    public static final String TOPIC_PROPERTY = "topic";

    public static final String TOGGLE_PROPERTY = "enabled";

    public static final String PRODUCER_PREFIX = "producer";

    public static final String CONSUMER_PREFIX = "consumer";

    public static final String CHANNEL = "Channel";

    private static final String KEY_PATTERN = "%s.%s";

    private static final String PROP_PATTERN = "%s=%s\n";

    private static final String SPLIT_PATTERN = "[\\w&&[^\\d]]*";
    private static final String TECH_TOPIC_PREFIX = "__";

    public static final String DOT = ".";

    public static final String VOID = void.class.getSimpleName();
    public static final String STRING = String.class.getSimpleName();
    public static final String INTEGER = Integer.class.getSimpleName();
    public static final String WILDCARD = "?";

    public static final String HEADER_MAPPER_BEAN = "headerMapper";

    public static final String STRING_PATTERN = "\"%s\"";
    public static final String VALUE_INJECT_PATTERN = "\"${%s}\"";

    public static final String TOPIC_PACKAGE = "topic";
    public static final String MODEL_PACKAGE = "model";
    public static final String SCHEMA_PACKAGE = "schema";
    public static final String MESSAGE_PACKAGE = "message";

    public static final String FACTORY_SUFFIX = "Factory";
    public static final String TEMPLATE_SUFFIX = "Template";

    public static final String KAFKA_PACKAGE = "kafka";
    public static final String KAFKA_SUFFIX = "Kafka";

    public static final String HEADER_FIELD = "headers";
    public static final String PAYLOAD_FIELD = "payload";

    public static final String CONSUMER_PACKAGE = "consumer";
    public static final String CONSUMER_SUFFIX = "Consumer";

    public static final String PRODUCER_PACKAGE = "producer";
    public static final String PRODUCER_SUFFIX = "Producer";

    public static final String CONFIG_PACKAGE = "config";
    public static final String CONFIG_SUFFIX = "Config";
    public static final String DEFAULT_KAFKA_PROPERTY_FIELD = "defaultKafkaProperties";
    public static final String KAFKA_PROPERTY_FIELD = "kafkaProperties";
    public static final String KAFKA_TEMPLATE_FIELD = "kafkaTemplate";


    public static final String PROPERTY_PACKAGE = "props";
    public static final String PROPERTIES_SUFFIX = "Properties";
    public static final String COMMON_PROPERTIES_METHOD = "buildCommonProperties";
    public static final String CONSUMER_PROPERTIES_METHOD = "buildConsumerProperties";
    public static final String PRODUCER_PROPERTIES_METHOD = "buildProducerProperties";


    protected final AsyncAPI model;
    protected final File sourceDir;
    protected final File propertiesFolder;
    protected final String basePackage;
    protected final String channel;
    protected final String defaultNaming;

    protected String propertyClassName;
    protected String propertiesPrefix;

    protected Boolean commonPropMethodExist = false;
    protected Boolean consumerPropMethodExist = false;
    protected Boolean producerPropMethodExist = false;

    protected boolean consumerExist;
    protected boolean producerExist;

    protected boolean backoffExist;
    protected boolean headerExist;

    protected Map<String, Message> messageMap = new HashMap<>();
    protected Map<String, Schema> schemaMap = new HashMap<>();

    protected String consumerPayloadMessage;
    protected String producerPayloadMessage;
    protected String producerMessage;

    protected Map<String, Property> props;
    protected String topic;
    protected String propPrefix;

    protected KafkaBaseGenerator(AsyncAPI model, File sourceDir, File propertiesFolder, String basePackage, String channel, String defaultNaming) {
        this.model = model;
        this.sourceDir = sourceDir;
        this.propertiesFolder = propertiesFolder;
        this.basePackage = basePackage;
        this.channel = channel;
        this.defaultNaming = defaultNaming;
        this.props = new HashMap<>();
        checkComponents();
    }

    protected void generateProps() {
        var channelItem = model.getChannels()
                .get(channel);
        if (channelItem == null) {
            throw new RuntimeException("not found");
        }

        processChannelBindings(channelItem);

        var servers = channelItem.getServers();
        if (servers != null) {
            var serverName = servers.stream().findFirst();
            serverName.ifPresent(this::processServerBindings);
        }

        processConsumerBinding(channelItem);
        processProducerBinding(channelItem);

        propertiesFolder.mkdirs();
        var file = new File(propertiesFolder.getAbsolutePath() + "/application.properties");
        try {
            file.createNewFile();
            try (var fos = new FileOutputStream(file)) {
                for (Map.Entry<String, Property> entry : props.entrySet()) {
                    byte[] bytes = String.format(PROP_PATTERN,
                                    entry.getKey(),
                                    entry.getValue()
                                            .getValue()
                                            .toString()
                            )
                            .getBytes(StandardCharsets.UTF_8);
                    fos.write(bytes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processChannelBindings(ChannelItem channelItem) {
        var channelBindings = channelItem.getBindings();
        if (channelBindings != null) {
            var channelBinding = (KafkaChannelBinding) channelBindings.get(KAFKA_BINDING);
            topic = channelBinding.getTopic();
        } else {
            topic = channelItem.getRef();
        }
        propPrefix = getPropertiesPrefix(topic);
        props.put(String.format(KEY_PATTERN, propPrefix, TOPIC_PROPERTY), new Property(TOPIC_PROPERTY, String.class, topic, false));
        props.put(String.format(KEY_PATTERN, propPrefix, TOGGLE_PROPERTY), new Property(TOGGLE_PROPERTY, Boolean.class, Boolean.TRUE, false));
    }

    private void processServerBindings(String serverName) {
        var server = model.getServers().get(serverName);
        var serverBinding = (KafkaServerBinding) server.getBindings().get(KAFKA_BINDING);
        var bootstrapServers = serverBinding.getBootstrapServers();
        if (bootstrapServers != null) {
            var key = String.format(KEY_PATTERN, propPrefix, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
            var value = bootstrapServers.stream().collect(Collectors.joining(DELIMINATOR));
            props.put(key, new Property(BOOTSTRAP_SERVER_FIELD, List.class, value, true));
        }
        var protocol = server.getProtocol().toLowerCase(Locale.ROOT);
        if (protocol != null) {
            props.put(
                    String.format(KEY_PATTERN, propPrefix, CommonClientConfigs.SECURITY_PROTOCOL_CONFIG),
                    new Property("protocol", String.class, protocol, true)
            );
        }
        var ssl = serverBinding.getSsl();
        if (ssl != null) {
            ssl.buildProperties()
                    .forEach((key, value) -> props.put(String.format(KEY_PATTERN, propPrefix, key), value));
        }
    }

    private void processConsumerBinding(ChannelItem channelItem) {
        var consumerBinding = (KafkaOperationBinding) channelItem.getPublish()
                .getBindings()
                .get(KAFKA_BINDING);
        var consumerPrefix = propPrefix + "." + CONSUMER_PREFIX;
        consumerBinding.buildConsumerProperties()
                .entrySet()
                .forEach(prop -> {
                    props.put(String.format(KEY_PATTERN, consumerPrefix, prop.getKey()), prop.getValue());
                    if (prop.getKey().equals(BACKOFF_RETRIES)) backoffExist = true;
                });
    }

    private void processProducerBinding(ChannelItem channelItem) {
        var producerBinding = (KafkaOperationBinding) channelItem.getSubscribe()
                .getBindings()
                .get(KAFKA_BINDING);
        var producerPrefix = propPrefix + "." + PRODUCER_PREFIX;
        producerBinding.buildProducerProperties()
                .entrySet()
                .forEach(prop -> props.put(String.format(KEY_PATTERN, producerPrefix, prop.getKey()), prop.getValue()));
    }

    protected String getPropertiesPrefix(String channelName) {
        var list = split(channelName);
        return list.stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining(DOT));
    }

    protected String getClassNamePrefix(String channelName) {
        var list = split(channelName);
        return list.stream().map(String::toLowerCase).map(StringUtils::capitalize).reduce(String::concat).orElseThrow();
    }

    protected VariableSourceGenerator headerMapperField() {
        return VariableSourceGenerator.create(ObjectMapper.class, HEADER_MAPPER_BEAN).addModifier(Modifier.PRIVATE).addModifier(Modifier.FINAL);
    }

    protected String getStaticName() {
        var list = split(topic);
        list.remove(KAFKA_PACKAGE);
        list.add(CHANNEL);
        return list.stream().map(String::toUpperCase).collect(Collectors.joining("_"));
    }

    protected String getMethodNamePrefix(String channelName) {
        var list = split(channelName);
        return StringUtils.uncapitalize(list.stream().map(String::toLowerCase).map(StringUtils::capitalize).reduce(String::concat).orElseThrow());
    }

    protected AnnotationSourceGenerator bean() {
        return AnnotationSourceGenerator.create(Bean.class);
    }

    protected AnnotationSourceGenerator bean(String beanName) {
        return AnnotationSourceGenerator.create(Bean.class).addParameter(VariableSourceGenerator.create(beanName));
    }

    protected String getChannelName(String channelName) {
        var list = split(channelName);
        list.add(CHANNEL);
        return StringUtils.uncapitalize(list.stream().map(String::toLowerCase).map(StringUtils::capitalize).reduce(String::concat).orElseThrow());
    }

    protected List<String> split(String channelName) {
        List<String> allMatches = new ArrayList<>();
        if (defaultNaming == null) {
            var m = Pattern.compile(SPLIT_PATTERN).matcher(channelName.startsWith(TECH_TOPIC_PREFIX) ? channelName.substring(2) : channelName);
            m.toMatchResult();
            while (m.find()) allMatches.add(m.group());
            allMatches.add(KAFKA_PACKAGE);
            allMatches = allMatches.stream().filter(x -> !Strings.isEmpty(x)).collect(Collectors.toList());
        } else {
            allMatches.addAll(Arrays.asList(defaultNaming.split(DOT_DELIMINATOR)));
        }
        return allMatches;
    }

    protected void checkComponents() {
        var channelItem = model.getChannels()
                .get(channel);
        Objects.requireNonNull(channelItem, "Channel not found");
        var publish = channelItem.getPublish();
        KafkaOperationBinding consumer = null;
        if (publish != null && publish.getBindings() != null) {
            consumer = (KafkaOperationBinding) publish.getBindings()
                    .get(KAFKA_PACKAGE);
        }
        consumerExist = consumer != null;
        var subscribe = channelItem.getSubscribe();
        KafkaOperationBinding producer = null;
        if (subscribe != null && subscribe.getBindings() != null) {
            producer = (KafkaOperationBinding) subscribe.getBindings()
                    .get(KAFKA_PACKAGE);
        }
        producerExist = producer != null;
    }

    protected VariableSourceGenerator   staticString(String name, String value) {
        return VariableSourceGenerator
                .create(TypeDeclarationSourceGenerator.create("String"), name)
                .addModifier(Modifier.PUBLIC)
                .addModifier(Modifier.STATIC)
                .addModifier(Modifier.FINAL)
                .setValue(String.format(STRING_PATTERN, value));
    }

    protected AnnotationSourceGenerator conditionalOnProperty(boolean value) {
        return AnnotationSourceGenerator.create(ConditionalOnProperty.class)
                .addParameter("name", VariableSourceGenerator.create(String.format("\"%s.enabled\"", propPrefix)))
                .addParameter("havingValue", VariableSourceGenerator.create(String.format(STRING_PATTERN, value)));
    }

    protected VariableSourceGenerator kafkaPropertyField() {
        return VariableSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(propertyClassName, getShortName(propertyClassName)), KAFKA_PROPERTY_FIELD)
                .addModifier(Modifier.PRIVATE)
                .addModifier(Modifier.FINAL);
    }

    protected FunctionSourceGenerator headerMapper() {
        return FunctionSourceGenerator
                .create(HEADER_MAPPER_BEAN)
                .setReturnType(ObjectMapper.class)
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Bean.class))
                .addBodyCodeLine("var mapper =  JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build();")
                .addBodyCodeLine("mapper.registerModule(new JavaTimeModule());")
                .addBodyCodeLine("mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);")
                .addBodyCodeLine("mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);")
                .addBodyCodeLine("return mapper;");
    }

    protected FunctionSourceGenerator kafkaTemplateMethod() {
        var methodNamePrefix = getMethodNamePrefix(topic);
        var kafkaProducerFactory = methodNamePrefix + KAFKA_SUFFIX + PRODUCER_SUFFIX + FACTORY_SUFFIX;
        var kafkaTemplate = methodNamePrefix + KAFKA_SUFFIX + TEMPLATE_SUFFIX;
        return FunctionSourceGenerator.create(kafkaTemplate)
                .setReturnType(TypeDeclarationSourceGenerator.create(KafkaTemplate.class)
                        .addGeneric(GenericSourceGenerator.create(STRING), GenericSourceGenerator.create(getShortName(producerPayloadMessage))))
                .addParameter(VariableSourceGenerator.create(
                        TypeDeclarationSourceGenerator.create(ProducerFactory.class)
                                .addGeneric(GenericSourceGenerator.create(STRING),
                                        GenericSourceGenerator.create(getShortName(producerPayloadMessage))
                                ),
                        kafkaProducerFactory
                ))
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Bean.class))
                .addBodyCode(String.format("return new KafkaTemplate<>(%s);", kafkaProducerFactory));
    }

    protected BodySourceGenerator consumerFactoryBody() {
        var body = BodySourceGenerator.create()
                .addCodeLine("var map = defaultKafkaProperties.buildConsumerProperties();");
        if (commonPropMethodExist) body.addCodeLine("map.putAll(kafkaProperties.buildCommonProperties());");
        if (consumerPropMethodExist) body.addCodeLine("map.putAll(kafkaProperties.buildConsumerProperties());");
        body.addCodeLine("return new DefaultKafkaConsumerFactory<>(map);");
        return body;
    }

    protected BodySourceGenerator producerFactoryBody() {
        var body = BodySourceGenerator.create()
                .addCodeLine("var map = defaultKafkaProperties.buildProducerProperties();");
        if (commonPropMethodExist) body.addCodeLine("map.putAll(kafkaProperties.buildCommonProperties());");
        if (producerPropMethodExist) body.addCodeLine("map.putAll(kafkaProperties.buildProducerProperties());");
        body.addCodeLine("return new DefaultKafkaProducerFactory<>(map);");
        return body;
    }

    protected void generatePropertyClass() {
        propertiesPrefix = getPropertiesPrefix(topic);
        var className = getClassNamePrefix(topic) + PROPERTIES_SUFFIX;
        var packageName = basePackage + DOT + CONFIG_PACKAGE + DOT + PROPERTY_PACKAGE;
        propertyClassName = packageName + DOT + className;
        var generatedUnit = UnitSourceGenerator.create(packageName).addImport(HashMap.class, Strings.class);
        var clazz = ClassSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(className))
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Data.class))
                .addAnnotation(AnnotationSourceGenerator.create(Component.class));
        props.forEach((key, value) -> generateVariable(generatedUnit, clazz, value, key));

        var commonProp = extractProps(
                entry -> (entry.getKey().startsWith(propertiesPrefix + DOT + ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG) ||
                         entry.getKey().startsWith(propertiesPrefix + DOT + CommonClientConfigs.SECURITY_PROTOCOL_CONFIG)) &&
                         entry.getValue().isBaseProp(),
                entry -> entry.getKey().replace(propertiesPrefix + DOT, "")
        );

        var sslProp = extractProps(
                entry -> entry.getKey().startsWith(propertiesPrefix + DOT + SSL_PROPERTY) &&
                         entry.getValue().isBaseProp(),
                entry -> entry.getKey().replace(propertiesPrefix + DOT, "")
        );

        var commonMethod = buildCommonProperties(commonProp, sslProp);
        if (commonMethod != null) {
            commonPropMethodExist = true;
            clazz.addMethod(commonMethod);
        }

        var consumerProp = extractProps(
                entry -> entry.getKey().startsWith(propertiesPrefix + DOT + CONSUMER_PACKAGE) &&
                         entry.getValue().isBaseProp(),
                entry -> entry.getKey().replace(propertiesPrefix + DOT + CONSUMER_PACKAGE + DOT, "")
        );
        var consumerMethod = buildConsumerProperties(consumerProp, CONSUMER_PROPERTIES_METHOD);
        if (consumerMethod != null) {
            consumerPropMethodExist = true;
            clazz.addMethod(consumerMethod);
        }

        var producerProp = extractProps(
                entry -> entry.getKey().startsWith(propertiesPrefix + DOT + PRODUCER_PACKAGE) &&
                     entry.getValue().isBaseProp(),
                entry -> entry.getKey().replace(propertiesPrefix + DOT + PRODUCER_PACKAGE + DOT, "")
        );
        var producerMethod = buildProducerProperties(producerProp, PRODUCER_PROPERTIES_METHOD);
        if (producerMethod != null) {
            producerPropMethodExist = true;
            clazz.addMethod(producerMethod);
        }

        generatedUnit.addClass(clazz);
        generatedUnit.make();
        generatedUnit.storeToClassPath(sourceDir.getAbsolutePath());
    }

    protected List<Pair<String, String>> extractProps(
            Predicate<Map.Entry<String, Property>> predicate, Function<Map.Entry<String, Property>, String> extractKey
    ) {
        return props
                .entrySet()
                .stream()
                .filter(predicate)
                .map(x -> Pair.of(extractKey.apply(x), x.getValue().getFieldName()))
                .collect(Collectors.toList());
    }

    protected VariableSourceGenerator loggerField(String className) {
        return VariableSourceGenerator
                .create(Logger.class, "LOGGER")
                .addModifier(Modifier.PUBLIC)
                .addModifier(Modifier.STATIC)
                .addModifier(Modifier.FINAL)
                .setValue(String.format("LoggerFactory.getLogger(%s.class)", className));
    }

    protected VariableSourceGenerator annotationParameter(String name, String property) {
        return VariableSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(""), name).setValue(String.format(VALUE_INJECT_PATTERN, property));
    }

    protected VariableSourceGenerator defaultKafkaPropertyField() {
        return VariableSourceGenerator
                .create(KafkaProperties.class, DEFAULT_KAFKA_PROPERTY_FIELD)
                .addModifier(Modifier.PRIVATE)
                .addModifier(Modifier.FINAL);
    }

    protected FunctionSourceGenerator buildCommonProperties(List<Pair<String, String>> commonProp, List<Pair<String, String>> sslProp) {
        if (commonProp.isEmpty()) return null;
        var method = FunctionSourceGenerator
                .create(COMMON_PROPERTIES_METHOD)
                .addModifier(Modifier.PUBLIC)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(Map.class)
                        .addGeneric(GenericSourceGenerator.create(STRING), GenericSourceGenerator.create("Object")))
                .addBodyCodeLine("Map<String, Object> map = new HashMap<>();");
        commonProp.forEach(x -> method.addBodyCodeLine(String.format("map.put(" + STRING_PATTERN + ", %s);", x.getKey(), x.getValue())));

        if (sslProp != null && !sslProp.isEmpty()) {
            method.addBodyCodeLine("if (\"ssl\".equalsIgnoreCase(protocol)) {");
            sslProp.forEach(x -> method.addBodyCodeLine(String.format("\tmap.put(" + STRING_PATTERN + ", %s);", x.getKey(), x.getValue())));
            method.addBodyCodeLine("}");
        }

        method.addBodyCodeLine("return map;");
        return method;
    }

    protected FunctionSourceGenerator buildConsumerProperties(List<Pair<String, String>> list, String methodName) {
        var method = generatePropertyBuilderMethod(list, methodName);
        if (commonPropMethodExist) method.addBodyCodeLine("map.putAll(buildCommonProperties());");
        method.addBodyCodeLine("return map;");
        return method;
    }

    protected FunctionSourceGenerator buildProducerProperties(List<Pair<String, String>> list, String methodName) {
        var method = generatePropertyBuilderMethod(list, methodName);
        if (commonPropMethodExist) method.addBodyCodeLine("map.putAll(buildCommonProperties());");
        method.addBodyCodeLine("return map;");
        return method;
    }

    protected FunctionSourceGenerator generatePropertyBuilderMethod(List<Pair<String, String>> list, String methodName) {

        if (list.isEmpty()) return null;
        var method = FunctionSourceGenerator
                .create(methodName)
                .addModifier(Modifier.PUBLIC)
                .setReturnType(TypeDeclarationSourceGenerator
                        .create(Map.class)
                        .addGeneric(GenericSourceGenerator.create(STRING), GenericSourceGenerator.create("Object")))
                .addBodyCodeLine("Map<String, Object> map = new HashMap<>();");
        list.forEach(x -> method.addBodyCodeLine(String.format("map.put(\"%s\", %s);", x.getKey(), x.getValue())));
        return method;
    }

    protected void generateVariable(UnitSourceGenerator sourceUnit, ClassSourceGenerator classUnit, Property value, String key) {

        if (!Class.class.getPackageName()
                .equals(value.getClazz()
                        .getPackageName())) {
            sourceUnit.addImport(value.getClazz());
        }
        classUnit.addField(VariableSourceGenerator
                .create(TypeDeclarationSourceGenerator.create(value.getClazz().getSimpleName()), value.getFieldName())
                .addModifier(Modifier.PRIVATE)
                .addAnnotation(AnnotationSourceGenerator
                        .create(Value.class)
                        .addParameter(VariableSourceGenerator.create(String.format(VALUE_INJECT_PATTERN, key)))));
    }

    protected String[] getCls(Object ref) {
        return ((Reference) ref).getRef()
                .split("/");
    }

    protected void generateModel() {
        messageMap = model.getComponents()
                .getMessages()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> (Message) v.getValue()));
        for (var e : model.getComponents()
                .getSchemas()
                .entrySet())
            schemaMap.put(e.getKey(), (Schema) e.getValue());
        var channelItem = model.getChannels()
                .get(channel);
        var consumerTuple = generateMessage(getCls(channelItem.getPublish()
                .getMessage()));
        consumerPayloadMessage = consumerTuple.getValue();
        var oroducerTuple = generateMessage(getCls(channelItem.getSubscribe()
                .getMessage()));
        producerPayloadMessage = oroducerTuple.getValue();
        producerMessage = oroducerTuple.getKey();
    }

    protected Pair<String, String> generateMessage(String[] cls) {
        var className = StringUtils.capitalize(cls[cls.length - 1]);
        Message message = messageMap.get(cls[cls.length - 1]);
        String packageName = basePackage + DOT + MODEL_PACKAGE + DOT + MESSAGE_PACKAGE;

        var unit = UnitSourceGenerator.create(packageName);

        var clazz = ClassSourceGenerator.create(TypeDeclarationSourceGenerator.create(className));
        clazz.addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Data.class))
                .addAnnotation(AnnotationSourceGenerator.create(AllArgsConstructor.class))
                .addAnnotation(AnnotationSourceGenerator.create(NoArgsConstructor.class))
                .addAnnotation(AnnotationSourceGenerator.create(Builder.class));

        var messageHeaders = message.getHeaders();
        if (messageHeaders instanceof Reference) {
            var headerFile = generateSchema(((Reference) messageHeaders).getRef()
                    .split("/"));
            var headerClass = headerFile.packageName + DOT + headerFile.typeSpec.name;
            unit.addImport(headerClass);
            headerExist = true;
            clazz.addField(VariableSourceGenerator.create(TypeDeclarationSourceGenerator.create(getShortName(headerClass)), HEADER_FIELD)
                    .addModifier(Modifier.PRIVATE));
        }

        String payloadClass = null;
        var messagePayload = message.getPayload();
        if (messagePayload instanceof Schema && ((Schema) messagePayload).getRef() != null) {
            var payloadFile = generateSchema(((Schema) messagePayload).getRef().split("/"));
            payloadClass = payloadFile.packageName + DOT + payloadFile.typeSpec.name;
            unit.addImport(payloadClass);
            clazz.addField(VariableSourceGenerator
                    .create(TypeDeclarationSourceGenerator.create(getShortName(payloadClass)), PAYLOAD_FIELD)
                    .addModifier(Modifier.PRIVATE));
        }

        unit.addClass(clazz);
        unit.make();
        unit.storeToClassPath(sourceDir.getAbsolutePath());
        return Pair.of(packageName + DOT + className, payloadClass);
    }

    protected AnnotationSourceGenerator component() {
        return AnnotationSourceGenerator.create(Component.class);
    }

    protected AnnotationSourceGenerator requiredArgsConstructor() {
        return AnnotationSourceGenerator.create(RequiredArgsConstructor.class);
    }

    protected String getShortName(String fullName) {
        var list = fullName.split(DOT_DELIMINATOR);
        return StringUtils.capitalize(list[list.length - 1]);
    }

    protected JavaFile generateSchema(String[] cls) {
        var name = StringUtils.capitalize(cls[cls.length - 1]);
        Schema schema = schemaMap.get(cls[cls.length - 1]);
        String fullPackageName = basePackage + DOT + MODEL_PACKAGE + DOT + SCHEMA_PACKAGE;
        TypeSpec.Builder type;
        type = TypeSpec.classBuilder(name);
        type.addAnnotation(ClassName.get("lombok", "Data"))
                .addAnnotation(ClassName.get("lombok", "Builder"))
                .addAnnotation(ClassName.get("lombok", "AllArgsConstructor"))
                .addAnnotation(ClassName.get("lombok", "NoArgsConstructor"));

        type.addJavadoc(Objects.requireNonNullElse(schema.getDescription(), ""))
                .addModifiers(javax.lang.model.element.Modifier.PUBLIC);

        genFields(type, schema);

        var typeSpec = type.build();
        JavaFile file = JavaFile.builder(fullPackageName, typeSpec)
                .skipJavaLangImports(true)
                .build();
        try {
            file.writeTo(sourceDir);
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void genFields(TypeSpec.Builder type, Schema schema) {
        Map<String, Schema> properties = schema.getProperties();
        if (properties != null) properties.forEach((key, value) -> genField(type, key, value));
    }

    protected void genField(TypeSpec.Builder type, String key, Schema value) {
        TypeName fieldType = TypeName.get(String.class);
        var isArray = isArray(value.getType());
        if (isArray) {
            value = value.getItems();
        }
        if (value.getType() == null && value.getRef() != null) {
            var file = generateSchema(value.getRef()
                    .split("/"));
            fieldType = ClassName.get(file.packageName, file.typeSpec.name);
        } else {
            Objects.requireNonNull(value.getType());
            var tmp = value.getType()
                    .toString();

            switch (tmp) {
                case "boolean":
                    fieldType = TypeName.get(Boolean.class);
                    break;
                case "string":
                    if (value.getFormat() == null) {
                        break;
                    }
                    break;
                case "number":
                    fieldType = TypeName.DOUBLE.box();
                    break;
                case "integer":
                    fieldType = TypeName.LONG.box();
                    break;
                default:
                    break;
            }
            if (value.getFormat() != null) {
                try {
                    fieldType = TypeName.get(Class.forName(value.getFormat()
                            .toString()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(String.format("Generate exception. Type[%s]. FieldType[%s]",
                            type.originatingElements.toString(),
                            value.getFormat()
                    ), e);
                }
            }
        }
        if (isArray) {
            fieldType = ParameterizedTypeName.get(ClassName.get(List.class), fieldType);
        }
        FieldSpec.Builder specBuilder = FieldSpec.builder(fieldType, key, javax.lang.model.element.Modifier.PRIVATE);
        if (value.getDescription() != null) {
            specBuilder.addJavadoc(value.getDescription());
        }

        boolean isNullable = Objects.requireNonNullElse(value.getRequired() != null, true);

        extreact(type, specBuilder, isNullable);
    }

    protected boolean isArray(Object obj) {
        return obj != null && obj.toString()
                .equals("array");
    }

    public static void extreact(TypeSpec.Builder type, FieldSpec.Builder specBuilder, boolean isNullable) {
        if (!isNullable) {
            specBuilder.addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                    .addMember("required",
                            CodeBlock.builder()
                                    .add(isNullable ? "false" : "true")
                                    .build()
                    )
                    .build());
            specBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("lombok", "NonNull"))
                    .build());
        }

        type.addField(specBuilder.build());
    }

    public AsyncAPI getModel() {
        return this.model;
    }

    public File getSourceDir() {
        return this.sourceDir;
    }

    public Map<String, Property> getProps() {
        return this.props;
    }

    public String getBasePackage() {
        return this.basePackage;
    }

    public String getTopic() {
        return this.topic;
    }

}
