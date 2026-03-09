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

class $Gson$Types_278_2Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClass() {
    Class<?> input = String.class;
    Class<?> result = $Gson$Types.getRawType(input);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() throws Exception {
    ParameterizedType mockParameterizedType = mock(ParameterizedType.class);
    when(mockParameterizedType.getRawType()).thenReturn(String.class);

    Class<?> result = $Gson$Types.getRawType(mockParameterizedType);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType_rawTypeNotClass_throws() {
    ParameterizedType mockParameterizedType = mock(ParameterizedType.class);
    // Return a Type that is not a Class
    Type nonClassType = new Type() {};
    when(mockParameterizedType.getRawType()).thenReturn(nonClassType);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(mockParameterizedType);
    });
    assertTrue(ex.getMessage().contains("Expected a Class"));
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withGenericArrayType() throws Exception {
    GenericArrayType mockGenericArrayType = mock(GenericArrayType.class);

    // Create a GenericArrayType with component type String.class
    when(mockGenericArrayType.getGenericComponentType()).thenReturn(String.class);

    Class<?> result = $Gson$Types.getRawType(mockGenericArrayType);
    assertTrue(result.isArray());
    assertEquals(String.class, result.getComponentType());
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withTypeVariable() throws Exception {
    TypeVariable<?> mockTypeVariable = mock(TypeVariable.class);
    Class<?> result = $Gson$Types.getRawType(mockTypeVariable);
    assertSame(Object.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withWildcardType() throws Exception {
    WildcardType mockWildcardType = mock(WildcardType.class);
    Type[] upperBounds = new Type[] {Number.class};
    when(mockWildcardType.getUpperBounds()).thenReturn(upperBounds);

    Class<?> result = $Gson$Types.getRawType(mockWildcardType);
    assertSame(Number.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withWildcardType_multipleBounds_assertionError() throws Exception {
    WildcardType mockWildcardType = mock(WildcardType.class);
    Type[] upperBounds = new Type[] {Number.class, String.class};
    when(mockWildcardType.getUpperBounds()).thenReturn(upperBounds);

    AssertionError error = assertThrows(AssertionError.class, () -> {
      $Gson$Types.getRawType(mockWildcardType);
    });
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withNull_throws() {
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(null);
    });
    assertTrue(ex.getMessage().contains("Expected a Class"));
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withUnsupportedType_throws() {
    Type unsupportedType = new Type() {};
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(unsupportedType);
    });
    assertTrue(ex.getMessage().contains("Expected a Class"));
  }
}