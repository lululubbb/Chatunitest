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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class TypeToken_typeEquals_Test {

  private Method typeEqualsMethod;

  @BeforeEach
  public void setup() throws NoSuchMethodException {
    typeEqualsMethod = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    typeEqualsMethod.setAccessible(true);
  }

  @SuppressWarnings("unchecked")
  private boolean invokeTypeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) throws InvocationTargetException, IllegalAccessException {
    return (boolean) typeEqualsMethod.invoke(null, from, to, typeVarMap);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_sameRawType_allArgumentsMatch() throws Exception {
    // Arrange
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    Class<?> rawType = Map.class; // Use a type with type parameters
    when(from.getRawType()).thenReturn(rawType);
    when(to.getRawType()).thenReturn(rawType);

    Type fromArg1 = String.class;
    Type toArg1 = String.class;
    Type[] fromArgs = new Type[] {fromArg1};
    Type[] toArgs = new Type[] {toArg1};
    when(from.getActualTypeArguments()).thenReturn(fromArgs);
    when(to.getActualTypeArguments()).thenReturn(toArgs);

    // Act
    boolean result = invokeTypeEquals(from, to, typeVarMap);

    // Assert
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_sameRawType_argumentMismatch() throws Exception {
    // Arrange
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    Class<?> rawType = Map.class; // Use a type with type parameters
    when(from.getRawType()).thenReturn(rawType);
    when(to.getRawType()).thenReturn(rawType);

    Type fromArg1 = String.class;
    Type toArg1 = Integer.class;
    Type[] fromArgs = new Type[] {fromArg1};
    Type[] toArgs = new Type[] {toArg1};
    when(from.getActualTypeArguments()).thenReturn(fromArgs);
    when(to.getActualTypeArguments()).thenReturn(toArgs);

    // Act
    boolean result = invokeTypeEquals(from, to, typeVarMap);

    // Assert
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_differentRawType() throws Exception {
    // Arrange
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    Class<?> fromRawType = Map.class;
    Class<?> toRawType = String.class;
    when(from.getRawType()).thenReturn(fromRawType);
    when(to.getRawType()).thenReturn(toRawType);

    // Act
    boolean result = invokeTypeEquals(from, to, typeVarMap);

    // Assert
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_multipleArguments_allMatch() throws Exception {
    // Arrange
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    Class<?> rawType = Map.class;
    when(from.getRawType()).thenReturn(rawType);
    when(to.getRawType()).thenReturn(rawType);

    Type fromArg1 = String.class;
    Type fromArg2 = Integer.class;
    Type toArg1 = String.class;
    Type toArg2 = Integer.class;

    Type[] fromArgs = new Type[] {fromArg1, fromArg2};
    Type[] toArgs = new Type[] {toArg1, toArg2};
    when(from.getActualTypeArguments()).thenReturn(fromArgs);
    when(to.getActualTypeArguments()).thenReturn(toArgs);

    // Act
    boolean result = invokeTypeEquals(from, to, typeVarMap);

    // Assert
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_multipleArguments_oneMismatch() throws Exception {
    // Arrange
    ParameterizedType from = mock(ParameterizedType.class);
    ParameterizedType to = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();

    Class<?> rawType = Map.class;
    when(from.getRawType()).thenReturn(rawType);
    when(to.getRawType()).thenReturn(rawType);

    Type fromArg1 = String.class;
    Type fromArg2 = Integer.class;
    Type toArg1 = String.class;
    Type toArg2 = Double.class;

    Type[] fromArgs = new Type[] {fromArg1, fromArg2};
    Type[] toArgs = new Type[] {toArg1, toArg2};
    when(from.getActualTypeArguments()).thenReturn(fromArgs);
    when(to.getActualTypeArguments()).thenReturn(toArgs);

    // Act
    boolean result = invokeTypeEquals(from, to, typeVarMap);

    // Assert
    assertFalse(result);
  }
}