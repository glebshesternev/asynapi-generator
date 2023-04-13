package com.asyncapi.parser.java.model.server;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.jackson.binding.server.ServerBindingsDeserializer;
import com.asyncapi.parser.java.model.Tag;

import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Server extends ExtendableObject {

    @NotNull
    private String url;

    @NotNull
    private String protocol;

    @Nullable
    private String protocolVersion;

    @Nullable
    private String description;

    @Nullable
    @JsonDeserialize(using = ServerVariablesDeserializer.class)
    private Map<String, Object> variables;

    @Nullable
    private List<Map<String, List<String>>> security;

    @Nullable
    private List<Tag> tags;

    @Nullable
    @JsonDeserialize(using = ServerBindingsDeserializer.class)
    private Map<String, Object> bindings;

}
