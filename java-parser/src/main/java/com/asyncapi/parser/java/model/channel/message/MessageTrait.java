package com.asyncapi.parser.java.model.channel.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.jackson.binding.message.MessageBindingsDeserializer;
import com.asyncapi.parser.java.model.ExternalDocumentation;
import com.asyncapi.parser.java.model.Tag;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageTrait extends ExtendableObject {

    @Nullable
    private String messageId;

    @Nullable
    @JsonDeserialize(using = MessageHeadersDeserializer.class)
    private Object headers;

    @Nullable
    @JsonDeserialize(using = MessageCorrelationIdDeserializer.class)
    private Object correlationId;

    @Nullable
    private String schemaFormat;

    @Nullable
    private String contentType;

    @Nullable
    private String name;

    @Nullable
    private String title;

    @Nullable
    private String summary;

    @Nullable
    private String description;

    @Nullable
    private List<Tag> tags;

    @Nullable
    private ExternalDocumentation externalDocs;

    @Nullable
    @JsonDeserialize(using = MessageBindingsDeserializer.class)
    private Map<String, Object> bindings;

    @Nullable
    private List<MessageExample> examples;

}
