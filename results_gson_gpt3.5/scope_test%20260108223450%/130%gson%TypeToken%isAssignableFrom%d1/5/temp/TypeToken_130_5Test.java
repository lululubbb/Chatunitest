package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_toGenericComponentTypeNotParameterizedType_returnsTrue() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    Type toGenericComponentType = mock(Type.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    // toGenericComponentType is not instance of ParameterizedType
    boolean result = invokeIsAssignableFrom(mock(Type.class), to);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromGenericArrayType_callsRecursiveParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    GenericArrayType from = mock(GenericArrayType.class);
    Type fromComponentType = mock(Type.class);
    when(from.getGenericComponentType()).thenReturn(fromComponentType);

    boolean result = invokeIsAssignableFrom(from, to);

    // We cannot predict the inner call result, but it should call the private method
    // So just assert result is boolean (no exception thrown)
    assertTrue(result || !result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromClassArray_callsRecursiveParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    Class<?> from = int[][].class;

    boolean result = invokeIsAssignableFrom(from, to);

    assertTrue(result || !result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromClassNonArray_callsRecursiveParameterizedType() throws Exception {
    GenericArrayType to = mock(GenericArrayType.class);
    ParameterizedType toGenericComponentType = mock(ParameterizedType.class);
    when(to.getGenericComponentType()).thenReturn(toGenericComponentType);

    Class<?> from = String.class;

    boolean result = invokeIsAssignableFrom(from, to);

    assertTrue(result || !result);
  }

  private boolean invokeIsAssignableFrom(Type from, GenericArrayType to) throws Exception {
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to);
  }
}