package com.asyncapi.parser.java.binding.channel.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.binding.channel.ChannelBinding;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KafkaChannelBinding extends ChannelBinding {

    @Nullable
    private String topic;

    @Nullable
    private TopicDescription description;
}
