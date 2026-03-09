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

class TypeToken_134_3Test {

  @Test
    @Timeout(8000)
  void testMatches_toEqualsFrom_returnsTrue() throws Exception {
    Type from = mock(Type.class);
    Type to = from;

    // Using reflection to invoke private static method
    boolean result = (boolean) invokeMatches(from, to, new HashMap<>());

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testMatches_fromIsTypeVariable_toEqualsTypeMapValue_returnsTrue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();

    when(from.getName()).thenReturn("T");
    typeMap.put("T", to);

    boolean result = (boolean) invokeMatches(from, to, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testMatches_fromIsTypeVariable_toNotEqualsTypeMapValue_returnsFalse() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> from = mock(TypeVariable.class);
    Type to = mock(Type.class);
    Type mappedType = mock(Type.class);
    Map<String, Type> typeMap = new HashMap<>();

    when(from.getName()).thenReturn("T");
    typeMap.put("T", mappedType);
    // to != mappedType
    assertNotEquals(to, mappedType);

    boolean result = (boolean) invokeMatches(from, to, typeMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testMatches_fromNotTypeVariableToNotEqual_returnsFalse() throws Exception {
    Type realFrom = String.class; // Class implements Type, and is not TypeVariable
    Type realTo = Integer.class;  // Different class, so equals should be false

    // Use spy to override equals safely with doReturn
    Type toSpy = spy(realTo);
    doReturn(false).when(toSpy).equals(realFrom);

    boolean result = (boolean) invokeMatches(realFrom, toSpy, new HashMap<>());

    assertFalse(result);
  }

  private Object invokeMatches(Type from, Type to, Map<String, Type> typeMap) throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);
    return method.invoke(null, from, to, typeMap);
  }
}