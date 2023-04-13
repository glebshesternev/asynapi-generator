package com.asyncapi.parser.java.jackson.binding.server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.binding.server.amqp.AMQPServerBinding;
import com.asyncapi.parser.java.binding.server.googlepubsub.GooglePubSubServerBinding;
import com.asyncapi.parser.java.binding.server.http.HTTPServerBinding;
import com.asyncapi.parser.java.binding.server.jms.JMSServerBinding;
import com.asyncapi.parser.java.binding.server.kafka.KafkaServerBinding;
import com.asyncapi.parser.java.jackson.BindingsMapDeserializer;
import com.asyncapi.parser.java.model.Reference;

import java.io.IOException;


public class ServerBindingsDeserializer extends BindingsMapDeserializer {

    @Override
    public Object chooseKnownPojo(String bindingKey, JsonNode binding, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = binding.traverse(objectCodec)) {
            if (binding.get("$ref") != null) {
                return jsonParser.readValueAs(Reference.class);
            }

            switch (bindingKey) {
                case "amqp":
                    return jsonParser.readValueAs(AMQPServerBinding.class);
                case "googlepubsub":
                    return jsonParser.readValueAs(GooglePubSubServerBinding.class);
                case "http":
                    return jsonParser.readValueAs(HTTPServerBinding.class);
                case "jms":
                    return jsonParser.readValueAs(JMSServerBinding.class);
                case "kafka":
                    return jsonParser.readValueAs(KafkaServerBinding.class);
                default:
                    return null;
            }
        }
    }

}
