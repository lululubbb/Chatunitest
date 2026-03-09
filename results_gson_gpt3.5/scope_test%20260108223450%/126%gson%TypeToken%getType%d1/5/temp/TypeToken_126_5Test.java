package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class TypeToken_126_5Test {

  @Test
    @Timeout(8000)
  void testGetType_returnsCorrectType() {
    TypeToken<String> token = new TypeToken<String>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String.class, type);
  }

  @Test
    @Timeout(8000)
  void testGetType_onParameterizedType() {
    TypeToken<java.util.List<String>> token = new TypeToken<java.util.List<String>>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertTrue(type instanceof java.lang.reflect.ParameterizedType);
    assertEquals(java.util.List.class, ((java.lang.reflect.ParameterizedType) type).getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_onArrayType() {
    TypeToken<String[]> token = new TypeToken<String[]>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertTrue(type instanceof java.lang.reflect.GenericArrayType || type instanceof Class<?>);
  }
}