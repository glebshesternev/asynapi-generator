package com.example2.create.stream.service;

import com.example2.create.stream.model.message.TransferRequestCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileTransferCreateSendServiceImpl implements FileTransferCreateSendService { 

	private final FileTransferCreate channelAdapter;
	private final ObjectMapper headerMapper; 

	@Override
	public void sendFileTransferCreate(
		TransferRequestCreate transferRequestCreate
	) {
		Map<String, Object> map = headerMapper.convertValue(transferRequestCreate.getHeaders(), Map.class); 
		channelAdapter.sendFileTransferCreate(new GenericMessage<>(transferRequestCreate.getPayload(), map)); 
	} 

}