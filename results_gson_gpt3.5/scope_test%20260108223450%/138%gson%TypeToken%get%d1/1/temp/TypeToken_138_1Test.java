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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeToken_138_1Test {

  @Test
    @Timeout(8000)
  @DisplayName("Test get(Type) with null type")
  void testGetWithNullType() {
    // TypeToken.get(null) throws NPE, so we check for that and skip or handle differently
    assertThrows(NullPointerException.class, () -> TypeToken.get(null));
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test get(Type) with Class type")
  void testGetWithClassType() {
    Type type = String.class;
    TypeToken<?> token = TypeToken.get(type);
    assertNotNull(token);
    assertEquals(type, token.getType());
    assertEquals(type, token.getRawType());
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test get(Type) with ParameterizedType")
  void testGetWithParameterizedType() throws Exception {
    TypeToken<Map<String, Integer>> mapToken = new TypeToken<Map<String, Integer>>() {};
    Type parameterizedType = mapToken.getType();

    TypeToken<?> token = TypeToken.get(parameterizedType);
    assertNotNull(token);
    assertEquals(parameterizedType, token.getType());
    // rawType of Map<String,Integer> is Map.class (interface), not HashMap.class (implementation)
    assertEquals(Map.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test get(Type) with GenericArrayType")
  void testGetWithGenericArrayType() throws Exception {
    TypeToken<Map<String, Integer>[]> arrayToken = new TypeToken<Map<String, Integer>[]>() {};
    Type genericArrayType = arrayToken.getType();

    TypeToken<?> token = TypeToken.get(genericArrayType);
    assertNotNull(token);
    assertEquals(genericArrayType, token.getType());
    // rawType should be Map[].class, not Object[].class
    assertEquals(Map[].class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  @DisplayName("Test get(Type) with TypeVariable")
  void testGetWithTypeVariable() throws Exception {
    class GenericClass<T> {
      Type getTypeVariable() {
        return GenericClass.class.getTypeParameters()[0];
      }
    }
    GenericClass<?> genericClass = new GenericClass<>();
    Type typeVariable = genericClass.getTypeVariable();

    TypeToken<?> token = TypeToken.get(typeVariable);
    assertNotNull(token);
    assertEquals(typeVariable, token.getType());
    assertEquals(Object.class, token.getRawType());
  }
}