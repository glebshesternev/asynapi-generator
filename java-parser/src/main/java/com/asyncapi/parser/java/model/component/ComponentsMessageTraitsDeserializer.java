package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.message.MessageTrait;

public class ComponentsMessageTraitsDeserializer extends MapOfReferencesOrObjectsDeserializer<MessageTrait> {

    @Override
    public Class<MessageTrait> objectTypeClass() {
        return MessageTrait.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
