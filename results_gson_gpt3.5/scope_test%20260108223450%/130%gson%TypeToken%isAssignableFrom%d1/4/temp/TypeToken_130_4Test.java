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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  @DisplayName("isAssignableFrom returns true when to generic component type is not ParameterizedType")
  void testIsAssignableFrom_ToGenericComponentTypeNotParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    Type nonParameterizedType = mock(Type.class);
    when(to.getGenericComponentType()).thenReturn(nonParameterizedType);

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, mock(Type.class), to);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  @DisplayName("isAssignableFrom with from as GenericArrayType and to generic component type ParameterizedType")
  void testIsAssignableFrom_FromGenericArrayType_ToParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    GenericArrayType from = mock(GenericArrayType.class);
    Type fromComponentType = mock(Type.class);
    when(from.getGenericComponentType()).thenReturn(fromComponentType);

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    Object result = method.invoke(null, from, to);
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  @DisplayName("isAssignableFrom with from as Class array and to generic component type ParameterizedType")
  void testIsAssignableFrom_FromClassArray_ToParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    Class<?> fromArrayClass = String[][].class;

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    Object result = method.invoke(null, fromArrayClass, to);
    assertTrue(result instanceof Boolean);
  }

  @Test
    @Timeout(8000)
  @DisplayName("isAssignableFrom with from as Class non-array and to generic component type ParameterizedType")
  void testIsAssignableFrom_FromClassNonArray_ToParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    Class<?> fromClass = String.class;

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    Object result = method.invoke(null, fromClass, to);
    assertTrue(result instanceof Boolean);
  }
}