package com.example2.status.base.producer;

import com.example2.status.base.config.props.FileTransferStatusProperties;
import com.example2.status.base.model.message.TransferRequestStatus;
import com.example2.status.base.model.schema.TransferRequestPayload;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty (
	name =  	"file.transfer.status.enabled",
	havingValue =  	"true" 
)
@RequiredArgsConstructor
public class FileTransferStatusProducer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferStatusProducer.class);
	private final KafkaTemplate<String, TransferRequestPayload> kafkaTemplate;
	private final FileTransferStatusProperties kafkaProperties; 

	public void sendFileTransferStatus(
		TransferRequestStatus transferRequestStatus
	) {
			var key = UUID.randomUUID().toString();
			var record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, transferRequestStatus.getPayload(), null);
			kafkaTemplate.send(record)
					.addCallback(result -> LOGGER.info("Сообщение {} отправлено в топик {} и получение подтверждено брокером",
									record.key(),
										record.topic()
							),
							e -> LOGGER.error("Ошибка отправки {} в {}", record.key(), kafkaProperties.getTopic(), e)
					); 
	} 

}