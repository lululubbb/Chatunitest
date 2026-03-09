package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeTokenToStringTest {

  @Test
    @Timeout(8000)
  void testToStringReturnsTypeToString() throws Exception {
    // Use TypeToken.get(Class) factory method to create instance for String class
    TypeToken<String> token = TypeToken.get(String.class);

    // Use the public getType() method instead of reflection to access 'type' field
    Type type = token.getType();

    // Expected string from $Gson$Types.typeToString(type)
    String expected = com.google.gson.internal.$Gson$Types.typeToString(type);

    // Assert toString returns expected string
    assertEquals(expected, token.toString());
  }
}