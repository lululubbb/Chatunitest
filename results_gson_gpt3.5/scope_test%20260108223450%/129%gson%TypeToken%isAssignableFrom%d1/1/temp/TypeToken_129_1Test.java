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

import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeToken_isAssignableFrom_Test {

  private TypeToken<?> typeTokenMock;

  @BeforeEach
  void setUp() {
    typeTokenMock = mock(TypeToken.class, Mockito.CALLS_REAL_METHODS);
    // Set a non-null type for typeTokenMock to avoid NullPointerException in isAssignableFrom
    doReturn(String.class).when(typeTokenMock).getType();
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullToken_returnsFalse() {
    assertFalse(typeTokenMock.isAssignableFrom((TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_tokenWithNullType_returnsFalse() {
    TypeToken<?> token = mock(TypeToken.class);
    when(token.getType()).thenReturn(null);
    assertFalse(typeTokenMock.isAssignableFrom(token));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_delegatesToIsAssignableFromType() {
    TypeToken<?> token = mock(TypeToken.class);
    Type type = String.class;
    when(token.getType()).thenReturn(type);

    TypeToken<?> spyTypeToken = spy(typeTokenMock);
    doReturn(true).when(spyTypeToken).isAssignableFrom(type);

    boolean result = spyTypeToken.isAssignableFrom(token);

    assertTrue(result);
    verify(spyTypeToken).isAssignableFrom(type);
  }
}