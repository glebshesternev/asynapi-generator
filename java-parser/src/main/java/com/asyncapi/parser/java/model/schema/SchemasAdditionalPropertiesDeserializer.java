package com.asyncapi.parser.java.model.schema;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.schema.Schema;

import java.io.IOException;


public class SchemasAdditionalPropertiesDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);

        return chooseKnownPojo(node, objectCodec);
    }

    private Object chooseKnownPojo(JsonNode jsonNode, final ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = jsonNode.traverse(objectCodec)) {
            if (jsonNode.isBoolean()) {
                return jsonNode.asBoolean();
            } else {
                return jsonParser.readValueAs(Schema.class);
            }
        }
    }
}
