package com.asyncapi.parser.java.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public abstract class MapOfReferencesOrObjectsDeserializer<ObjectType> extends JsonDeserializer<Map<String, Object>> {

    abstract public Class<ObjectType> objectTypeClass();

    abstract public Class<?> referenceClass();

    @Override
    public Map<String, Object> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode map = objectCodec.readTree(jsonParser);

        Map<String, Object> parameters = new HashMap<>();

        map.fieldNames().forEachRemaining(
                fieldName -> {
                    try {
                        parameters.put(fieldName, chooseKnownPojo(map.get(fieldName), objectCodec));
                    } catch (IOException ignore) {
                        try {
                            parameters.put(fieldName, readAsObject(map.get(fieldName), objectCodec));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        return parameters;
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
