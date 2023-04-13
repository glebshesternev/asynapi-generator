package com.asyncapi.parser.java.jackson.binding.channel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.asyncapi.parser.java.binding.channel.amqp.AMQPChannelBinding;
import com.asyncapi.parser.java.binding.channel.googlepubsub.GooglePubSubChannelBinding;
import com.asyncapi.parser.java.binding.channel.http.HTTPChannelBinding;
import com.asyncapi.parser.java.binding.channel.jms.JMSChannelBinding;
import com.asyncapi.parser.java.binding.channel.kafka.KafkaChannelBinding;
import com.asyncapi.parser.java.jackson.BindingsMapDeserializer;
import com.asyncapi.parser.java.model.Reference;

import java.io.IOException;


public class ChannelBindingsDeserializer extends BindingsMapDeserializer {

    public Object chooseKnownPojo(String bindingKey, JsonNode binding, ObjectCodec objectCodec)
            throws IOException {
        try (JsonParser jsonParser = binding.traverse(objectCodec)) {
            if (binding.get("$ref") != null) {
                return jsonParser.readValueAs(Reference.class);
            }

            switch (bindingKey) {
                case "amqp":
                    return jsonParser.readValueAs(AMQPChannelBinding.class);
                case "googlepubsub":
                    return jsonParser.readValueAs(GooglePubSubChannelBinding.class);
                case "http":
                    return jsonParser.readValueAs(HTTPChannelBinding.class);
                case "jms":
                    return jsonParser.readValueAs(JMSChannelBinding.class);
                case "kafka":
                    return jsonParser.readValueAs(KafkaChannelBinding.class);
                default:
                    return null;
            }
        }
    }

}
