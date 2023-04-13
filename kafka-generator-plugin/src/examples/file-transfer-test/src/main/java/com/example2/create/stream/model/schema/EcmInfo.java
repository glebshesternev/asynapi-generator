package com.example2.create.stream.model.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Информация о файле в ЕСМ
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcmInfo {
  @JsonProperty(
      required = true
  )
  @NonNull
  private String nameEcm;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String targetEcm;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String fileId;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String contentType;

  @JsonProperty(
      required = true
  )
  @NonNull
  private String clazz;
}
