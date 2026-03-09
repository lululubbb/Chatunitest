package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_sameClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertTrue(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_superclass() {
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
  void isAssignableFrom_withClass_interfaceImplemented() {
    TypeToken<CharSequence> token = TypeToken.get(CharSequence.class);
    assertTrue(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_null() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertFalse(token.isAssignableFrom((Class<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_objectClass() {
    TypeToken<Object> token = TypeToken.get(Object.class);
    assertTrue(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClass_primitiveAndWrapper() {
    TypeToken<Integer> token = TypeToken.get(Integer.class);
    assertFalse(token.isAssignableFrom(int.class));
    TypeToken<int[]> arrayToken = TypeToken.get(int[].class);
    assertTrue(arrayToken.isAssignableFrom(int[].class));
  }
}