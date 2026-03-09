package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_138_6Test {

  @Test
    @Timeout(8000)
  void testGet_withClassType() {
    Class<String> clazz = String.class;
    TypeToken<?> typeToken = TypeToken.get(clazz);
    assertNotNull(typeToken);
    assertEquals(String.class, typeToken.getRawType());
    assertEquals(String.class, typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withParameterizedType() {
    Type parameterizedType = new TypeToken<Map<String, Integer>>() {}.getType();
    TypeToken<?> typeToken = TypeToken.get(parameterizedType);
    assertNotNull(typeToken);
    assertEquals(Map.class, typeToken.getRawType());
    assertEquals(parameterizedType, typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withGenericArrayType() {
    Type genericArrayType = new TypeToken<String[]>() {}.getType();
    TypeToken<?> typeToken = TypeToken.get(genericArrayType);
    assertNotNull(typeToken);
    assertEquals(String[].class, typeToken.getRawType());
    assertEquals(genericArrayType, typeToken.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withTypeVariable() {
    TypeToken<?> typeToken = TypeToken.get(TypeToken.class.getTypeParameters()[0]);
    assertNotNull(typeToken);
    assertEquals(Object.class, typeToken.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGet_withNullType() {
    assertThrows(NullPointerException.class, () -> {
      TypeToken.get(null);
    });
  }
}