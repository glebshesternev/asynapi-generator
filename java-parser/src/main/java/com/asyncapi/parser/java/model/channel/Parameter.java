package com.asyncapi.parser.java.model.channel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.model.schema.SchemaDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Parameter extends ExtendableObject {

    @Nullable
    private String description;

    @Nullable
    @JsonDeserialize(using = SchemaDeserializer.class)
    private Object schema;

    @Nullable
    private String location;

}
