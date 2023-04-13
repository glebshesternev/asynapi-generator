package com.asyncapi.parser.java.jackson.binding.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.binding.message.amqp.AMQPMessageBinding;
import com.asyncapi.parser.java.binding.message.googlepubsub.GooglePubSubMessageBinding;
import com.asyncapi.parser.java.binding.message.http.HTTPMessageBinding;
import com.asyncapi.parser.java.binding.message.jms.JMSMessageBinding;
import com.asyncapi.parser.java.binding.message.kafka.KafkaMessageBinding;
import com.asyncapi.parser.java.jackson.BindingsMapDeserializer;
import com.asyncapi.parser.java.model.Reference;

import java.io.IOException;


public class MessageBindingsDeserializer extends BindingsMapDeserializer {

    public Object chooseKnownPojo(String bindingKey, JsonNode binding, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = binding.traverse(objectCodec)) {
            if (binding.get("$ref") != null) {
                return jsonParser.readValueAs(Reference.class);
            }

            switch (bindingKey) {
                case "amqp":
                    return jsonParser.readValueAs(AMQPMessageBinding.class);
                case "googlepubsub":
                    return jsonParser.readValueAs(GooglePubSubMessageBinding.class);
                case "http":
                    return jsonParser.readValueAs(HTTPMessageBinding.class);
                case "jms":
                    return jsonParser.readValueAs(JMSMessageBinding.class);
                case "kafka":
                    return jsonParser.readValueAs(KafkaMessageBinding.class);
                default:
                    return null;
            }
        }
    }

}
