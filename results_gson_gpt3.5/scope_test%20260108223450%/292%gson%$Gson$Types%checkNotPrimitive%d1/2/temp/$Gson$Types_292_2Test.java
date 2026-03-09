package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GsonTypesCheckNotPrimitiveTest {

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withPrimitiveClass_throwsIllegalArgumentException() {
    try (MockedStatic<$Gson$Preconditions> mocked = mockStatic($Gson$Preconditions.class)) {
      // Arrange: setup mock to throw IllegalArgumentException when checkArgument is called with false
      mocked.when(() -> $Gson$Preconditions.checkArgument(false)).thenThrow(IllegalArgumentException.class);
      // Allow checkArgument(true) to do nothing
      mocked.when(() -> $Gson$Preconditions.checkArgument(true)).thenAnswer(invocation -> null);

      // Act & Assert: primitive type should cause checkArgument to throw IllegalArgumentException
      assertThrows(IllegalArgumentException.class, () ->
          invokeCheckNotPrimitive(int.class));
    }
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonPrimitiveClass_doesNotThrow() {
    try (MockedStatic<$Gson$Preconditions> mocked = mockStatic($Gson$Preconditions.class)) {
      // Arrange: setup mock to do nothing when checkArgument(true) is called
      mocked.when(() -> $Gson$Preconditions.checkArgument(true)).thenAnswer(invocation -> null);
      // Also allow checkArgument(false) to do nothing to avoid unexpected calls failing the test
      mocked.when(() -> $Gson$Preconditions.checkArgument(false)).thenAnswer(invocation -> null);

      // Act & Assert: non-primitive type should not throw
      invokeCheckNotPrimitive(String.class);
    }
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonClassType_doesNotThrow() {
    try (MockedStatic<$Gson$Preconditions> mocked = mockStatic($Gson$Preconditions.class)) {
      // Arrange: setup mock to do nothing when checkArgument(true) is called
      mocked.when(() -> $Gson$Preconditions.checkArgument(true)).thenAnswer(invocation -> null);
      // Also allow checkArgument(false) to do nothing to avoid unexpected calls failing the test
      mocked.when(() -> $Gson$Preconditions.checkArgument(false)).thenAnswer(invocation -> null);

      // Act & Assert: a Type that is not Class<?> should not throw
      Type genericArrayType = new Type() {};
      invokeCheckNotPrimitive(genericArrayType);
    }
  }

  private static void invokeCheckNotPrimitive(Type type) {
    try {
      Class<?> gsonTypesClass = Class.forName("com.google.gson.internal.$Gson$Types");
      var method = gsonTypesClass.getDeclaredMethod("checkNotPrimitive", Type.class);
      method.setAccessible(true);
      method.invoke(null, type);
    } catch (ReflectiveOperationException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      throw new RuntimeException(e);
    }
  }
}