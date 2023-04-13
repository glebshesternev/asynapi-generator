package com.example1.base.config.props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SmokeTestKafkaProperties { 

	@Value (
		"${smoke.test.kafka.consumer.fetch.min.bytes}" 
	)
	private Integer fetchMinSize;
	@Value (
		"${smoke.test.kafka.producer.client.id}" 
	)
	private String clientId;
	@Value (
		"${smoke.test.kafka.consumer.heartbeat.interval.ms}" 
	)
	private Integer heartbeatInterval;
	@Value (
		"${smoke.test.kafka.bootstrap.servers}" 
	)
	private List bootstrapServers;
	@Value (
		"${smoke.test.kafka.consumer.group.id}" 
	)
	private String groupId;
	@Value (
		"${smoke.test.kafka.security.protocol}" 
	)
	private String protocol;
	@Value (
		"${smoke.test.kafka.consumer.value.deserializer}" 
	)
	private Class valueDeserializer;
	@Value (
		"${smoke.test.kafka.consumer.backoff.initial}" 
	)
	private Integer backoffInitial;
	@Value (
		"${smoke.test.kafka.consumer.backoff.multiplier}" 
	)
	private Double backoffMultiplier;
	@Value (
		"${smoke.test.kafka.enabled}" 
	)
	private Boolean enabled;
	@Value (
		"${smoke.test.kafka.consumer.enable.auto.commit}" 
	)
	private Boolean enableAutoCommit;
	@Value (
		"${smoke.test.kafka.consumer.auto.offset.reset}" 
	)
	private String autoOffsetReset;
	@Value (
		"${smoke.test.kafka.consumer.fetch.max.wait.ms}" 
	)
	private Integer fetchMaxWait;
	@Value (
		"${smoke.test.kafka.topic}" 
	)
	private String topic;
	@Value (
		"${smoke.test.kafka.consumer.backoff.max}" 
	)
	private Integer backoffMax;
	@Value (
		"${smoke.test.kafka.producer.key.serializer}" 
	)
	private Class keySerializer;
	@Value (
		"${smoke.test.kafka.producer.acks}" 
	)
	private String acks;
	@Value (
		"${smoke.test.kafka.producer.batch.size}" 
	)
	private Integer batchSize;
	@Value (
		"${smoke.test.kafka.producer.value.serializer}" 
	)
	private Class valueSerializer; 

	public Map<String, Object> buildCommonProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("bootstrap.servers", bootstrapServers); 
		map.put("security.protocol", protocol); 
		return map; 
	}
	
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("fetch.min.bytes", fetchMinSize); 
		map.put("heartbeat.interval.ms", heartbeatInterval); 
		map.put("group.id", groupId); 
		map.put("value.deserializer", valueDeserializer); 
		map.put("enable.auto.commit", enableAutoCommit); 
		map.put("auto.offset.reset", autoOffsetReset); 
		map.put("fetch.max.wait.ms", fetchMaxWait); 
		map.putAll(buildCommonProperties()); 
		return map; 
	}
	
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("client.id", clientId); 
		map.put("key.serializer", keySerializer); 
		map.put("acks", acks); 
		map.put("batch.size", batchSize); 
		map.put("value.serializer", valueSerializer); 
		map.putAll(buildCommonProperties()); 
		return map; 
	} 

}