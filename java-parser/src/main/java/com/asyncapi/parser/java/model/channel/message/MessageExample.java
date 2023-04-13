package com.asyncapi.parser.java.model.channel.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;

import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageExample extends ExtendableObject {

    @Nullable
    public Map<String, Object> headers;

    @Nullable
    private Object payload;

    @Nullable
    private String name;

    @Nullable
    private String summary;

}
