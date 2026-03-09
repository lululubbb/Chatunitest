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

import java.lang.reflect.Type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeToken_138_4Test {

  @Test
    @Timeout(8000)
  @DisplayName("get(Type) returns non-null TypeToken with correct type and rawType")
  void testGetWithType() {
    Type type = String.class;
    TypeToken<?> token = TypeToken.get(type);

    assertNotNull(token, "TypeToken.get should not return null");
    assertEquals(type, token.getType(), "TypeToken.get type should match input type");
    assertEquals(String.class, token.getRawType(), "TypeToken.get rawType should match input class");
  }

  @Test
    @Timeout(8000)
  @DisplayName("get(Type) with parameterized type returns TypeToken with correct rawType")
  void testGetWithParameterizedType() throws Exception {
    Type parameterizedType = new TypeToken<java.util.List<String>>() {}.getType();
    TypeToken<?> token = TypeToken.get(parameterizedType);

    assertNotNull(token);
    assertEquals(parameterizedType, token.getType());
    assertEquals(java.util.List.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  @DisplayName("get(Type) with array type returns TypeToken with correct rawType")
  void testGetWithArrayType() {
    Type type = String[].class;
    TypeToken<?> token = TypeToken.get(type);

    assertNotNull(token);
    assertEquals(type, token.getType());
    assertEquals(String[].class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  @DisplayName("get(Type) with null type throws NullPointerException")
  void testGetWithNullType() {
    assertThrows(NullPointerException.class, () -> {
      TypeToken.get(null);
    });
  }
}