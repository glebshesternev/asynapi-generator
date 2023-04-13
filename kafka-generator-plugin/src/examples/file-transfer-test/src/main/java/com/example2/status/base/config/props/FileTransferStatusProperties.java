package com.example2.status.base.config.props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileTransferStatusProperties { 

	@Value (
		"${file.transfer.status.security.protocol}" 
	)
	private String protocol;
	@Value (
		"${file.transfer.status.consumer.backoff.max}" 
	)
	private Integer backoffMax;
	@Value (
		"${file.transfer.status.producer.receive.buffer.bytes}" 
	)
	private Integer receiveBuffer;
	@Value (
		"${file.transfer.status.producer.acks}" 
	)
	private String acks;
	@Value (
		"${file.transfer.status.ssl.key.password}" 
	)
	private String keyPassword;
	@Value (
		"${file.transfer.status.consumer.fetch.min.bytes}" 
	)
	private Integer fetchMinSize;
	@Value (
		"${file.transfer.status.producer.retries}" 
	)
	private Integer retries;
	@Value (
		"${file.transfer.status.ssl.truststore.password}" 
	)
	private String trustStorePassword;
	@Value (
		"${file.transfer.status.producer.key.serializer}" 
	)
	private Class keySerializer;
	@Value (
		"${file.transfer.status.bootstrap.servers}" 
	)
	private List bootstrapServers;
	@Value (
		"${file.transfer.status.consumer.group.id}" 
	)
	private String groupId;
	@Value (
		"${file.transfer.status.ssl.keystore.password}" 
	)
	private String keyStorePassword;
	@Value (
		"${file.transfer.status.consumer.backoff.multiplier}" 
	)
	private Double backoffMultiplier;
	@Value (
		"${file.transfer.status.enabled}" 
	)
	private Boolean enabled;
	@Value (
		"${file.transfer.status.topic}" 
	)
	private String topic;
	@Value (
		"${file.transfer.status.consumer.auto.offset.reset}" 
	)
	private String autoOffsetReset;
	@Value (
		"${file.transfer.status.producer.retry.backoff.ms}" 
	)
	private Integer retryBackoff;
	@Value (
		"${file.transfer.status.consumer.fetch.max.wait.ms}" 
	)
	private Integer fetchMaxWait;
	@Value (
		"${file.transfer.status.ssl.truststore.location}" 
	)
	private String trustStoreLocation;
	@Value (
		"${file.transfer.status.producer.max.in.flight.requests.per.connection}" 
	)
	private Integer maxInFlightRequestsPerConnection;
	@Value (
		"${file.transfer.status.consumer.backoff.initial}" 
	)
	private Integer backoffInitial;
	@Value (
		"${file.transfer.status.consumer.enable.auto.commit}" 
	)
	private Boolean enableAutoCommit;
	@Value (
		"${file.transfer.status.producer.compression.type}" 
	)
	private String compressionType;
	@Value (
		"${file.transfer.status.producer.send.buffer.bytes}" 
	)
	private Integer sendBuffer;
	@Value (
		"${file.transfer.status.ssl.keystore.location}" 
	)
	private String keyStoreLocation;
	@Value (
		"${file.transfer.status.consumer.heartbeat.interval.ms}" 
	)
	private Integer heartbeatInterval;
	@Value (
		"${file.transfer.status.consumer.value.deserializer}" 
	)
	private Class valueDeserializer;
	@Value (
		"${file.transfer.status.producer.value.serializer}" 
	)
	private Class valueSerializer; 

	public Map<String, Object> buildCommonProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("security.protocol", protocol); 
		map.put("bootstrap.servers", bootstrapServers); 
		if ("ssl".equalsIgnoreCase(protocol)) { 
			map.put("ssl.key.password", keyPassword); 
			map.put("ssl.truststore.password", trustStorePassword); 
			map.put("ssl.keystore.password", keyStorePassword); 
			map.put("ssl.truststore.location", trustStoreLocation); 
			map.put("ssl.keystore.location", keyStoreLocation); 
		} 
		return map; 
	}
	
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("fetch.min.bytes", fetchMinSize); 
		map.put("group.id", groupId); 
		map.put("auto.offset.reset", autoOffsetReset); 
		map.put("fetch.max.wait.ms", fetchMaxWait); 
		map.put("enable.auto.commit", enableAutoCommit); 
		map.put("heartbeat.interval.ms", heartbeatInterval); 
		map.put("value.deserializer", valueDeserializer); 
		map.putAll(buildCommonProperties()); 
		return map; 
	}
	
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("receive.buffer.bytes", receiveBuffer); 
		map.put("acks", acks); 
		map.put("retries", retries); 
		map.put("key.serializer", keySerializer); 
		map.put("retry.backoff.ms", retryBackoff); 
		map.put("max.in.flight.requests.per.connection", maxInFlightRequestsPerConnection); 
		map.put("compression.type", compressionType); 
		map.put("send.buffer.bytes", sendBuffer); 
		map.put("value.serializer", valueSerializer); 
		map.putAll(buildCommonProperties()); 
		return map; 
	} 

}