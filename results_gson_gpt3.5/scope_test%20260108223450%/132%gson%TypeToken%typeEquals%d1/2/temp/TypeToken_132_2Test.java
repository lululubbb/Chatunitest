package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class TypeToken_typeEquals_Test {

  private Method typeEqualsMethod;

  @BeforeEach
  void setUp() throws Exception {
    typeEqualsMethod = TypeToken.class.getDeclaredMethod(
        "typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    typeEqualsMethod.setAccessible(true);
  }

  private boolean invokeTypeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    return (boolean) typeEqualsMethod.invoke(null, from, to, typeVarMap);
  }

  @Test
    @Timeout(8000)
  void testTypeEquals_rawTypeNotEquals_returnsFalse() throws Exception {
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    when(from.getRawType()).thenReturn(String.class);
    when(to.getRawType()).thenReturn(Integer.class);

    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testTypeEquals_rawTypeEquals_argumentsMatch_returnsTrue() throws Exception {
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    when(from.getRawType()).thenReturn(Map.class);
    when(to.getRawType()).thenReturn(Map.class);

    Type fromArg1 = String.class;
    Type fromArg2 = Integer.class;
    Type toArg1 = String.class;
    Type toArg2 = Integer.class;

    when(from.getActualTypeArguments()).thenReturn(new Type[] {fromArg1, fromArg2});
    when(to.getActualTypeArguments()).thenReturn(new Type[] {toArg1, toArg2});

    // matches is private static, so we need to spy or mock it indirectly
    // but matches is private static in TypeToken, so we cannot mock it easily
    // Instead, use real matches method via reflection or test with types that match exactly

    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testTypeEquals_rawTypeEquals_argumentsMismatch_returnsFalse() throws Exception {
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    when(from.getRawType()).thenReturn(Map.class);
    when(to.getRawType()).thenReturn(Map.class);

    Type fromArg1 = String.class;
    Type fromArg2 = Integer.class;
    Type toArg1 = String.class;
    Type toArg2 = Double.class;

    when(from.getActualTypeArguments()).thenReturn(new Type[] {fromArg1, fromArg2});
    when(to.getActualTypeArguments()).thenReturn(new Type[] {toArg1, toArg2});

    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testTypeEquals_emptyArguments_returnsTrue() throws Exception {
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    when(from.getRawType()).thenReturn(Map.class);
    when(to.getRawType()).thenReturn(Map.class);

    when(from.getActualTypeArguments()).thenReturn(new Type[0]);
    when(to.getActualTypeArguments()).thenReturn(new Type[0]);

    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertTrue(result);
  }
}