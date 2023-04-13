package com.asyncapi.parser.java.model.channel.message;

import com.asyncapi.parser.java.jackson.ListOfReferencesOrObjectsDeserializer;
import com.asyncapi.parser.java.model.Reference;

public class MessagesDeserializer extends ListOfReferencesOrObjectsDeserializer<Message> {

    @Override
    public Class<Message> objectTypeClass() {
        return Message.class;
    }

    @Override
    public Class<?> referenceClass() {
        return Reference.class;
    }

}
