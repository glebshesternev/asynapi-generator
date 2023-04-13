package com.example2.create.base.config.props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileTransferCreateProperties { 

	@Value (
		"${file.transfer.create.security.protocol}" 
	)
	private String protocol;
	@Value (
		"${file.transfer.create.consumer.value.deserializer}" 
	)
	private Class valueDeserializer;
	@Value (
		"${file.transfer.create.producer.retry.backoff.ms}" 
	)
	private Integer retryBackoff;
	@Value (
		"${file.transfer.create.producer.key.serializer}" 
	)
	private Class keySerializer;
	@Value (
		"${file.transfer.create.ssl.truststore.password}" 
	)
	private String trustStorePassword;
	@Value (
		"${file.transfer.create.ssl.keystore.password}" 
	)
	private String keyStorePassword;
	@Value (
		"${file.transfer.create.consumer.enable.auto.commit}" 
	)
	private Boolean enableAutoCommit;
	@Value (
		"${file.transfer.create.producer.compression.type}" 
	)
	private String compressionType;
	@Value (
		"${file.transfer.create.enabled}" 
	)
	private Boolean enabled;
	@Value (
		"${file.transfer.create.ssl.keystore.location}" 
	)
	private String keyStoreLocation;
	@Value (
		"${file.transfer.create.consumer.backoff.multiplier}" 
	)
	private Double backoffMultiplier;
	@Value (
		"${file.transfer.create.topic}" 
	)
	private String topic;
	@Value (
		"${file.transfer.create.ssl.truststore.location}" 
	)
	private String trustStoreLocation;
	@Value (
		"${file.transfer.create.producer.retries}" 
	)
	private Integer retries;
	@Value (
		"${file.transfer.create.consumer.auto.offset.reset}" 
	)
	private String autoOffsetReset;
	@Value (
		"${file.transfer.create.consumer.fetch.max.wait.ms}" 
	)
	private Integer fetchMaxWait;
	@Value (
		"${file.transfer.create.consumer.fetch.min.bytes}" 
	)
	private Integer fetchMinSize;
	@Value (
		"${file.transfer.create.producer.acks}" 
	)
	private String acks;
	@Value (
		"${file.transfer.create.producer.value.serializer}" 
	)
	private Class valueSerializer;
	@Value (
		"${file.transfer.create.bootstrap.servers}" 
	)
	private List bootstrapServers;
	@Value (
		"${file.transfer.create.producer.receive.buffer.bytes}" 
	)
	private Integer receiveBuffer;
	@Value (
		"${file.transfer.create.consumer.backoff.initial}" 
	)
	private Integer backoffInitial;
	@Value (
		"${file.transfer.create.producer.send.buffer.bytes}" 
	)
	private Integer sendBuffer;
	@Value (
		"${file.transfer.create.consumer.backoff.max}" 
	)
	private Integer backoffMax;
	@Value (
		"${file.transfer.create.ssl.key.password}" 
	)
	private String keyPassword;
	@Value (
		"${file.transfer.create.consumer.group.id}" 
	)
	private String groupId;
	@Value (
		"${file.transfer.create.producer.max.in.flight.requests.per.connection}" 
	)
	private Integer maxInFlightRequestsPerConnection;
	@Value (
		"${file.transfer.create.consumer.heartbeat.interval.ms}" 
	)
	private Integer heartbeatInterval;
	@Value (
		"${file.transfer.create.consumer.backoff.retries}" 
	)
	private Integer backoffRetries; 

	public Map<String, Object> buildCommonProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("security.protocol", protocol); 
		map.put("bootstrap.servers", bootstrapServers); 
		if ("ssl".equalsIgnoreCase(protocol)) { 
			map.put("ssl.truststore.password", trustStorePassword); 
			map.put("ssl.keystore.password", keyStorePassword); 
			map.put("ssl.keystore.location", keyStoreLocation); 
			map.put("ssl.truststore.location", trustStoreLocation); 
			map.put("ssl.key.password", keyPassword); 
		} 
		return map; 
	}
	
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("value.deserializer", valueDeserializer); 
		map.put("enable.auto.commit", enableAutoCommit); 
		map.put("auto.offset.reset", autoOffsetReset); 
		map.put("fetch.max.wait.ms", fetchMaxWait); 
		map.put("fetch.min.bytes", fetchMinSize); 
		map.put("group.id", groupId); 
		map.put("heartbeat.interval.ms", heartbeatInterval); 
		map.putAll(buildCommonProperties()); 
		return map; 
	}
	
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("retry.backoff.ms", retryBackoff); 
		map.put("key.serializer", keySerializer); 
		map.put("compression.type", compressionType); 
		map.put("retries", retries); 
		map.put("acks", acks); 
		map.put("value.serializer", valueSerializer); 
		map.put("receive.buffer.bytes", receiveBuffer); 
		map.put("send.buffer.bytes", sendBuffer); 
		map.put("max.in.flight.requests.per.connection", maxInFlightRequestsPerConnection); 
		map.putAll(buildCommonProperties()); 
		return map; 
	} 

}