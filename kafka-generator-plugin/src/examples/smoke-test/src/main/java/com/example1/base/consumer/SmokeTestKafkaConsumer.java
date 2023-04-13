package com.example1.base.consumer;

import com.example1.base.model.schema.ContractPayload;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty (
	name =  	"smoke.test.kafka.enabled",
	havingValue =  	"true" 
)
public class SmokeTestKafkaConsumer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(SmokeTestKafkaConsumer.class); 

	@KafkaListener (
		topics =  	"${smoke.test.kafka.topic}",
		 groupId = "${smoke.test.kafka.consumer.group.id}" 
	)
	public void getContract(
		ConsumerRecord<String, ContractPayload> record
	) {
		LOGGER.info("Key: {}, Payload: {}, Timestamp: {}, Partition: {}", record.key(), record.value(), record.timestamp(), record.partition()); 
	} 

}