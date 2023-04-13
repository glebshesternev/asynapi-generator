package com.example4.stream.model.message;

import com.example4.stream.model.schema.DocStatusPayload;
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