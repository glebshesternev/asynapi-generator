package com.asyncapi.parser.java.model.channel.message;

import com.asyncapi.parser.java.jackson.ReferenceOrObjectDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.schema.Schema;

public class MessageHeadersDeserializer extends ReferenceOrObjectDeserializer<Schema> {

    @Override
    public Class<Schema> objectTypeClass() {
        return Schema.class;
    }

    public Class<?> referenceClass() {
        return Reference.class;
    }

}
