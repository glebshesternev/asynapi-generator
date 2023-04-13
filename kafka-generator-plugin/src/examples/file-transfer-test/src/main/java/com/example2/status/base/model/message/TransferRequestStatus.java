package com.example2.status.base.model.message;

import com.example2.status.base.model.schema.TransferRequestPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestStatus { 

	private TransferRequestPayload payload; 

}