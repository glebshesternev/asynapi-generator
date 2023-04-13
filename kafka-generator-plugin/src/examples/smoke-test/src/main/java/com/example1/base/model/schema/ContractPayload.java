package com.example1.base.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractPayload {
  @JsonProperty(
      required = true
  )
  @NonNull
  private List<Long> id;

  @JsonProperty(
      required = true
  )
  @NonNull
  private List<Amount> amounts;

  @JsonProperty(
      required = true
  )
  @NonNull
  private Long amount;
}
