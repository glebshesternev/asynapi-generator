package com.example3.base.config.props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FileTransferProperties { 

	@Value (
		"${file.transfer.consumer.auto.offset.reset}" 
	)
	private String autoOffsetReset;
	@Value (
		"${file.transfer.enabled}" 
	)
	private Boolean enabled;
	@Value (
		"${file.transfer.consumer.value.deserializer}" 
	)
	private Class valueDeserializer;
	@Value (
		"${file.transfer.topic}" 
	)
	private String topic;
	@Value (
		"${file.transfer.consumer.backoff.initial}" 
	)
	private Integer backoffInitial;
	@Value (
		"${file.transfer.security.protocol}" 
	)
	private String protocol;
	@Value (
		"${file.transfer.producer.acks}" 
	)
	private String acks;
	@Value (
		"${file.transfer.producer.batch.size}" 
	)
	private Integer batchSize;
	@Value (
		"${file.transfer.consumer.fetch.max.wait.ms}" 
	)
	private Integer fetchMaxWait;
	@Value (
		"${file.transfer.consumer.enable.auto.commit}" 
	)
	private Boolean enableAutoCommit;
	@Value (
		"${file.transfer.consumer.heartbeat.interval.ms}" 
	)
	private Integer heartbeatInterval;
	@Value (
		"${file.transfer.consumer.fetch.min.bytes}" 
	)
	private Integer fetchMinSize;
	@Value (
		"${file.transfer.consumer.backoff.max}" 
	)
	private Integer backoffMax;
	@Value (
		"${file.transfer.consumer.backoff.multiplier}" 
	)
	private Double backoffMultiplier;
	@Value (
		"${file.transfer.producer.key.serializer}" 
	)
	private Class keySerializer;
	@Value (
		"${file.transfer.producer.value.serializer}" 
	)
	private Class valueSerializer;
	@Value (
		"${file.transfer.bootstrap.servers}" 
	)
	private List bootstrapServers;
	@Value (
		"${file.transfer.consumer.group.id}" 
	)
	private String groupId; 

	public Map<String, Object> buildCommonProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("security.protocol", protocol); 
		map.put("bootstrap.servers", bootstrapServers); 
		return map; 
	}
	
	public Map<String, Object> buildConsumerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("auto.offset.reset", autoOffsetReset); 
		map.put("value.deserializer", valueDeserializer); 
		map.put("fetch.max.wait.ms", fetchMaxWait); 
		map.put("enable.auto.commit", enableAutoCommit); 
		map.put("heartbeat.interval.ms", heartbeatInterval); 
		map.put("fetch.min.bytes", fetchMinSize); 
		map.put("group.id", groupId); 
		map.putAll(buildCommonProperties()); 
		return map; 
	}
	
	public Map<String, Object> buildProducerProperties() {
		Map<String, Object> map = new HashMap<>(); 
		map.put("acks", acks); 
		map.put("batch.size", batchSize); 
		map.put("key.serializer", keySerializer); 
		map.put("value.serializer", valueSerializer); 
		map.putAll(buildCommonProperties()); 
		return map; 
	} 

}