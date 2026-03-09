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
  private Type mockType;

  @BeforeEach
  void setUp() {
    mockType = mock(Type.class);
    typeToken = new TypeToken<Object>() {
      {
        // Use reflection to set private fields
        setField(this, "type", mockType);
      }
    };
    otherTypeToken = new TypeToken<Object>() {
      {
        setField(this, "type", mockType);
      }
    };
  }

  @Test
    @Timeout(8000)
  void equals_sameInstance_returnsTrue() {
    assertTrue(typeToken.equals(typeToken));
  }

  @Test
    @Timeout(8000)
  void equals_null_returnsFalse() {
    assertFalse(typeToken.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_differentClass_returnsFalse() {
    assertFalse(typeToken.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void equals_otherTypeToken_sameType_returnsTrue() {
    // $Gson$Types.equals(type, type) returns true
    TypeToken<?> other = new TypeToken<Object>() {
      {
        setField(this, "type", mockType);
      }
    };
    assertTrue(typeToken.equals(other));
  }

  @Test
    @Timeout(8000)
  void equals_otherTypeToken_differentType_returnsFalse() {
    Type differentType = mock(Type.class);
    when(differentType.equals(mockType)).thenReturn(false);
    TypeToken<?> other = new TypeToken<Object>() {
      {
        setField(this, "type", differentType);
      }
    };

    // Mock $Gson$Types.equals to simulate false
    // Since $Gson$Types.equals is static, we cannot mock it with Mockito 3 easily.
    // Instead, override type field with different Type, equals should return false.
    assertFalse(typeToken.equals(other));
  }

  // Helper method to set private final fields via reflection
  private static void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = TypeToken.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}