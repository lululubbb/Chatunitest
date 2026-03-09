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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class GsonTypesCheckNotPrimitiveTest {

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonPrimitiveClass_doesNotThrow() throws Exception {
    Type type = String.class;
    assertDoesNotThrow(() -> {
      var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
      method.setAccessible(true);
      method.invoke(null, type);
    });
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withPrimitiveClass_throwsIllegalArgumentException() throws Exception {
    Type type = int.class;
    var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    method.setAccessible(true);
    assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, type);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonClassType_doesNotThrow() throws Exception {
    Type type = new Type() {};
    assertDoesNotThrow(() -> {
      var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
      method.setAccessible(true);
      method.invoke(null, type);
    });
  }
}