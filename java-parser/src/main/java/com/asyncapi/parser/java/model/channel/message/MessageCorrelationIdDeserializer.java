package com.asyncapi.parser.java.model.channel.message;

import com.asyncapi.parser.java.jackson.ReferenceOrObjectDeserializer;
import com.asyncapi.parser.java.model.Reference;

public class MessageCorrelationIdDeserializer extends ReferenceOrObjectDeserializer<CorrelationId> {

    @Override
    public Class<CorrelationId> objectTypeClass() {
        return CorrelationId.class;
    }

    public Class<?> referenceClass() {
        return Reference.class;
    }

}
