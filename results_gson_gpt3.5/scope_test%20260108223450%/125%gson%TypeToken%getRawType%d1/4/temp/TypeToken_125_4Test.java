package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TypeToken_125_4Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClassType() {
    TypeToken<String> typeToken = new TypeToken<String>() {};
    Class<? super String> rawType = typeToken.getRawType();
    assertEquals(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() throws Exception {
    // Use reflection to create a TypeToken of a parameterized type
    java.lang.reflect.Type paramType = new com.google.gson.reflect.TypeToken<java.util.Map<String, Integer>>() {}.getType();
    java.lang.reflect.Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(java.lang.reflect.Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = constructor.newInstance(paramType);
    Class<?> rawType = token.getRawType();
    assertEquals(java.util.Map.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withArrayType() throws Exception {
    java.lang.reflect.Type arrayType = new com.google.gson.reflect.TypeToken<String[]>() {}.getType();
    java.lang.reflect.Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(java.lang.reflect.Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = constructor.newInstance(arrayType);
    Class<?> rawType = token.getRawType();
    assertEquals(String[].class, rawType);
  }
}