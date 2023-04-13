package com.asyncapi.parser.java.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import com.asyncapi.parser.java.ExtendableObject;
import com.asyncapi.parser.java.model.ExternalDocumentation;
import com.asyncapi.parser.java.model.schema.SchemasAdditionalPropertiesDeserializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Schema extends ExtendableObject {

    @Nullable
    @JsonProperty
    public String title;

    @Nullable
    @JsonProperty
    public String description;

    @Nullable
    @JsonProperty("default")
    public Object defaultValue;

    @Nullable
    @JsonProperty
    public Boolean readOnly;

    @Nullable
    @JsonProperty
    public Boolean writeOnly;

    @Nullable
    @JsonProperty
    public List<Object> examples;
    @Nullable
    @JsonProperty
    public Object type;
    @Nullable
    @JsonProperty("enum")
    public List<Object> enumValue;
    @Nullable
    @JsonProperty("const")
    public Object constValue;
    @Nullable
    @JsonProperty
    public Integer multipleOf;
    @Nullable
    @JsonProperty
    public BigDecimal maximum;
    @Nullable
    @JsonProperty
    public BigDecimal exclusiveMaximum;
    @Nullable
    @JsonProperty
    public BigDecimal minimum;
    @Nullable
    @JsonProperty
    public BigDecimal exclusiveMinimum;
    @Nullable
    @JsonProperty
    public Integer maxLength;
    @Nullable
    @JsonProperty
    public Integer minLength;
    @Nullable
    @JsonProperty
    public String pattern;
    @Nullable
    @JsonProperty
    public Schema items;
    @Nullable
    public Schema additionalItems;
    @Nullable
    @JsonProperty
    public Integer maxItems;
    @Nullable
    @JsonProperty
    public Integer minItems;
    @Nullable
    @JsonProperty
    public Boolean uniqueItems;
    @Nullable
    @JsonProperty
    public Schema contains;
    @Nullable
    @JsonProperty
    public Integer maxProperties;
    @Nullable
    @JsonProperty
    public Integer minProperties;
    @Nullable
    @JsonProperty
    public List<String> required;
    @Nullable
    @JsonProperty
    public Map<String, Schema> properties;
    @Nullable
    @JsonProperty
    public Map<String, Schema> patternProperties;
    @Nullable
    @JsonProperty
    @JsonDeserialize(using = SchemasAdditionalPropertiesDeserializer.class)
    public Object additionalProperties;
    @Nullable
    @JsonProperty
    public Object dependencies;
    @Nullable
    @JsonProperty
    public Schema propertyNames;
    @JsonProperty("if")
    @Nullable
    public Schema ifValue;
    @JsonProperty("then")
    @Nullable
    public Schema thenValue;
    @JsonProperty("else")
    @Nullable
    public Schema elseValue;
    @Nullable
    @JsonProperty
    public List<Schema> allOf;
    @Nullable
    @JsonProperty
    public List<Schema> anyOf;
    @Nullable
    @JsonProperty
    public List<Schema> oneOf;
    @Nullable
    @JsonProperty
    public Schema not;
    @Nullable
    @JsonProperty
    public Object format;
    @Nullable
    @JsonProperty
    public String discriminator;
    @Nullable
    @JsonProperty
    public ExternalDocumentation externalDocs;
    @Nullable
    @JsonProperty
    public Boolean deprecated;
    @Nullable
    @JsonProperty("$ref")
    private String ref;
    @Nullable
    @JsonProperty
    private String contentEncoding;
    @Nullable
    @JsonProperty
    private String contentMediaType;

}
