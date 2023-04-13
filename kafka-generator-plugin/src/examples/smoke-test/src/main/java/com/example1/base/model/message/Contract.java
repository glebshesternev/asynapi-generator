package com.example1.base.model.message;

import com.example1.base.model.schema.ContractPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contract { 

	private ContractPayload payload; 

}