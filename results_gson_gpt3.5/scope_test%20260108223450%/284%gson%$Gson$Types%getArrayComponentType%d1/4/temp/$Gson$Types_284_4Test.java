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

class $Gson$Types_284_4Test {

  @Test
    @Timeout(8000)
  void getArrayComponentType_WithGenericArrayType_ReturnsGenericComponentType() {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type expectedComponentType = String.class;
    when(genericArrayType.getGenericComponentType()).thenReturn(expectedComponentType);

    Type result = $Gson$Types.getArrayComponentType(genericArrayType);

    assertSame(expectedComponentType, result);
    verify(genericArrayType).getGenericComponentType();
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_WithClassArray_ReturnsComponentType() {
    Type arrayType = String[].class;

    Type result = $Gson$Types.getArrayComponentType(arrayType);

    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_WithClassNonArray_ReturnsNull() {
    Type nonArrayType = String.class;

    Type result = $Gson$Types.getArrayComponentType(nonArrayType);

    assertNull(result);
  }
}