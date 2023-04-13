package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.Parameter;

public class ComponentsParametersDeserializer extends MapOfReferencesOrObjectsDeserializer<Parameter> {

    @Override
    public Class<Parameter> objectTypeClass() {
        return Parameter.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
