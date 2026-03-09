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

class TypeToken_134_5Test {

  @Test
    @Timeout(8000)
  void matches_whenToEqualsFrom_returnsTrue() throws Exception {
    Type from = mock(Type.class);
    Type to = from;
    Map<String, Type> typeMap = new HashMap<>();

    // invoke private static method matches
    boolean result = invokeMatches(from, to, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToEqualsMapping_returnsTrue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();
    when(from.getName()).thenReturn("X");
    typeMap.put("X", to);

    // do NOT mock equals (final method), just rely on actual equals behavior

    boolean result = invokeMatches(from, to, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToNotEqualsMapping_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Type otherType = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();
    when(from.getName()).thenReturn("X");
    typeMap.put("X", otherType);

    // do NOT mock equals (final method), just rely on actual equals behavior

    boolean result = invokeMatches(from, to, typeMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsNotTypeVariableAndNotEqualTo_returnsFalse() throws Exception {
    Type from = mock(Type.class);
    Type to = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();

    // do NOT mock equals (final method), just rely on actual equals behavior

    boolean result = invokeMatches(from, to, typeMap);

    assertFalse(result);
  }

  private boolean invokeMatches(Type from, Type to, Map<String, Type> typeMap) throws Exception {
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeMap);
  }
}