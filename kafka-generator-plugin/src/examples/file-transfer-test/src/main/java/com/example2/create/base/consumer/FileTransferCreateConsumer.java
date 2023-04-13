package com.example2.create.base.consumer;

import com.example2.create.base.model.schema.TransferRequestPayload;
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
	name =  	"file.transfer.create.enabled",
	havingValue =  	"true" 
)
public class FileTransferCreateConsumer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferCreateConsumer.class); 

	@KafkaListener (
		topics =  	"${file.transfer.create.topic}",
		 groupId = "${file.transfer.create.consumer.group.id}" 
	)
	public void reciveFileTransferRequest(
		ConsumerRecord<String, TransferRequestPayload> record
	) {
		LOGGER.info("Key: {}, Payload: {}, Timestamp: {}, Partition: {}", record.key(), record.value(), record.timestamp(), record.partition()); 
	} 

}