package com.asyncapi.parser.java.model.component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.jackson.binding.channel.ChannelBindingsDeserializer;
import com.asyncapi.parser.java.jackson.binding.message.MessageBindingsDeserializer;
import com.asyncapi.parser.java.jackson.binding.operation.OperationBindingsDeserializer;
import com.asyncapi.parser.java.jackson.binding.server.ServerBindingsDeserializer;
import com.asyncapi.parser.java.model.channel.ChannelItem;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Components extends ExtendableObject {

    @Nullable
    @JsonDeserialize(using = ComponentsSchemasDeserializer.class)
    private Map<String, Object> schemas;

    @Nullable
    @JsonDeserialize(using = ComponentsServersDeserializer.class)
    private Map<String, Object> servers;

    @Nullable
    @JsonDeserialize(using = ComponentsServerVariablesDeserializer.class)
    private Map<String, Object> serverVariables;

    private Map<String, ChannelItem> channels;

    @Nullable
    @JsonDeserialize(using = ComponentsMessagesDeserializer.class)
    private Map<String, Object> messages;

    @Nullable
    @JsonDeserialize(using = ComponentsParametersDeserializer.class)
    private Map<String, Object> parameters;

    @Nullable
    @JsonDeserialize(using = ComponentsCorrelationIdsDeserializer.class)
    private Map<String, Object> correlationIds;

    @Nullable
    @JsonDeserialize(using = ComponentsOperationTraitsDeserializer.class)
    private Map<String, Object> operationTraits;

    @Nullable
    @JsonDeserialize(using = ComponentsMessageTraitsDeserializer.class)
    private Map<String, Object> messageTraits;

    @Nullable
    @JsonDeserialize(using = ServerBindingsDeserializer.class)
    private Map<String, Object> serverBindings;

    @Nullable
    @JsonDeserialize(using = ChannelBindingsDeserializer.class)
    private Map<String, Object> channelBindings;

    @Nullable
    @JsonDeserialize(using = OperationBindingsDeserializer.class)
    private Map<String, Object> operationBindings;

    @Nullable
    @JsonDeserialize(using = MessageBindingsDeserializer.class)
    private Map<String, Object> messageBindings;

}
