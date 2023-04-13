package com.asyncapi.parser.java.model.channel.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OneOfMessages {


    @NotNull
    @JsonDeserialize(using = MessagesDeserializer.class)
    private List<Object> oneOf;

}
