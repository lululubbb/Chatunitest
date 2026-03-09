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

class $Gson$Types_284_2Test {

  @Test
    @Timeout(8000)
  void getArrayComponentType_withGenericArrayType_returnsGenericComponentType() {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(genericArrayType.getGenericComponentType()).thenReturn(componentType);

    Type result = $Gson$Types.getArrayComponentType(genericArrayType);

    assertSame(componentType, result);
    verify(genericArrayType).getGenericComponentType();
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_withClassArray_returnsComponentType() {
    Class<?> arrayClass = String[].class;

    Type result = $Gson$Types.getArrayComponentType(arrayClass);

    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_withNonArrayClass_returnsNull() {
    Class<?> nonArrayClass = String.class;

    Type result = $Gson$Types.getArrayComponentType(nonArrayClass);

    assertNull(result);
  }
}