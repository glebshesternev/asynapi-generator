package com.example3.stream.service;

import com.example3.stream.model.schema.TransferRequestPayload;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.support.GenericMessage;
import static com.example3.stream.config.FileTransferConfig.FILE_TRANSFER_CHANNEL;

@MessagingGateway
public interface FileTransfer { 

	@Gateway (
		requestChannel =  	FILE_TRANSFER_CHANNEL 
	)
	void sendFileTransfer(
		GenericMessage<TransferRequestPayload> fileTransfer
	) ; 

}