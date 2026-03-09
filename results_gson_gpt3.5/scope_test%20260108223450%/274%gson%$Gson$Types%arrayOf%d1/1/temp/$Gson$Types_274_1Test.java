package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

class $Gson$Types_274_1Test {

  @Test
    @Timeout(8000)
  void arrayOf_withValidComponentType_returnsGenericArrayTypeImpl() {
    Type componentType = String.class;

    GenericArrayType result = $Gson$Types.arrayOf(componentType);

    assertNotNull(result);
    assertEquals(componentType, result.getGenericComponentType());
    // The returned object should be an instance of GenericArrayType
    assertTrue(result instanceof GenericArrayType);
  }

  @Test
    @Timeout(8000)
  void arrayOf_withParameterizedTypeComponentType_returnsCorrectGenericArrayType() {
    Type parameterizedType = mock(Type.class);

    GenericArrayType result = $Gson$Types.arrayOf(parameterizedType);

    assertNotNull(result);
    assertEquals(parameterizedType, result.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void arrayOf_withNullComponentType_throwsNullPointerException() {
    Type componentType = null;

    assertThrows(NullPointerException.class, () -> {
      $Gson$Types.arrayOf(componentType);
    });
  }
}