package com.asyncapi.parser.java.model.channel.operation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.jackson.binding.operation.OperationBindingsDeserializer;
import com.asyncapi.parser.java.model.ExternalDocumentation;
import com.asyncapi.parser.java.model.Tag;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OperationTrait extends ExtendableObject {

    @Nullable
    private String operationId;

    @Nullable
    private String summary;

    @Nullable
    private String description;

    @Nullable
    private List<Map<String, List<String>>> security;

    @Nullable
    private List<Tag> tags;

    @Nullable
    private ExternalDocumentation externalDocs;

    @Nullable
    @JsonDeserialize(using = OperationBindingsDeserializer.class)
    private Map<String, Object> bindings;

}
