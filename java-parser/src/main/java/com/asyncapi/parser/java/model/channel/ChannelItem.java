package com.asyncapi.parser.java.model.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.jackson.binding.channel.ChannelBindingsDeserializer;
import com.asyncapi.parser.java.model.channel.operation.Operation;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChannelItem extends ExtendableObject {

    @Nullable
    @JsonProperty("$ref")
    private String ref;

    @Nullable
    private String description;

    @Nullable
    private List<String> servers;

    @Nullable
    private Operation subscribe;

    @Nullable
    private Operation publish;

    @Nullable
    @JsonDeserialize(using = ChannelParametersDeserializer.class)
    private Map<String, Object> parameters;

    @Nullable
    @JsonDeserialize(using = ChannelBindingsDeserializer.class)
    private Map<String, Object> bindings;

}
