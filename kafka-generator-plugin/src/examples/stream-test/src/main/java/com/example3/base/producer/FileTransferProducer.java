package com.example3.base.producer;

import com.example3.base.config.props.FileTransferProperties;
import com.example3.base.model.message.TransferRequest;
import com.example3.base.model.schema.TransferRequestPayload;
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
	name =  	"file.transfer.enabled",
	havingValue =  	"true" 
)
public class FileTransferProducer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(FileTransferProducer.class);
	private final KafkaTemplate<String, TransferRequestPayload> kafkaTemplate;
	private final FileTransferProperties kafkaProperties;
	private final ObjectMapper headerMapper; 

	public FileTransferProducer(
		KafkaTemplate<String, TransferRequestPayload> kafkaTemplate,
		FileTransferProperties kafkaProperties,
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
		TransferRequest transferRequest
	) {
			Map<String, Object> map = headerMapper.convertValue(transferRequest.getHeaders(), Map.class);
			var headers = new RecordHeaders();
			map.forEach((key, value) -> headers.add(key, objectToByte(value)));
			var key = UUID.randomUUID().toString();
			var record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, transferRequest.getPayload(), headers);
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