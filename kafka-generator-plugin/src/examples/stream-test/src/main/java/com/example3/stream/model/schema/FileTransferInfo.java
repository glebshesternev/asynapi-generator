package com.example3.stream.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * File info
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileTransferInfo {
  @JsonProperty(
      required = true
  )
  @NonNull
  private EcmInfo ecm;

  @JsonProperty(
      required = true
  )
  @NonNull
  private CephInfo ceph;

  @JsonProperty(
      required = true
  )
  @NonNull
  private List<Signature> signatures;
}
