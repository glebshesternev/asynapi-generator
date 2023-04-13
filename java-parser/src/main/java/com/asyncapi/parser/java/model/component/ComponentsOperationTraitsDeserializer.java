package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.operation.OperationTrait;

public class ComponentsOperationTraitsDeserializer extends MapOfReferencesOrObjectsDeserializer<OperationTrait> {

    @Override
    public Class<OperationTrait> objectTypeClass() {
        return OperationTrait.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
