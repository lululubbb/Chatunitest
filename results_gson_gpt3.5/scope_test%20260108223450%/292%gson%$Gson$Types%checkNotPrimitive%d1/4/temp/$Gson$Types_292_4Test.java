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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class GsonTypesCheckNotPrimitiveTest {

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonPrimitiveClass_doesNotThrow() {
    // Non-primitive class type
    Type type = String.class;

    // Should not throw any exception
    $Gson$Types.checkNotPrimitive(type);
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withPrimitiveClass_throwsIllegalArgumentException() {
    Type type = int.class;

    // checkArgument throws IllegalArgumentException if condition is false
    assertThrows(IllegalArgumentException.class, () -> $Gson$Types.checkNotPrimitive(type));
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonClassType_doesNotThrow() {
    // Type that is not Class<?> (e.g., an anonymous Type implementation)
    Type type = new Type() {};

    // Should not throw any exception
    $Gson$Types.checkNotPrimitive(type);
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNull_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> $Gson$Types.checkNotPrimitive(null));
  }
}