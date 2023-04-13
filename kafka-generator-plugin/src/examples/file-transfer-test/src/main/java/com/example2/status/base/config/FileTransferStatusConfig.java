package com.example2.status.base.config;

import com.example2.status.base.config.props.FileTransferStatusProperties;
import com.example2.status.base.model.schema.TransferRequestPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty (
	name =  	"file.transfer.status.enabled",
	havingValue =  	"true" 
)
public class FileTransferStatusConfig { 

	private final KafkaProperties defaultKafkaProperties;
	private final FileTransferStatusProperties kafkaProperties; 

	@Bean
	public ConsumerFactory<String, TransferRequestPayload> fileTransferStatusKafkaConsumerFactory() {
		var map = defaultKafkaProperties.buildConsumerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildConsumerProperties()); 
		return new DefaultKafkaConsumerFactory<>(map); 
	}
	
	@Bean
	public ProducerFactory<String, TransferRequestPayload> fileTransferStatusKafkaProducerFactory() {
		var map = defaultKafkaProperties.buildProducerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildProducerProperties()); 
		return new DefaultKafkaProducerFactory<>(map); 
	}
	
	@Bean
	public KafkaTemplate<String, TransferRequestPayload> fileTransferStatusKafkaTemplate(
		ProducerFactory<String, TransferRequestPayload> fileTransferStatusKafkaProducerFactory
	) {
		return new KafkaTemplate<>(fileTransferStatusKafkaProducerFactory); 
	} 

}