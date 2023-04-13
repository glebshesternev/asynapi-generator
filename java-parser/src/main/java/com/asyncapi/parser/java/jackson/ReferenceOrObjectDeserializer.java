package com.asyncapi.parser.java.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.IOException;

public abstract class ReferenceOrObjectDeserializer<ObjectType> extends JsonDeserializer<Object> {

    abstract public Class<ObjectType> objectTypeClass();

    abstract public Class<?> referenceClass();

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);

        try {
            return chooseKnownPojo(node, objectCodec);
        } catch (UnrecognizedPropertyException unrecognizedPropertyException) {
            return readAsObject(node, objectCodec);
        }
    }

    private Object chooseKnownPojo(JsonNode jsonNode, ObjectCodec objectCodec)
            throws IOException {
        JsonNode ref = jsonNode.get("$ref");
        try (JsonParser jsonParser = jsonNode.traverse(objectCodec)) {
            if (ref != null) {
                return jsonParser.readValueAs(referenceClass());
            } else {
                return jsonParser.readValueAs(objectTypeClass());
            }
        }
    }

    private Object readAsObject(JsonNode jsonNode, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = jsonNode.traverse(objectCodec)) {
            return jsonParser.readValueAs(objectTypeClass());
        }
    }

}
