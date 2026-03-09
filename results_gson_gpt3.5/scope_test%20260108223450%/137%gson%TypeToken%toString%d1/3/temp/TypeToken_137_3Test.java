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
    // Create a TypeToken instance for String.class using the public static get method
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Use reflection to access the internal $Gson$Types.typeToString(Type) method
    Class<?> gsonTypesClass = Class.forName("com.google.gson.internal.$Gson$Types");
    var method = gsonTypesClass.getDeclaredMethod("typeToString", Type.class);
    method.setAccessible(true);

    Type type = typeToken.getType();
    String expected = (String) method.invoke(null, type);

    // Call toString and assert it matches expected
    assertEquals(expected, typeToken.toString());
  }
}