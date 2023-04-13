package com.example4.stream.service;

import com.example4.stream.model.message.DocStatusMessage;

public interface MonitoringDocumentStatusKafkaSendService { 

	void sendMonitoringDocumentStatus(
		DocStatusMessage docStatusMessage
	) ; 

}