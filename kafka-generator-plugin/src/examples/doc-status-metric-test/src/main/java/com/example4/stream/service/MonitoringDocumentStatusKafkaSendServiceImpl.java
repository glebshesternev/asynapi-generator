package com.example4.stream.service;

import com.example4.stream.model.message.DocStatusMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonitoringDocumentStatusKafkaSendServiceImpl implements MonitoringDocumentStatusKafkaSendService { 

	private final MonitoringDocumentStatusChannelAdapter channelAdapter; 

	@Override
	public void sendMonitoringDocumentStatus(
		DocStatusMessage docStatusMessage
	) {
		channelAdapter.sendMonitoringDocumentStatus(new GenericMessage<>(docStatusMessage.getPayload())); 
	} 

}