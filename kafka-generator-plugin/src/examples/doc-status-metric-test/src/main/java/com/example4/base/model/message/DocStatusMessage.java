package com.example4.base.model.message;

import com.example4.base.model.schema.DocStatusPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocStatusMessage { 

	private DocStatusPayload payload; 

}