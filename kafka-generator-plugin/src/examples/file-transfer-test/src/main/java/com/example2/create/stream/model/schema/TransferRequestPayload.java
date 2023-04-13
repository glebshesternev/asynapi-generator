package com.example2.create.stream.model.schema;

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
public class TransferRequestPayload {
  @JsonProperty(
      required = true
  )
  @NonNull
  private String uuid;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String initiator;

  @JsonProperty(
      required = true
  )
  @NonNull
  private Boolean isDeleteCephFileAfterTransfer;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String state;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String docTypeDictionary;

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

  @JsonProperty(
      required = true
  )
  @NonNull
  private String objectStoreId;

  @JsonProperty(
      required = true
  )
  @NonNull
  private Boolean isKeepEncryptedSignFilesInCeph;
}
