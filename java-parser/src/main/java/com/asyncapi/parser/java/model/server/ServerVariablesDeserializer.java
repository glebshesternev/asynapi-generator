package com.asyncapi.parser.java.model.server;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;


public class ServerVariablesDeserializer extends MapOfReferencesOrObjectsDeserializer<ServerVariable> {

    @Override
    public Class<ServerVariable> objectTypeClass() {
        return ServerVariable.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
