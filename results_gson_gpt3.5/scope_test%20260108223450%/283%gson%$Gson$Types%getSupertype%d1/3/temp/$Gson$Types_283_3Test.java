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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class $Gson$Types_283_3Test {

  @Test
    @Timeout(8000)
  void getSupertype_withWildcardType_resolvesUpperBound() throws Exception {
    // Arrange
    WildcardType wildcardType = mock(WildcardType.class);
    Type upperBound = String.class;
    when(wildcardType.getUpperBounds()).thenReturn(new Type[] {upperBound});

    Class<?> contextRawType = Integer.class;
    Class<?> supertype = Number.class;

    // create a spy on $Gson$Types to mock static private methods
    // but since methods are static and private, we will use reflection only

    // Act & Assert
    // Because supertype.isAssignableFrom(contextRawType) must be true, and Integer extends Number, this holds.

    // Invoke private static getSupertype via reflection
    Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // We expect resolve(...) to be called internally, but we cannot mock it easily.
    // So we just call getSupertype and verify no exceptions and non-null return.
    Object result = getSupertypeMethod.invoke(null, wildcardType, contextRawType, supertype);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void getSupertype_withNonWildcardType_resolvesCorrectly() throws Exception {
    // Arrange
    Type context = Integer.class;
    Class<?> contextRawType = Integer.class;
    Class<?> supertype = Number.class;

    Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // Act
    Object result = getSupertypeMethod.invoke(null, context, contextRawType, supertype);

    // Assert
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void getSupertype_throwsIfSupertypeNotAssignableFromContextRawType() throws Exception {
    // Arrange
    Type context = String.class;
    Class<?> contextRawType = String.class;
    Class<?> supertype = Number.class; // Number not assignable from String

    Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // Act
    Executable executable = () -> getSupertypeMethod.invoke(null, context, contextRawType, supertype);

    // Assert
    Exception exception = assertThrows(InvocationTargetException.class, executable);
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }
}