package com.example1.base.config;

import com.example1.base.config.props.SmokeTestKafkaProperties;
import com.example1.base.model.schema.ContractPayload;
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
	name =  	"smoke.test.kafka.enabled",
	havingValue =  	"true" 
)
public class SmokeTestKafkaConfig { 

	private final KafkaProperties defaultKafkaProperties;
	private final SmokeTestKafkaProperties kafkaProperties; 

	@Bean
	public ConsumerFactory<String, ContractPayload> smokeTestKafkaKafkaConsumerFactory() {
		var map = defaultKafkaProperties.buildConsumerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildConsumerProperties()); 
		return new DefaultKafkaConsumerFactory<>(map); 
	}
	
	@Bean
	public ProducerFactory<String, ContractPayload> smokeTestKafkaKafkaProducerFactory() {
		var map = defaultKafkaProperties.buildProducerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildProducerProperties()); 
		return new DefaultKafkaProducerFactory<>(map); 
	}
	
	@Bean
	public KafkaTemplate<String, ContractPayload> smokeTestKafkaKafkaTemplate(
		ProducerFactory<String, ContractPayload> smokeTestKafkaKafkaProducerFactory
	) {
		return new KafkaTemplate<>(smokeTestKafkaKafkaProducerFactory); 
	} 

}