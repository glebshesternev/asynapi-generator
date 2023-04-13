package com.example4.base.config;

import com.example4.base.config.props.MonitoringDocumentStatusKafkaProperties;
import com.example4.base.model.schema.DocStatusPayload;
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
	name =  	"monitoring.document.status.kafka.enabled",
	havingValue =  	"true" 
)
public class MonitoringDocumentStatusKafkaConfig { 

	private final KafkaProperties defaultKafkaProperties;
	private final MonitoringDocumentStatusKafkaProperties kafkaProperties; 

	@Bean
	public ConsumerFactory<String, DocStatusPayload> monitoringDocumentStatusKafkaKafkaConsumerFactory() {
		var map = defaultKafkaProperties.buildConsumerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildConsumerProperties()); 
		return new DefaultKafkaConsumerFactory<>(map); 
	}
	
	@Bean
	public ProducerFactory<String, DocStatusPayload> monitoringDocumentStatusKafkaKafkaProducerFactory() {
		var map = defaultKafkaProperties.buildProducerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildProducerProperties()); 
		return new DefaultKafkaProducerFactory<>(map); 
	}
	
	@Bean
	public KafkaTemplate<String, DocStatusPayload> monitoringDocumentStatusKafkaKafkaTemplate(
		ProducerFactory<String, DocStatusPayload> monitoringDocumentStatusKafkaKafkaProducerFactory
	) {
		return new KafkaTemplate<>(monitoringDocumentStatusKafkaKafkaProducerFactory); 
	} 

}