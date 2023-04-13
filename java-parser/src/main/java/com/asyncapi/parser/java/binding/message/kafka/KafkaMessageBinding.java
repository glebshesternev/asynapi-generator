package com.asyncapi.parser.java.binding.message.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.binding.message.MessageBinding;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KafkaMessageBinding extends MessageBinding {

    @Nullable
    private Object key;

    @Nullable
    private String schemaIdLocation;

    @Nullable
    private String schemaIdPayloadEncoding;

    @Nullable
    private String schemaLookupStrategy;

    @Nullable
    @Builder.Default
    private String bindingVersion = "0.4.0";

}
