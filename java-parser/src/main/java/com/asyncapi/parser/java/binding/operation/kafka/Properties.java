package com.asyncapi.parser.java.binding.operation.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import com.asyncapi.parser.java.binding.server.kafka.Security;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Properties extends HashMap<String, Property> {

    public Consumer<Property> in(String key) {
        return value -> this.put(key, value);
    }

    public Properties with(Security security, Map<String, String> properties) {
        this.putAll(security.buildProperties());
        this.putAll(properties.entrySet().stream()
                .map(x-> Pair.of(x.getKey(), new Property(x.getKey(), String.class, x.getValue(), true)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
        return this;
    }
}