package com.example2.create.stream.service;

import com.example2.create.stream.model.message.TransferRequestCreate;

public interface FileTransferCreateSendService { 

	void sendFileTransferCreate(
		TransferRequestCreate transferRequestCreate
	) ; 

}