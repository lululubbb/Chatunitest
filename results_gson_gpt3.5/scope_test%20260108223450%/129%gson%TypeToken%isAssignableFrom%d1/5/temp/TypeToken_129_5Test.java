package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_IsAssignableFromTest {

  private TypeToken<String> typeTokenString;
  private TypeToken<Integer> typeTokenInteger;
  private TypeToken<?> mockToken;

  @BeforeEach
  void setUp() {
    typeTokenString = new TypeToken<String>() {};
    typeTokenInteger = new TypeToken<Integer>() {};
    mockToken = mock(TypeToken.class);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_sameType() {
    when(mockToken.getType()).thenReturn(typeTokenString.getType());
    assertTrue(typeTokenString.isAssignableFrom(mockToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_subclassType() {
    TypeToken<Number> numberToken = new TypeToken<Number>() {};
    when(mockToken.getType()).thenReturn(typeTokenInteger.getType());
    assertTrue(numberToken.isAssignableFrom(mockToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_unrelatedType() {
    when(mockToken.getType()).thenReturn(typeTokenInteger.getType());
    assertFalse(typeTokenString.isAssignableFrom(mockToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullToken() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    method.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(typeTokenString, new Object[] { null }));
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }
}