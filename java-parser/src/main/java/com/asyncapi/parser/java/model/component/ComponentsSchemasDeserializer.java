package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.schema.Schema;

public class ComponentsSchemasDeserializer extends MapOfReferencesOrObjectsDeserializer<Schema> {

    @Override
    public Class<Schema> objectTypeClass() {
        return Schema.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
