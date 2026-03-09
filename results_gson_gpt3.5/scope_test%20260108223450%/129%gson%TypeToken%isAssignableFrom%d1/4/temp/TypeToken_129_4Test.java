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

class TypeTokenIsAssignableFromTest {

  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullToken() {
    assertFalse(typeToken.isAssignableFrom((TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_sameTypeToken() {
    assertTrue(typeToken.isAssignableFrom(typeToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_assignableType() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    TypeToken<CharSequence> charSequenceToken = new TypeToken<CharSequence>() {};
    // CharSequence.class is assignable from String.class
    assertTrue(charSequenceToken.isAssignableFrom(stringToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_notAssignableType() {
    TypeToken<Integer> integerToken = new TypeToken<Integer>() {};
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertFalse(integerToken.isAssignableFrom(stringToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withMockedToken() {
    @SuppressWarnings("unchecked")
    TypeToken<Object> mockToken = mock(TypeToken.class);
    Type mockType = String.class;
    when(mockToken.getType()).thenReturn(mockType);

    TypeToken<CharSequence> charSequenceToken = new TypeToken<CharSequence>() {};
    assertTrue(charSequenceToken.isAssignableFrom(mockToken));
    verify(mockToken).getType();
  }

}