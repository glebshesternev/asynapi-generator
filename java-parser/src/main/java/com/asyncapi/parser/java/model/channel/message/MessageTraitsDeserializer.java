package com.asyncapi.parser.java.model.channel.message;

import com.asyncapi.parser.java.jackson.ListOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;


public class MessageTraitsDeserializer extends ListOfReferencesOrObjectsDeserializer<MessageTrait> {

    @Override
    public Class<MessageTrait> objectTypeClass() {
        return MessageTrait.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
