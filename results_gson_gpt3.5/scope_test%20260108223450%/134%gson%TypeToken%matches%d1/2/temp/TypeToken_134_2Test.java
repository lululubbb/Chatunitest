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

class TypeToken_134_2Test {

  @Test
    @Timeout(8000)
  void matches_whenToEqualsFrom_returnsTrue() throws Exception {
    Type from = mock(Type.class);
    Type to = from;
    Map<String, Type> typeMap = new HashMap<>();

    // Use reflection to invoke private static method
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToEqualsMappedType_returnsTrue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();
    when(from.getName()).thenReturn("T");
    typeMap.put("T", to);

    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromIsTypeVariableAndToNotEqualMappedType_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Type mappedType = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();
    when(from.getName()).thenReturn("T");
    typeMap.put("T", mappedType);

    // to does not equal mappedType
    // equals returns false by default for mocks
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void matches_whenFromNotTypeVariableAndNotEqualTo_returnsFalse() throws Exception {
    Type from = mock(Type.class);
    Type to = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();

    // equals returns false by default for mocks
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, from, to, typeMap);

    assertFalse(result);
  }
}