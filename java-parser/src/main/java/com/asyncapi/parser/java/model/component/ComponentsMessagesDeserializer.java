package com.asyncapi.parser.java.model.component;

import com.asyncapi.parser.java.jackson.MapOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;
import com.asyncapi.parser.java.model.channel.message.Message;

public class ComponentsMessagesDeserializer extends MapOfReferencesOrObjectsDeserializer<Message> {

    @Override
    public Class<Message> objectTypeClass() {
        return Message.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
