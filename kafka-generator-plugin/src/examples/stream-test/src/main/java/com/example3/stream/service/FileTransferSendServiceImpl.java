package com.example3.stream.service;

import com.example3.stream.model.message.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileTransferSendServiceImpl implements FileTransferSendService { 

	private final FileTransfer channelAdapter;
	private final ObjectMapper headerMapper; 

	@Override
	public void sendFileTransfer(
		TransferRequest transferRequest
	) {
		Map<String, Object> map = headerMapper.convertValue(transferRequest.getHeaders(), Map.class); 
		channelAdapter.sendFileTransfer(new GenericMessage<>(transferRequest.getPayload(), map)); 
	} 

}