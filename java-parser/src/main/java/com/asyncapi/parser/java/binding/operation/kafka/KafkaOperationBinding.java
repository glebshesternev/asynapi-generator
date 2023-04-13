package com.asyncapi.parser.java.binding.operation.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.asyncapi.parser.java.binding.operation.OperationBinding;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KafkaOperationBinding extends OperationBinding {

    public static final String BACKOFF_RETRIES = "backoff.retries";
    public static final String BACKOFF_MAX = "backoff.max";
    public static final String BACKOFF_MULTIPLIER = "backoff.multiplier";
    public static final String BACKOFF_INITIAL = "backoff.initial";

    @Nullable private String acks;
    @Nullable private Integer batchSize;
    @Nullable private Integer bufferMemory;
    @Nullable private String clientId;
    @Nullable private String compressionType;
    @Nullable private Class<?> keySerializer = StringSerializer.class;
    @Nullable private Class<?> valueSerializer = JsonSerializer.class;
    @Nullable private Integer retries;
    @Nullable private String transactionIdPrefix;
    @Nullable private Integer autoCommitInterval;
    @Nullable private Integer retryBackoff;
    @Nullable private String autoOffsetReset;
    @Nullable private Boolean enableAutoCommit;
    @Nullable private Integer fetchMaxWait;
    @Nullable private Integer fetchMinSize;
    @NotNull private String groupId;
    @Nullable private Integer heartbeatInterval;
    @Nullable private IsolationLevel isolationLevel;
    @Nullable private Class<?> keyDeserializer;
    @Nullable private Class<?> valueDeserializer = JsonDeserializer.class;
    @Nullable private Integer maxPollRecords;
    /*
     *  generate backoff configs if backoffRetries initiated
     */
    @Nullable private Integer backoffRetries;
    @Nullable private Integer backoffInitial = 1000;
    @Nullable private Double backoffMultiplier = 2D;
    @Nullable private Integer backoffMax = 10000;
    @Nullable private Integer maxInFlightRequestsPerConnection;
    @Nullable private Integer sendBuffer;
    @Nullable private Integer receiveBuffer;

    public Map<String, Property> buildProducerProperties() {
        Properties properties = new Properties();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this::getAcks)
           .as(value -> new Property("acks", String.class, value, true))
           .to(properties.in(ProducerConfig.ACKS_CONFIG));
        map.from(this::getBatchSize)
           .as(value -> new Property("batchSize", Integer.class, value, true))
           .to(properties.in(ProducerConfig.BATCH_SIZE_CONFIG));
        map.from(this::getBufferMemory)
           .as(value -> new Property("bufferMemory", Integer.class, value, true))
           .to(properties.in(ProducerConfig.BUFFER_MEMORY_CONFIG));
        map.from(this::getClientId)
           .as(value -> new Property("clientId", String.class, value, true))
           .to(properties.in(ProducerConfig.CLIENT_ID_CONFIG));
        map.from(this::getCompressionType)
           .as(value -> new Property("compressionType", String.class, value, true))
           .to(properties.in(ProducerConfig.COMPRESSION_TYPE_CONFIG));
        map.from(this::getKeySerializer)
           .as(value -> new Property("keySerializer", Class.class, value.getName(), true))
           .to(properties.in(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        map.from(this::getRetries)
           .as(value -> new Property("retries", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.RETRIES_CONFIG));
        map.from(this::getValueSerializer)
           .as(value -> new Property("valueSerializer", Class.class, value.getName(), true))
           .to(properties.in(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
        map.from(this::getRetries)
           .as(value -> new Property("retries", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.RETRIES_CONFIG));
        map.from(this::getMaxInFlightRequestsPerConnection)
           .as(value -> new Property("maxInFlightRequestsPerConnection", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION));
        map.from(this::getSendBuffer)
           .as(value -> new Property("sendBuffer", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.SEND_BUFFER_CONFIG));
        map.from(this::getReceiveBuffer)
           .as(value -> new Property("receiveBuffer", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.RECEIVE_BUFFER_CONFIG));
        map.from(this::getReceiveBuffer)
           .as(value -> new Property("retryBackoff", Integer.class, value.toString(), true))
           .to(properties.in(ProducerConfig.RETRY_BACKOFF_MS_CONFIG));
        return properties;
    }

    public Map<String, Property> buildConsumerProperties() {
        Properties properties = new Properties();
        PropertyMapper map = PropertyMapper.get()
                                           .alwaysApplyingWhenNonNull();
        map.from(this::getAutoCommitInterval)
           .as(value -> new Property("autoCommitInterval", Integer.class, value, true))
           .to(properties.in(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
        map.from(this::getAutoOffsetReset)
           .as(value -> new Property("autoOffsetReset", String.class, value, true))
           .to(properties.in(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
        map.from(this::getClientId)
           .as(value -> new Property("clientId", String.class, value, true))
           .to(properties.in(ConsumerConfig.CLIENT_ID_CONFIG));
        map.from(this::getEnableAutoCommit)
           .as(value -> new Property("enableAutoCommit", Boolean.class, value, true))
           .to(properties.in(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
        map.from(this::getFetchMaxWait)
           .as(value -> new Property("fetchMaxWait", Integer.class, value, true))
           .to(properties.in(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG));
        map.from(this::getFetchMinSize)
           .as(value -> new Property("fetchMinSize", Integer.class, value, true))
           .to(properties.in(ConsumerConfig.FETCH_MIN_BYTES_CONFIG));
        map.from(this::getGroupId)
           .as(value -> new Property("groupId", String.class, value, true))
           .to(properties.in(ConsumerConfig.GROUP_ID_CONFIG));
        map.from(this::getHeartbeatInterval)
           .as(value -> new Property("heartbeatInterval", Integer.class, value, true))
           .to(properties.in(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
        map.from(this::getIsolationLevel)
           .as(value -> new Property("isolationLevel", IsolationLevel.class, value.name(), true))
           .to(properties.in(ConsumerConfig.ISOLATION_LEVEL_CONFIG));
        map.from(this::getKeyDeserializer)
           .as(value -> new Property("keyDeserializer", Class.class, value.getName(), true))
           .to(properties.in(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        map.from(this::getValueDeserializer)
           .as(value -> new Property("valueDeserializer", Class.class, value.getName(), true))
           .to(properties.in(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        map.from(this::getMaxPollRecords)
           .as(value -> new Property("Integer", Integer.class, value, true))
           .to(properties.in(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));
        map.from(this::getBackoffInitial)
           .as(value -> new Property("backoffInitial", Integer.class, value.toString(), false))
           .to(properties.in(BACKOFF_INITIAL));
        map.from(this::getBackoffMax)
           .as(value -> new Property("backoffMax", Integer.class, value.toString(), false))
           .to(properties.in(BACKOFF_MAX));
        map.from(this::getBackoffMultiplier)
           .as(value -> new Property("backoffMultiplier", Double.class, value.toString(), false))
           .to(properties.in(BACKOFF_MULTIPLIER));
        map.from(this::getBackoffRetries)
           .as(value -> new Property("backoffRetries", Integer.class, value.toString(), false))
           .to(properties.in(BACKOFF_RETRIES));
        return properties;
    }

    public enum IsolationLevel {
        READ_UNCOMMITTED((byte) 0),
        READ_COMMITTED((byte) 1);

        private final byte id;

        IsolationLevel(byte id) {
            this.id = id;
        }

        public byte id() {
            return this.id;
        }
    }
}
