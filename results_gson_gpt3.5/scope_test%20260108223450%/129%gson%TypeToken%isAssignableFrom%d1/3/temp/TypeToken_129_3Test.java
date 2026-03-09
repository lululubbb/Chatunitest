package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_IsAssignableFrom_Test {

  private TypeToken<Object> focalTypeToken;

  @BeforeEach
  void setUp() {
    focalTypeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullToken_returnsFalse() {
    assertFalse(focalTypeToken.isAssignableFrom((TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithNullType_returnsFalse() {
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(null);
    assertFalse(focalTypeToken.isAssignableFrom(token));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithSameType_returnsTrue() {
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(focalTypeToken.getType());
    assertTrue(focalTypeToken.isAssignableFrom(token));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithDifferentType_returnsTrue() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(stringToken.getType());
    boolean result = focalTypeToken.isAssignableFrom(token);
    // Object type token is assignable from String type token, so should be true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithUnrelatedType_returnsTrue() {
    TypeToken<int[]> intArrayToken = new TypeToken<int[]>() {};
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(intArrayToken.getType());
    boolean result = focalTypeToken.isAssignableFrom(token);
    // Object is assignable from int[] (since int[] extends Object)
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithParameterizedType() throws Exception {
    // Create a TypeToken for Map<String, String>
    TypeToken<?> mapToken = TypeToken.getParameterized(Map.class, String.class, String.class);
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(mapToken.getType());
    boolean result = focalTypeToken.isAssignableFrom(token);
    // Object is assignable from Map<String, String>
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithGenericArrayType() throws Exception {
    // Create a TypeToken for String[]
    TypeToken<?> stringArrayToken = TypeToken.getArray(String.class);
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(stringArrayToken.getType());
    boolean result = focalTypeToken.isAssignableFrom(token);
    // Object is assignable from String[]
    assertTrue(result);
  }
}