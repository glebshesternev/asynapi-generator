package com.example1.base.producer;

import com.example1.base.config.props.SmokeTestKafkaProperties;
import com.example1.base.model.message.Contract;
import com.example1.base.model.schema.ContractPayload;
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
	name =  	"smoke.test.kafka.enabled",
	havingValue =  	"true" 
)
@RequiredArgsConstructor
public class SmokeTestKafkaProducer { 

	public static final Logger LOGGER = LoggerFactory.getLogger(SmokeTestKafkaProducer.class);
	private final KafkaTemplate<String, ContractPayload> kafkaTemplate;
	private final SmokeTestKafkaProperties kafkaProperties; 

	public void sendContract(
		Contract contract
	) {
			var key = UUID.randomUUID().toString();
			var record = new ProducerRecord<>(kafkaProperties.getTopic(), null, key, contract.getPayload(), null);
			kafkaTemplate.send(record)
					.addCallback(result -> LOGGER.info("Сообщение {} отправлено в топик {} и получение подтверждено брокером",
									record.key(),
										record.topic()
							),
							e -> LOGGER.error("Ошибка отправки {} в {}", record.key(), kafkaProperties.getTopic(), e)
					); 
	} 

}