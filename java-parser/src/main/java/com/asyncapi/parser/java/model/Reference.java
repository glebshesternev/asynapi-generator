package com.asyncapi.parser.java.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Reference {

    @NotNull
    @JsonProperty(value = "$ref")
    private String ref;

}
