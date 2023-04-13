package com.example3.base.consumer;

import com.example3.base.model.schema.TransferRequestPayload;
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
	name =  	"file.transfer.enabled",
	havingValue =  	"true" 
)
public class FileTransferConsumer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferConsumer.class); 

	@KafkaListener (
		topics =  	"${file.transfer.topic}",
		 groupId = "${file.transfer.consumer.group.id}" 
	)
	public void reciveFileTransferRequest(
		ConsumerRecord<String, TransferRequestPayload> record
	) {
		LOGGER.info("Key: {}, Payload: {}, Timestamp: {}, Partition: {}", record.key(), record.value(), record.timestamp(), record.partition()); 
	} 

}