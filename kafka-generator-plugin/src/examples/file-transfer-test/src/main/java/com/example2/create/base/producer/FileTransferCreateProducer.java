package com.example2.create.base.producer;

import com.example2.create.base.config.props.FileTransferCreateProperties;
import com.example2.create.base.model.message.TransferRequestCreate;
import com.example2.create.base.model.schema.TransferRequestPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty (
	name =  	"file.transfer.create.enabled",
	havingValue =  	"true" 
)
public class FileTransferCreateProducer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferCreateProducer.class);
	private final KafkaTemplate<String, TransferRequestPayload> kafkaTemplate;
	private final FileTransferCreateProperties kafkaProperties;
	private final ObjectMapper headerMapper; 

	public FileTransferCreateProducer(
		KafkaTemplate<String, TransferRequestPayload> kafkaTemplate,
		FileTransferCreateProperties kafkaProperties,
		@Qualifier (
			value =  	"headerMapper" 
		)
		ObjectMapper headerMapper
	) {
		this.kafkaTemplate = kafkaTemplate; 
		this.kafkaProperties = kafkaProperties; 
		this.headerMapper = headerMapper; 
	} 

	public void sendFileTransferRequest(
		TransferRequestCreate transferRequestCreate
	) {
			Map<String, Object> map = headerMapper.convertValue(transferRequestCreate.getHeaders(), Map.class);
			var headers = new RecordHeaders();
			map.forEach((key, value) -> headers.add(key, objectToByte(value)));
			var key = UUID.randomUUID().toString();
			var record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, transferRequestCreate.getPayload(), headers);
			kafkaTemplate.send(record)
					.addCallback(result -> LOGGER.info("Сообщение {} отправлено в топик {} и получение подтверждено брокером",
									record.key(),
										record.topic()
							),
							e -> LOGGER.error("Ошибка отправки {} в {}", record.key(), kafkaProperties.getTopic(), e)
					); 
	}
	
	private byte[] objectToByte(
		Object object
	) {
			try {
				return headerMapper.writeValueAsBytes(object);
			} catch (JsonProcessingException e) {
				throw new SerializationException("Ошибка сериализации заголовков", e);
			} 
	} 

}