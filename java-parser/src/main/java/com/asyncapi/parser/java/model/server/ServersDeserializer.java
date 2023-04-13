package com.asyncapi.parser.java.model.server;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;


public class ServersDeserializer extends MapOfReferencesOrObjectsDeserializer<Server> {

    @Override
    public Class<Server> objectTypeClass() {
        return Server.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
