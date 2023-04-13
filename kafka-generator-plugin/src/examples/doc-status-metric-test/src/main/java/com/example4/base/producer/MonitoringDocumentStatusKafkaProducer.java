package com.example4.base.producer;

import com.example4.base.config.props.MonitoringDocumentStatusKafkaProperties;
import com.example4.base.model.message.DocStatusMessage;
import com.example4.base.model.schema.DocStatusPayload;
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
	name =  	"monitoring.document.status.kafka.enabled",
	havingValue =  	"true" 
)
@RequiredArgsConstructor
public class MonitoringDocumentStatusKafkaProducer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(MonitoringDocumentStatusKafkaProducer.class);
	private final KafkaTemplate<String, DocStatusPayload> kafkaTemplate;
	private final MonitoringDocumentStatusKafkaProperties kafkaProperties; 

	public void sendDocStatus(
		DocStatusMessage docStatusMessage
	) {
			var key = UUID.randomUUID().toString();
			var record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, docStatusMessage.getPayload(), null);
			kafkaTemplate.send(record)
					.addCallback(result -> LOGGER.info("Сообщение {} отправлено в топик {} и получение подтверждено брокером",
									record.key(),
										record.topic()
							),
							e -> LOGGER.error("Ошибка отправки {} в {}", record.key(), kafkaProperties.getTopic(), e)
					); 
	} 

}