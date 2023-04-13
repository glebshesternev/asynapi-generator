package com.example2.status.base.consumer;

import com.example2.status.base.model.schema.TransferRequestPayload;
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
	name =  	"file.transfer.status.enabled",
	havingValue =  	"true" 
)
public class FileTransferStatusConsumer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferStatusConsumer.class); 

	@KafkaListener (
		topics =  	"${file.transfer.status.topic}",
		 groupId = "${file.transfer.status.consumer.group.id}" 
	)
	public void reciveFileTransferStatus(
		ConsumerRecord<String, TransferRequestPayload> record
	) {
		LOGGER.info("Key: {}, Payload: {}, Timestamp: {}, Partition: {}", record.key(), record.value(), record.timestamp(), record.partition()); 
	} 

}