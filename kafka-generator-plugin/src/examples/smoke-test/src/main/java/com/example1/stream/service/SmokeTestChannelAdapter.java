package com.example1.stream.service;

import com.example1.stream.model.schema.ContractPayload;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.support.GenericMessage;
import static com.example1.stream.config.SmokeTestKafkaConfig.SMOKE_TEST_CHANNEL;

@MessagingGateway
public interface SmokeTestChannelAdapter { 

	@Gateway (
		requestChannel =  	SMOKE_TEST_CHANNEL 
	)
	void sendSmokeTest(
		GenericMessage<ContractPayload> smokeTestKafka
	) ; 

}