package com.example3.base.model.message;

import com.example3.base.model.schema.TransferRequestHeaders;
import com.example3.base.model.schema.TransferRequestPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest { 

	private TransferRequestHeaders headers;
	private TransferRequestPayload payload; 

}