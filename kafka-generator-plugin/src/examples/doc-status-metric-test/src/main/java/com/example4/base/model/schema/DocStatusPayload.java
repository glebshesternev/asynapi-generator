package com.example4.base.model.schema;

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
public class DocStatusPayload {
  @JsonProperty(
      required = true
  )
  @NonNull
  private String emitter;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String id;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String status;

  @JsonProperty(
      required = true
  )
  @NonNull
  private Long timestamp;
}
