package com.example4.base.consumer;

import com.example4.base.model.schema.DocStatusPayload;
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
	name =  	"monitoring.document.status.kafka.enabled",
	havingValue =  	"true" 
)
public class MonitoringDocumentStatusKafkaConsumer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(MonitoringDocumentStatusKafkaConsumer.class); 

	@KafkaListener (
		topics =  	"${monitoring.document.status.kafka.topic}",
		 groupId = "${monitoring.document.status.kafka.consumer.group.id}" 
	)
	public void fetchDocStatus(
		ConsumerRecord<String, DocStatusPayload> record
	) {
		LOGGER.info("Key: {}, Payload: {}, Timestamp: {}, Partition: {}", record.key(), record.value(), record.timestamp(), record.partition()); 
	} 

}