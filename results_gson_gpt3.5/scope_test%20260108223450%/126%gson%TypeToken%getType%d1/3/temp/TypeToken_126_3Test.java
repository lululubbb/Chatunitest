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

class TypeToken_126_3Test {

  @Test
    @Timeout(8000)
  void testGetType_withDefaultConstructor_shouldReturnType() {
    TypeToken<String> token = new TypeToken<String>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withParameterizedTypeToken_shouldReturnType() {
    TypeToken<Map<String, Integer>> token = new TypeToken<Map<String, Integer>>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(Map.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_withArrayType_shouldReturnType() {
    TypeToken<String[]> token = new TypeToken<String[]>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String[].class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_staticGetMethod_shouldReturnType() {
    TypeToken<?> token = TypeToken.get(String.class);
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_staticGetParameterized_shouldReturnType() {
    TypeToken<?> token = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(Map.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType_staticGetArray_shouldReturnType() {
    TypeToken<?> token = TypeToken.getArray(String.class);
    Type type = token.getType();
    assertNotNull(type);
    assertEquals(String[].class, token.getRawType());
  }
}