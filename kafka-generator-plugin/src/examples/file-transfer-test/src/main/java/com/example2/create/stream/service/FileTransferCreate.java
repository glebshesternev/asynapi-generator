package com.example2.create.stream.service;

import com.example2.create.stream.model.schema.TransferRequestPayload;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.support.GenericMessage;
import static com.example2.create.stream.config.FileTransferCreateConfig.FILE_TRANSFER_CREATE_CHANNEL;

@MessagingGateway
public interface FileTransferCreate { 

	@Gateway (
		requestChannel =  	FILE_TRANSFER_CREATE_CHANNEL 
	)
	void sendFileTransferCreate(
		GenericMessage<TransferRequestPayload> fileTransferCreate
	) ; 

}