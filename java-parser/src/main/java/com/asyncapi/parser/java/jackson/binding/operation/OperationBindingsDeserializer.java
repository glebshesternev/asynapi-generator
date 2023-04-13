package com.asyncapi.parser.java.jackson.binding.operation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.binding.operation.amqp.AMQPOperationBinding;
import com.asyncapi.parser.java.binding.operation.googlepubsub.GooglePubSubOperationBinding;
import com.asyncapi.parser.java.binding.operation.http.HTTPOperationBinding;
import com.asyncapi.parser.java.binding.operation.jms.JMSOperationBinding;
import com.asyncapi.parser.java.binding.operation.kafka.KafkaOperationBinding;
import com.asyncapi.parser.java.jackson.BindingsMapDeserializer;
import com.asyncapi.parser.java.model.Reference;

import java.io.IOException;


public class OperationBindingsDeserializer extends BindingsMapDeserializer {

    public Object chooseKnownPojo(String bindingKey, JsonNode binding, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = binding.traverse(objectCodec)) {
            if (binding.get("$ref") != null) {
                return jsonParser.readValueAs(Reference.class);
            }

            switch (bindingKey) {
                case "amqp":
                    return jsonParser.readValueAs(AMQPOperationBinding.class);
                case "googlepubsub":
                    return jsonParser.readValueAs(GooglePubSubOperationBinding.class);
                case "http":
                    return jsonParser.readValueAs(HTTPOperationBinding.class);
                case "jms":
                    return jsonParser.readValueAs(JMSOperationBinding.class);
                case "kafka":
                    return jsonParser.readValueAs(KafkaOperationBinding.class);
                default:
                    return null;
            }
        }
    }

}
