package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenEqualsTest {

  private TypeToken<String> typeTokenString;
  private TypeToken<Integer> typeTokenInteger;
  private TypeToken<String> typeTokenStringSpy;

  @BeforeEach
  void setUp() {
    typeTokenString = TypeToken.get(String.class);
    typeTokenInteger = TypeToken.get(Integer.class);
    typeTokenStringSpy = Mockito.spy(typeTokenString);
  }

  @Test
    @Timeout(8000)
  void equals_sameInstance_returnsTrue() {
    assertTrue(typeTokenString.equals(typeTokenString));
  }

  @Test
    @Timeout(8000)
  void equals_nullObject_returnsFalse() {
    assertFalse(typeTokenString.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_differentClass_returnsFalse() {
    assertFalse(typeTokenString.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void equals_differentTypeTokens_returnsFalse() {
    assertFalse(typeTokenString.equals(typeTokenInteger));
  }

  @Test
    @Timeout(8000)
  void equals_sameTypeTokens_returnsTrue() {
    TypeToken<String> anotherStringToken = TypeToken.get(String.class);
    assertTrue(typeTokenString.equals(anotherStringToken));
  }

  @Test
    @Timeout(8000)
  void equals_callsGsonTypesEquals() {
    TypeToken<String> other = TypeToken.get(String.class);
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.equals(any(Type.class), any(Type.class))).thenReturn(true);
      boolean result = typeTokenString.equals(other);
      assertTrue(result);
      mocked.verify(() -> $Gson$Types.equals(typeTokenString.getType(), other.getType()));
    }
  }

  @Test
    @Timeout(8000)
  void equals_callsGsonTypesEquals_returnsFalse() {
    TypeToken<String> other = TypeToken.get(String.class);
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.equals(any(Type.class), any(Type.class))).thenReturn(false);
      boolean result = typeTokenString.equals(other);
      assertFalse(result);
      mocked.verify(() -> $Gson$Types.equals(typeTokenString.getType(), other.getType()));
    }
  }

}