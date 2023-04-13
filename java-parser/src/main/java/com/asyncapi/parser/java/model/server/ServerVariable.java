package com.asyncapi.parser.java.model.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerVariable extends ExtendableObject {

    @Nullable
    @JsonProperty(value = "enum")
    private List<String> enumValues;

    @Nullable
    @JsonProperty("default")
    private String defaultValue;

    @Nullable
    private String description;

    @Nullable
    private List<String> examples;

}
