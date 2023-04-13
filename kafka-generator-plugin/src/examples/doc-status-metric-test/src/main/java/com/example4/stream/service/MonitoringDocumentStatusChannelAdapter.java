package com.example4.stream.service;

import com.example4.stream.model.schema.DocStatusPayload;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.support.GenericMessage;
import static com.example4.stream.config.MonitoringDocumentStatusKafkaConfig.MONITORING_DOCUMENT_STATUS_CHANNEL;

@MessagingGateway
public interface MonitoringDocumentStatusChannelAdapter { 

	@Gateway (
		requestChannel =  	MONITORING_DOCUMENT_STATUS_CHANNEL 
	)
	void sendMonitoringDocumentStatus(
		GenericMessage<DocStatusPayload> monitoringDocumentStatusKafka
	) ; 

}