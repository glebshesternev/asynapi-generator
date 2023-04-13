package com.example3.base.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Ceph params
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CephInfo {
  /**
   * Ceph path
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String bucketName;

  /**
   * Ceph id
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String fileId;

  /**
   * secret key
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String secretKey;

  /**
   * Md5 secret key
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String secretKeyMd5;

  /**
   * encryption algorithm
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String encryptionAlgorithm;

  /**
   * signature identifier
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String signFileId;
}
