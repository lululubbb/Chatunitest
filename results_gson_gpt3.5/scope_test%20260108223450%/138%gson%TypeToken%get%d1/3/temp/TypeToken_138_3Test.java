package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_138_3Test {

  @Test
    @Timeout(8000)
  void testGet_withClassType() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withParameterizedType() {
    Type parameterizedType = new TypeToken<Map<String, Integer>>() {}.getType();
    TypeToken<?> token = TypeToken.get(parameterizedType);
    assertNotNull(token);
    // Accept either Map.class or HashMap.class as raw type
    Class<?> rawType = token.getRawType();
    assertTrue(rawType == Map.class || rawType == java.util.HashMap.class,
        "Raw type should be HashMap or Map depending on implementation");
    assertEquals(parameterizedType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withGenericArrayType() {
    Type arrayType = new TypeToken<String[]>() {}.getType();
    TypeToken<?> token = TypeToken.get(arrayType);
    assertNotNull(token);
    assertEquals(String[].class, token.getRawType());
    assertEquals(arrayType, token.getType());
  }

  @Test
    @Timeout(8000)
  void testGet_withTypeVariable() throws Exception {
    // Create a dummy TypeVariable via reflection on a generic class
    class GenericClass<T> {
      T field;
    }
    TypeVariable<?>[] typeParameters = GenericClass.class.getTypeParameters();
    assertTrue(typeParameters.length > 0);
    TypeVariable<?> typeVariable = typeParameters[0];
    TypeToken<?> token = TypeToken.get(typeVariable);
    assertNotNull(token);
    assertEquals(Object.class, token.getRawType());
    assertEquals(typeVariable, token.getType());
  }
}