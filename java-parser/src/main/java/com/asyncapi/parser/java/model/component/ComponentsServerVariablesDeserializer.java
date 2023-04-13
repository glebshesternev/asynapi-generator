package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.server.ServerVariable;

public class ComponentsServerVariablesDeserializer extends MapOfReferencesOrObjectsDeserializer<ServerVariable> {

    @Override
    public Class<ServerVariable> objectTypeClass() {
        return ServerVariable.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
