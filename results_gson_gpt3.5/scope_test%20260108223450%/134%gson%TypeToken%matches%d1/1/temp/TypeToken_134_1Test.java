package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_134_1Test {

  @Test
    @Timeout(8000)
  void matches_whenToEqualsFrom_returnsTrue() throws Exception {
    Type from = String.class;
    Type to = String.class;
    Map<String, Type> typeMap = new HashMap<>();

    // Use reflection to invoke private static method
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToEqualsTypeMapValue_returnsTrue() throws Exception {
    // Create a mock TypeVariable with name "T"
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    when(from.getName()).thenReturn("T");

    Type to = Integer.class;
    Map<String, Type> typeMap = new HashMap<>();
    typeMap.put("T", to);

    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToNotEqualsTypeMapValue_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    when(from.getName()).thenReturn("T");

    Type to = Integer.class;
    Map<String, Type> typeMap = new HashMap<>();
    typeMap.put("T", String.class);

    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromNotEqualsToAndFromNotTypeVariable_returnsFalse() throws Exception {
    Type from = String.class;
    Type to = Integer.class;
    Map<String, Type> typeMap = new HashMap<>();

    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertFalse(result);
  }
}