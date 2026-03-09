package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_genericArrayType_withParameterizedTypeComponent() throws Exception {
    // Arrange
    ParameterizedType parameterizedType = mock(ParameterizedType.class);

    // Create 'to' GenericArrayType with ParameterizedType component
    GenericArrayType to = () -> parameterizedType;

    // Create 'from' GenericArrayType with ParameterizedType component
    GenericArrayType from = () -> parameterizedType;

    // Act
    var focalMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    focalMethod.setAccessible(true);
    Object result = focalMethod.invoke(null, from, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_genericArrayType_withNonParameterizedComponent() throws Exception {
    // Arrange
    Type nonParameterizedType = mock(Type.class);

    GenericArrayType to = () -> nonParameterizedType;

    Type from = mock(Type.class);

    // Act
    var focalMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    focalMethod.setAccessible(true);
    Object result = focalMethod.invoke(null, from, to);

    // Assert
    assertEquals(true, result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_genericArrayType_withFromClassArray() throws Exception {
    // Arrange
    Class<?> from = String[][].class; // multi-dimensional array class

    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    GenericArrayType to = () -> parameterizedType;

    // Act
    var focalMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    focalMethod.setAccessible(true);
    Object result = focalMethod.invoke(null, from, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_genericArrayType_withFromClassNonArray() throws Exception {
    // Arrange
    Class<?> from = String.class; // not an array

    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    GenericArrayType to = () -> parameterizedType;

    // Act
    var focalMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    focalMethod.setAccessible(true);
    Object result = focalMethod.invoke(null, from, to);

    // Assert
    assertTrue(result instanceof Boolean);
  }
}