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

import org.junit.jupiter.api.Test;

class TypeToken_138_5Test {

  @Test
    @Timeout(8000)
  void testGet_withClassType() {
    Class<String> clazz = String.class;
    TypeToken<?> token = TypeToken.get(clazz);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());
    assertEquals(clazz, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withParameterizedType() throws Exception {
    // Create a ParameterizedType instance via reflection for java.util.Map<String, Integer>
    Type mapType = new com.google.gson.reflect.TypeToken<java.util.Map<String, Integer>>() {}.getType();
    TypeToken<?> token = TypeToken.get(mapType);
    assertNotNull(token);
    assertEquals(java.util.Map.class, token.getRawType());
    assertEquals(mapType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withGenericArrayType() throws Exception {
    // Create a GenericArrayType for List<String>[]
    Type genericArrayType = new com.google.gson.reflect.TypeToken<java.util.List<String>[]>() {}.getType();
    TypeToken<?> token = TypeToken.get(genericArrayType);
    assertNotNull(token);
    assertEquals(java.util.List[].class, token.getRawType());
    assertEquals(genericArrayType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withTypeVariable() throws Exception {
    // Use a TypeVariable from a dummy generic class
    class Generic<T> {
      Type getTypeVariable() throws Exception {
        return Generic.class.getTypeParameters()[0];
      }
    }
    Generic<?> generic = new Generic<>();
    Type typeVariable = generic.getTypeVariable();
    TypeToken<?> token = TypeToken.get(typeVariable);
    assertNotNull(token);
    // Raw type is Object.class for TypeVariables in this implementation
    assertEquals(Object.class, token.getRawType());
    assertEquals(typeVariable, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withNullType() {
    assertThrows(NullPointerException.class, () -> TypeToken.get(null));
  }
}