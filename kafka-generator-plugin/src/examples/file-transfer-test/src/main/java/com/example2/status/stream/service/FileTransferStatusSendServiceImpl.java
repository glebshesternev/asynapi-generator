package com.example2.status.stream.service;

import com.example2.status.stream.model.message.TransferRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileTransferStatusSendServiceImpl implements FileTransferStatusSendService { 

	private final FileTransferStatus channelAdapter; 

	@Override
	public void sendFileTransferStatus(
		TransferRequestStatus transferRequestStatus
	) {
		channelAdapter.sendFileTransferStatus(new GenericMessage<>(transferRequestStatus.getPayload())); 
	} 

}