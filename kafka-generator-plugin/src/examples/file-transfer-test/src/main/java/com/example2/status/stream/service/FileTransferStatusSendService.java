package com.example2.status.stream.service;

import com.example2.status.stream.model.message.TransferRequestStatus;

public interface FileTransferStatusSendService { 

	void sendFileTransferStatus(
		TransferRequestStatus transferRequestStatus
	) ; 

}