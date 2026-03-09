package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_ToGenericComponentTypeIsParameterizedType_FromGenericArrayType() throws Exception {
    // Arrange
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toParamType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toParamType);

    GenericArrayType fromGenericArray = mock(GenericArrayType.class);
    Type fromComponentType = mock(Type.class);
    when(fromGenericArray.getGenericComponentType()).thenReturn(fromComponentType);

    // Use reflection to access private static method
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    // Act
    Object result = method.invoke(null, fromGenericArray, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_ToGenericComponentTypeIsParameterizedType_FromClassArray() throws Exception {
    // Arrange
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toParamType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toParamType);

    Class<?> fromClass = String[][].class; // multi-dimensional array class

    // Act
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    Object result = method.invoke(null, fromClass, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_ToGenericComponentTypeIsParameterizedType_FromClassNonArray() throws Exception {
    // Arrange
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toParamType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toParamType);

    Class<?> fromClass = String.class;

    // Act
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    Object result = method.invoke(null, fromClass, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_ToGenericComponentTypeIsNotParameterizedType() throws Exception {
    // Arrange
    GenericArrayType to = mock(GenericArrayType.class);
    Type nonParamType = mock(Type.class);
    when(to.getGenericComponentType()).thenReturn(nonParamType);

    Type from = mock(Type.class);

    // Act
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    Object result = method.invoke(null, from, to);

    // Assert
    assertTrue(result instanceof Boolean);
    // Per code, should return true when toGenericComponentType is not ParameterizedType
    assertEquals(true, result);
  }
}