package com.example2.create.base.config;

import com.example2.create.base.config.props.FileTransferCreateProperties;
import com.example2.create.base.model.schema.TransferRequestPayload;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
	name =  	"file.transfer.create.enabled",
	havingValue =  	"true" 
)
public class FileTransferCreateConfig { 

	private final KafkaProperties defaultKafkaProperties;
	private final FileTransferCreateProperties kafkaProperties; 

	@Bean
	public ConsumerFactory<String, TransferRequestPayload> fileTransferCreateKafkaConsumerFactory() {
		var map = defaultKafkaProperties.buildConsumerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildConsumerProperties()); 
		return new DefaultKafkaConsumerFactory<>(map); 
	}
	
	@Bean
	public ProducerFactory<String, TransferRequestPayload> fileTransferCreateKafkaProducerFactory() {
		var map = defaultKafkaProperties.buildProducerProperties(); 
		map.putAll(kafkaProperties.buildCommonProperties()); 
		map.putAll(kafkaProperties.buildProducerProperties()); 
		return new DefaultKafkaProducerFactory<>(map); 
	}
	
	@Bean
	public KafkaTemplate<String, TransferRequestPayload> fileTransferCreateKafkaTemplate(
		ProducerFactory<String, TransferRequestPayload> fileTransferCreateKafkaProducerFactory
	) {
		return new KafkaTemplate<>(fileTransferCreateKafkaProducerFactory); 
	}
	
	@Bean
	public ObjectMapper headerMapper() {
		var mapper =  JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES).build(); 
		mapper.registerModule(new JavaTimeModule()); 
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
		return mapper; 
	} 

}