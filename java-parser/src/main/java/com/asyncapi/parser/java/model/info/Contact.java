package com.asyncapi.parser.java.model.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contact extends ExtendableObject {

    @Nullable
    private String name;

    @Nullable
    private String url;

    @Nullable
    private String email;

}
