package com.example4.stream.config.props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class MonitoringDocumentStatusKafkaProperties { 

	@Value (
		"${monitoring.document.status.kafka.security.protocol}" 
	)
	private String protocol;
	@Value (
		"${monitoring.document.status.kafka.ssl.truststore.location}" 
	)
	private String trustStoreLocation;
	@Value (
		"${monitoring.document.status.kafka.producer.compression.type}" 
	)
	private String compressionType;
	@Value (
		"${monitoring.document.status.kafka.producer.max.in.flight.requests.per.connection}" 
	)
	private Integer maxInFlightRequestsPerConnection;
	@Value (
		"${monitoring.document.status.kafka.ssl.keystore.password}" 
	)
	private String keyStorePassword;
	@Value (
		"${monitoring.document.status.kafka.enabled}" 
	)
	private Boolean enabled;
	@Value (
		"${monitoring.document.status.kafka.producer.value.serializer}" 
	)
	private Class valueSerializer;
	@Value (
		"${monitoring.document.status.kafka.consumer.fetch.max.wait.ms}" 
	)
	private Integer fetchMaxWait;
	@Value (
		"${monitoring.document.status.kafka.ssl.truststore.password}" 
	)
	private String trustStorePassword;
	@Value (
		"${monitoring.document.status.kafka.consumer.backoff.multiplier}" 
	)
	private Double backoffMultiplier;
	@Value (
		"${monitoring.document.status.kafka.producer.receive.buffer.bytes}" 
	)
	private Integer receiveBuffer;
	@Value (
		"${monitoring.document.status.kafka.topic}" 
	)
	private String topic;
	@Value (
		"${monitoring.document.status.kafka.producer.acks}" 
	)
	private String acks;
	@Value (
		"${monitoring.document.status.kafka.producer.key.serializer}" 
	)
	private Class keySerializer;
	@Value (
		"${monitoring.document.status.kafka.producer.retries}" 
	)
	private Integer retries;
	@Value (
		"${monitoring.document.status.kafka.ssl.keystore.location}" 
	)
	private String keyStoreLocation;
	@Value (
		"${monitoring.document.status.kafka.producer.send.buffer.bytes}" 
	)
	private Integer sendBuffer;
	@Value (
		"${monitoring.document.status.kafka.consumer.fetch.min.bytes}" 
	)
	private Integer fetchMinSize;
	@Value (
		"${monitoring.document.status.kafka.consumer.backoff.max}" 
	)
	private Integer backoffMax;
	@Value (
		"${monitoring.document.status.kafka.consumer.heartbeat.interval.ms}" 
	)
	private Integer heartbeatInterval;
	@Value (
		"${monitoring.document.status.kafka.bootstrap.servers}" 
	)
	private List bootstrapServers;
	@Value (
		"${monitoring.document.status.kafka.consumer.group.id}" 
	)
	private String groupId;
	@Value (
		"${monitoring.document.status.kafka.consumer.enable.auto.commit}" 
	)
	private Boolean enableAutoCommit;
	@Value (
		"${monitoring.document.status.kafka.consumer.auto.offset.reset}" 
	)
	private String autoOffsetReset;
	@Value (
		"${monitoring.document.status.kafka.consumer.backoff.initial}" 
	)
	private Integer backoffInitial;
	@Value (
		"${monitoring.document.status.kafka.ssl.key.password}" 
	)
	private String keyPassword;
	@Value (
		"${monitoring.document.status.kafka.consumer.value.deserializer}" 
	)
	private Class valueDeserializer;
	@Value (
		"${monitoring.document.status.kafka.producer.retry.backoff.ms}" 
	)
	private Integer retryBackoff; 

	public Map<String, Object> buildCommonProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("security.protocol", protocol); 
		map.put("bootstrap.servers", bootstrapServers); 
		if ("ssl".equalsIgnoreCase(protocol)) { 
			map.put("ssl.truststore.location", trustStoreLocation); 
			map.put("ssl.keystore.password", keyStorePassword); 
			map.put("ssl.truststore.password", trustStorePassword); 
			map.put("ssl.keystore.location", keyStoreLocation); 
			map.put("ssl.key.password", keyPassword); 
		} 
		return map; 
	}
	
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("fetch.max.wait.ms", fetchMaxWait); 
		map.put("fetch.min.bytes", fetchMinSize); 
		map.put("heartbeat.interval.ms", heartbeatInterval); 
		map.put("group.id", groupId); 
		map.put("enable.auto.commit", enableAutoCommit); 
		map.put("auto.offset.reset", autoOffsetReset); 
		map.put("value.deserializer", valueDeserializer); 
		map.putAll(buildCommonProperties()); 
		return map; 
	}
	
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("compression.type", compressionType); 
		map.put("max.in.flight.requests.per.connection", maxInFlightRequestsPerConnection); 
		map.put("value.serializer", valueSerializer); 
		map.put("receive.buffer.bytes", receiveBuffer); 
		map.put("acks", acks); 
		map.put("key.serializer", keySerializer); 
		map.put("retries", retries); 
		map.put("send.buffer.bytes", sendBuffer); 
		map.put("retry.backoff.ms", retryBackoff); 
		map.putAll(buildCommonProperties()); 
		return map; 
	} 

}