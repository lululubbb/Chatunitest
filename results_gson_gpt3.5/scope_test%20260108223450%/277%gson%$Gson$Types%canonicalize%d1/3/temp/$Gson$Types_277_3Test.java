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

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;

class GsonTypesCanonicalizeTest {

  @Test
    @Timeout(8000)
  void canonicalize_withClassNonArray_returnsSameClass() {
    Class<String> stringClass = String.class;
    Type result = $Gson$Types.canonicalize(stringClass);
    assertSame(stringClass, result);
  }

  @Test
    @Timeout(8000)
  void canonicalize_withClassArray_returnsGenericArrayTypeImpl() {
    Class<String[]> arrayClass = String[].class;
    Type result = $Gson$Types.canonicalize(arrayClass);
    assertTrue(result instanceof GenericArrayType);
    GenericArrayType genericArrayType = (GenericArrayType) result;
    assertEquals(String.class, genericArrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void canonicalize_withParameterizedType_returnsParameterizedTypeImpl() {
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return new Type[]{String.class}; }
      @Override public Type getRawType() { return java.util.Collection.class; }
      @Override public Type getOwnerType() { return null; }
    };
    Type result = $Gson$Types.canonicalize(parameterizedType);
    assertNotNull(result);
    assertTrue(result instanceof ParameterizedType);
    ParameterizedType resultParameterizedType = (ParameterizedType) result;
    assertEquals(parameterizedType.getRawType(), resultParameterizedType.getRawType());
    assertArrayEquals(parameterizedType.getActualTypeArguments(), resultParameterizedType.getActualTypeArguments());
    assertEquals(parameterizedType.getOwnerType(), resultParameterizedType.getOwnerType());
  }

  @Test
    @Timeout(8000)
  void canonicalize_withGenericArrayType_returnsGenericArrayTypeImpl() {
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override public Type getGenericComponentType() { return String.class; }
    };
    Type result = $Gson$Types.canonicalize(genericArrayType);
    assertTrue(result instanceof GenericArrayType);
    GenericArrayType resultGenericArrayType = (GenericArrayType) result;
    assertEquals(String.class, resultGenericArrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void canonicalize_withWildcardType_returnsWildcardTypeImpl() {
    WildcardType wildcardType = new WildcardType() {
      @Override public Type[] getUpperBounds() { return new Type[]{Number.class}; }
      @Override public Type[] getLowerBounds() { return new Type[]{}; }
    };
    Type result = $Gson$Types.canonicalize(wildcardType);
    assertTrue(result instanceof WildcardType);
    WildcardType resultWildcard = (WildcardType) result;
    assertArrayEquals(wildcardType.getUpperBounds(), resultWildcard.getUpperBounds());
    assertArrayEquals(wildcardType.getLowerBounds(), resultWildcard.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void canonicalize_withOtherType_returnsSameType() {
    Type customType = new Type() {};
    Type result = $Gson$Types.canonicalize(customType);
    assertSame(customType, result);
  }
}