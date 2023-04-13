package com.asyncapi.parser.java.model.channel.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CorrelationId extends ExtendableObject {

    @Nullable
    private String description;

    @NotNull
    private String location;

}
