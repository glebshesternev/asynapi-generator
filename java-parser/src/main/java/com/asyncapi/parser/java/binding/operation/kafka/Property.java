package com.asyncapi.parser.java.binding.operation.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    private String fieldName;
    private Class<?> clazz;
    private Object value;
    /*
     *  if isBaseProp then add this prop to builder method
     */
    private boolean isBaseProp;
}