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
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GsonTypesCheckNotPrimitiveTest {

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withPrimitiveClass_throwsIllegalArgumentException() {
    try (MockedStatic<$Gson$Preconditions> mockedPreconditions = mockStatic($Gson$Preconditions.class)) {
      mockedPreconditions.when(() -> $Gson$Preconditions.checkArgument(false))
          .thenThrow(new IllegalArgumentException());

      Class<?> primitiveType = int.class;
      assertThrows(IllegalArgumentException.class, () -> {
        // invoke private static method via reflection
        var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
        method.setAccessible(true);
        method.invoke(null, primitiveType);
      });
    }
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonPrimitiveClass_noException() throws Exception {
    Class<?> nonPrimitiveType = String.class;
    var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    method.setAccessible(true);
    // Should not throw
    method.invoke(null, nonPrimitiveType);
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_withNonClassType_noException() throws Exception {
    Type type = new Type() {};
    var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    method.setAccessible(true);
    // Should not throw
    method.invoke(null, type);
  }
}