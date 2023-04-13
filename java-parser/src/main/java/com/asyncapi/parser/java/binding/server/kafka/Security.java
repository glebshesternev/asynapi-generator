package com.asyncapi.parser.java.binding.server.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.PropertyMapper;
import com.asyncapi.parser.java.binding.operation.kafka.Property;
import com.asyncapi.parser.java.binding.operation.kafka.Properties;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Security {

    @Nullable
    private String keyPassword;

    @Nullable
    private String keyStoreCertificateChain;

    @Nullable
    private String keyStoreKey;

    @Nullable
    private String keyStoreLocation;

    @Nullable
    private String keyStorePassword;

    @Nullable
    private String keyStoreType;

    @Nullable
    private String trustStoreCertificates;

    @Nullable
    private String trustStoreLocation;

    @Nullable
    private String trustStorePassword;

    @Nullable
    private String trustStoreType;

    public Map<String, Property> buildProperties() {
        Properties properties = new Properties();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this::getKeyPassword).as(value-> new Property("keyPassword", String.class, value, true)).to(properties.in("ssl.key.password"));
        map.from(this::getKeyStoreCertificateChain).as(value-> new Property("keyStoreCertificateChain", String.class, value, true)).to(properties.in("ssl.keystore.certificate.chain"));
        map.from(this::getKeyStoreKey).as(value-> new Property("keyStoreKey", String.class, value, true)).to(properties.in("ssl.keystore.key"));
        map.from(this::getKeyStoreLocation).as(value-> new Property("keyStoreLocation", String.class, value, true)).to(properties.in("ssl.keystore.location"));
        map.from(this::getKeyStorePassword).as(value-> new Property("keyStorePassword", String.class, value, true)).to(properties.in("ssl.keystore.password"));
        map.from(this::getKeyStoreType).as(value-> new Property("keyStoreType", String.class, value, true)).to(properties.in("ssl.keystore.type"));
        map.from(this::getTrustStoreCertificates).as(value-> new Property("trustStoreCertificates", String.class, value, true)).to(properties.in("ssl.truststore.certificates"));
        map.from(this::getTrustStoreLocation).as(value-> new Property("trustStoreLocation", String.class, value, true)).to(properties.in("ssl.truststore.location"));
        map.from(this::getTrustStorePassword).as(value-> new Property("trustStorePassword", String.class, value, true)).to(properties.in("ssl.truststore.password"));
        map.from(this::getTrustStoreType).as(value-> new Property("trustStoreType", String.class, value, true)).to(properties.in("ssl.truststore.type"));
        return properties;
    }
}