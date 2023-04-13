package com.asyncapi.parser.java.model.channel.message;

import com.asyncapi.parser.java.jackson.ObjectDeserializer;
import com.asyncapi.parser.java.schema.Schema;

public class MessagePayloadDeserializer extends ObjectDeserializer<Schema> {

    @Override
    public Class<Schema> objectTypeClass() {
        return Schema.class;
    }

}
