package com.asyncapi.parser.java.model.channel.operation;

import com.asyncapi.parser.java.jackson.ListOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;

public class OperationTraitsDeserializer extends ListOfReferencesOrObjectsDeserializer<OperationTrait> {

    @Override
    public Class<OperationTrait> objectTypeClass() {
        return OperationTrait.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
