package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
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

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;

class $Gson$Types_292_1Test {

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonPrimitiveClass_doesNotThrow() {
    assertDoesNotThrow(() -> {
      $Gson$Types.checkNotPrimitive(String.class);
    });
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withPrimitiveClass_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.checkNotPrimitive(int.class);
    });
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonClassType_doesNotThrow() {
    Type type = mock(Type.class);
    assertDoesNotThrow(() -> {
      $Gson$Types.checkNotPrimitive(type);
    });
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_invokesCheckArgument() {
    try (MockedStatic<$Gson$Preconditions> mockedPreconditions = mockStatic($Gson$Preconditions.class)) {
      mockedPreconditions.when(() -> $Gson$Preconditions.checkArgument(true)).thenAnswer(i -> null);
      $Gson$Types.checkNotPrimitive(String.class);
      mockedPreconditions.verify(() -> $Gson$Preconditions.checkArgument(true));
    }
  }
}