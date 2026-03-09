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

class TypeTokenEqualsTest {

  private TypeToken<?> typeToken;
  private TypeToken<?> otherTypeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<String>() {};
    otherTypeToken = new TypeToken<String>() {};
  }

  @Test
    @Timeout(8000)
  void equals_SameInstance_ReturnsTrue() {
    assertTrue(typeToken.equals(typeToken));
  }

  @Test
    @Timeout(8000)
  void equals_Null_ReturnsFalse() {
    assertFalse(typeToken.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_DifferentClass_ReturnsFalse() {
    assertFalse(typeToken.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void equals_SameType_ReturnsTrue() {
    // Both typeTokens are TypeToken<String>
    assertTrue(typeToken.equals(otherTypeToken));
  }

  @Test
    @Timeout(8000)
  void equals_DifferentType_ReturnsFalse() {
    // Create a different TypeToken with different generic type
    TypeToken<Integer> intTypeToken = new TypeToken<Integer>() {};
    assertFalse(typeToken.equals(intTypeToken));
  }

  @Test
    @Timeout(8000)
  void equals_MockedTypeTokenWithDifferentType_ReturnsFalse() {
    TypeToken<?> mockToken = mock(TypeToken.class);
    when(mockToken.getType()).thenReturn((Type) Integer.class);
    // Delegate equals to real method in TypeToken class
    when(mockToken.equals(any())).thenCallRealMethod();

    assertFalse(typeToken.equals(mockToken));
  }

  @Test
    @Timeout(8000)
  void equals_MockedTypeTokenWithSameType_ReturnsTrue() {
    TypeToken<?> mockToken = mock(TypeToken.class);
    when(mockToken.getType()).thenReturn(typeToken.getType());
    // Delegate equals to real method in TypeToken class
    when(mockToken.equals(any())).thenCallRealMethod();

    assertTrue(typeToken.equals(mockToken));
  }
}