package com.asyncapi.parser.java.binding.channel.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDescription {

    @Nullable
    private String retention;

    @Nullable
    private String maxMessage;

    @Nullable
    private String partitionCount;

    @Nullable
    private String replicaCount;
}
