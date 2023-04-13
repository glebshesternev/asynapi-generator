package com.example3.base.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * sign
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Signature {
  /**
   * Product code
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String businessCode;

  @JsonProperty(
      required = true
  )
  @NonNull
  private CephInfo ceph;
}
