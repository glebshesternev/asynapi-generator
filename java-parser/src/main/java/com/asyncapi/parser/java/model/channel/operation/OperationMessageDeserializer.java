package com.asyncapi.parser.java.model.channel.operation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.message.Message;
import com.asyncapi.parser.java.model.channel.message.OneOfMessages;

import java.io.IOException;

public class OperationMessageDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);

        return chooseKnownPojo(node, objectCodec);
    }

    private Object chooseKnownPojo(JsonNode message, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = message.traverse(objectCodec)) {
            if (message.get("oneOf") != null) {
                return jsonParser.readValueAs(OneOfMessages.class);
            } else if (message.get("$ref") != null) {
                return jsonParser.readValueAs(Reference.class);
            } else {
                return jsonParser.readValueAs(Message.class);
            }
        }
    }
}
