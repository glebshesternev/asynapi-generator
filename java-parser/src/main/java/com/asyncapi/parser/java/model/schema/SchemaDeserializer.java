package com.asyncapi.parser.java.model.schema;

import com.asyncapi.parser.java.jackson.ReferenceOrObjectDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.schema.Schema;


public class SchemaDeserializer extends ReferenceOrObjectDeserializer<Schema> {

    @Override
    public Class<Schema> objectTypeClass() {
        return Schema.class;
    }

    public Class<?> referenceClass() {
        return Reference.class;
    }

}
