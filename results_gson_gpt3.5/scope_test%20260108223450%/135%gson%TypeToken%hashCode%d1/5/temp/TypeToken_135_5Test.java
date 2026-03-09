package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TypeToken_HashCodeTest {

  @Test
    @Timeout(8000)
  void testHashCode_returnsPrecomputedHashCode() throws Exception {
    // Arrange
    TypeToken<?> typeToken = mock(TypeToken.class, invocation -> {
      if ("hashCode".equals(invocation.getMethod().getName())) {
        return invocation.callRealMethod();
      }
      return invocation.callRealMethod();
    });

    // Use reflection to set private final int hashCode field
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int expectedHashCode = 123456789;
    hashCodeField.set(typeToken, expectedHashCode);

    // Act
    int actualHashCode = typeToken.hashCode();

    // Assert
    assertEquals(expectedHashCode, actualHashCode);
  }
}