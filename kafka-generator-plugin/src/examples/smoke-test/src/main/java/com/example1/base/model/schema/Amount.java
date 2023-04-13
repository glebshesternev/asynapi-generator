package com.example1.base.model.schema;

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
public class Amount {
  @JsonProperty(
      required = true
  )
  @NonNull
  private Double sum;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String currency;
}
