package com.example3.base.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * transfer request
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestPayload {
  /**
   * uuid
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String uuid;

  /**
   * initiator
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String initiator;

  /**
   * is delete Ceph file after transfer flag
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private Boolean isDeleteCephFileAfterTransfer;

  /**
   * state of transfer
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String state;

  /**
   * document type
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String docTypeDictionary;

  /**
   * direction
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String direction;

  @JsonProperty(
      required = true
  )
  @NonNull
  private List<FileTransferInfo> files;

  /**
   * backet id
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private String objectStoreId;

  /**
   * is keep encrypted sign files in Ceph flag
   */
  @JsonProperty(
      required = true
  )
  @NonNull
  private Boolean isKeepEncryptedSignFilesInCeph;
}
