package com.asyncapi.parser.java.binding.operation.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.binding.operation.OperationBinding;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HTTPOperationBinding extends OperationBinding {

    @NotNull
    private String type;

    @Nullable
    private String method;

    @Nullable
    private Object query;

    @Nullable
    @Builder.Default
    private String bindingVersion = "0.1.0";

}
