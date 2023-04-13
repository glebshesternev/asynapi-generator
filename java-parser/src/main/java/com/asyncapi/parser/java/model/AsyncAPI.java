package com.asyncapi.parser.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.model.channel.ChannelItem;
import com.asyncapi.parser.java.model.component.Components;
import com.asyncapi.parser.java.model.info.Info;
import com.asyncapi.parser.java.model.server.Server;

import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AsyncAPI extends ExtendableObject {

    @NotNull
    private String asyncapi = "2.6.0";

    @Nullable
    private String id;

    @NotNull
    private Info info;

    //TODO: references
    @Nullable
    private Map<String, Server> servers;

    @Nullable
    private String defaultContentType;

    @NotNull
    private Map<String, ChannelItem> channels;

    @Nullable
    private Components components;

    @Nullable
    private List<Tag> tags;

    @Nullable
    private ExternalDocumentation externalDocs;

}
