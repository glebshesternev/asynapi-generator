package com.example1.stream.service;

import com.example1.stream.model.message.Contract;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmokeTestKafkaSendServiceImpl implements SmokeTestKafkaSendService { 

	private final SmokeTestChannelAdapter channelAdapter; 

	@Override
	public void sendSmokeTest(
		Contract contract
	) {
		channelAdapter.sendSmokeTest(new GenericMessage<>(contract.getPayload())); 
	} 

}