package com.example2.status.stream.service;

import com.example2.status.stream.model.schema.TransferRequestPayload;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.support.GenericMessage;
import static com.example2.status.stream.config.FileTransferStatusConfig.FILE_TRANSFER_STATUS_CHANNEL;

@MessagingGateway
public interface FileTransferStatus { 

	@Gateway (
		requestChannel =  	FILE_TRANSFER_STATUS_CHANNEL 
	)
	void sendFileTransferStatus(
		GenericMessage<TransferRequestPayload> fileTransferStatus
	) ; 

}