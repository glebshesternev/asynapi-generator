package com.example2.create.base.model.message;

import com.example2.create.base.model.schema.TransferRequestHeaders;
import com.example2.create.base.model.schema.TransferRequestPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestCreate { 

	private TransferRequestHeaders headers;
	private TransferRequestPayload payload; 

}