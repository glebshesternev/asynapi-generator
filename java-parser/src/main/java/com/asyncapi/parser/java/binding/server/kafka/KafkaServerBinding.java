package com.asyncapi.parser.java.binding.server.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.binding.server.ServerBinding;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KafkaServerBinding extends ServerBinding {

    @NotNull
    private List<String> bootstrapServers;

    @Nullable
    private Security ssl;

    @Nullable
    @Builder.Default
    private String bindingVersion = "0.4.0";
}
