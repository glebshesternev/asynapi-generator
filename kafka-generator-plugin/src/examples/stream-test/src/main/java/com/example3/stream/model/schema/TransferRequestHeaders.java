package com.example3.stream.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestHeaders {
  @JsonProperty(
      required = true
  )
  @NonNull
  private String digitalId;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String userUuid;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String clientModule;
}
