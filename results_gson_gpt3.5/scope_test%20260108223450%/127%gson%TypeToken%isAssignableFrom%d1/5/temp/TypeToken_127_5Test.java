package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_isAssignableFromTest {

  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_sameClass() {
    Class<?> cls = Object.class;
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertTrue(token.isAssignableFrom(cls));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_subclass() {
    TypeToken<Number> token = TypeToken.get(Number.class);
    assertTrue(token.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_unrelatedClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertFalse(token.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withType_sameType() {
    TypeToken<String> token = TypeToken.get(String.class);
    Type type = String.class;
    assertTrue(token.isAssignableFrom(type));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withType_parameterizedType() {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(Map.class);
    TypeToken<Map> token = TypeToken.get(Map.class);
    assertTrue(token.isAssignableFrom(parameterizedType));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withType_genericArrayType() {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    TypeToken<Object[]> token = TypeToken.get(Object[].class);
    assertFalse(token.isAssignableFrom(genericArrayType));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeToken_sameRawType() {
    TypeToken<String> token = TypeToken.get(String.class);
    TypeToken<?> other = TypeToken.get(String.class);
    assertTrue(token.isAssignableFrom(other));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeToken_differentRawType() {
    TypeToken<String> token = TypeToken.get(String.class);
    TypeToken<?> other = TypeToken.get(Integer.class);
    assertFalse(token.isAssignableFrom(other));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withNullClass() {
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertFalse(token.isAssignableFrom((Class<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withNullType() {
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertFalse(token.isAssignableFrom((Type) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withNullTypeToken() {
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertFalse(token.isAssignableFrom((TypeToken<?>) null));
  }
}