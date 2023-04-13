package com.example3.stream.service;

import com.example3.stream.model.message.TransferRequest;

public interface FileTransferSendService { 

	void sendFileTransfer(
		TransferRequest transferRequest
	) ; 

}