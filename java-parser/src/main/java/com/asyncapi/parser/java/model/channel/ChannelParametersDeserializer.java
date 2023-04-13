package com.asyncapi.parser.java.model.channel;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;

public class ChannelParametersDeserializer extends MapOfReferencesOrObjectsDeserializer<Parameter> {

    @Override
    public Class<Parameter> objectTypeClass() {
        return Parameter.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
