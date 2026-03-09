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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class GsonTypesGetSupertypeTest {

  @Test
    @Timeout(8000)
  void testGetSupertype_withWildcardType() throws Exception {
    // Arrange
    WildcardType wildcardType = mock(WildcardType.class);
    Type upperBound = Object.class; // Use Object.class because supertype is Object.class and isAssignableFrom check requires it
    when(wildcardType.getUpperBounds()).thenReturn(new Type[]{upperBound});
    Class<?> contextRawType = Object.class;
    Class<?> supertype = Object.class;

    // Use reflection to access private static method getSupertype
    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    // Act
    Type result = (Type) method.invoke(null, wildcardType, contextRawType, supertype);

    // Assert
    assertNotNull(result);
    assertEquals(Object.class, $Gson$Types.getRawType(result));
  }

  @Test
    @Timeout(8000)
  void testGetSupertype_withAssignableContextRawType() throws Exception {
    // Arrange
    Class<?> contextRawType = Integer.class;
    Class<?> supertype = Number.class;
    Type context = Integer.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    // Act
    Type result = (Type) method.invoke(null, context, contextRawType, supertype);

    // Assert
    assertNotNull(result);
    assertEquals(Number.class, $Gson$Types.getRawType(result));
  }

  @Test
    @Timeout(8000)
  void testGetSupertype_withNonWildcardType() throws Exception {
    // Arrange
    Class<?> contextRawType = Integer.class;
    Class<?> supertype = Number.class;
    Type context = Integer.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    // Act
    Type result = (Type) method.invoke(null, context, contextRawType, supertype);

    // Assert
    assertNotNull(result);
    assertEquals(Number.class, $Gson$Types.getRawType(result));
  }

  @Test
    @Timeout(8000)
  void testGetSupertype_withAssignableFromFalse_shouldThrow() throws Exception {
    // Arrange
    Class<?> contextRawType = String.class;
    Class<?> supertype = Number.class;
    Type context = String.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    // Act & Assert
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, context, contextRawType, supertype);
    });
    // InvocationTargetException wraps the IllegalArgumentException, unwrap it
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }
}