package com.example2.create.stream.model.schema;

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
public class CephInfo {
  @JsonProperty(
      required = true
  )
  @NonNull
  private String bucketName;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String fileId;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String secretKey;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String secretKeyMd5;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String encryptionAlgorithm;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String signFileId;
}
