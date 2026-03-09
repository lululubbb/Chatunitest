package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

class $Gson$Types_274_3Test {

  @Test
    @Timeout(8000)
  void arrayOf_withClassComponentType_returnsGenericArrayType() {
    Type componentType = String.class;
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(componentType);
    assertNotNull(genericArrayType);
    assertEquals(componentType, genericArrayType.getGenericComponentType());
    // The class of returned GenericArrayType is internal, test its toString and class type indirectly
    assertTrue(genericArrayType.toString().contains("java.lang.String[]"));
  }

  @Test
    @Timeout(8000)
  void arrayOf_withParameterizedTypeComponentType_returnsGenericArrayType() {
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return new Type[] {String.class}; }
      @Override public Type getRawType() { return java.util.Collection.class; }
      @Override public Type getOwnerType() { return null; }
      @Override public String toString() { return "java.util.Collection<java.lang.String>"; }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               java.util.Objects.equals(getRawType(), that.getRawType()) &&
               java.util.Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^
               (getRawType() == null ? 0 : getRawType().hashCode()) ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }
    };
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(parameterizedType);
    assertNotNull(genericArrayType);
    assertEquals(parameterizedType, genericArrayType.getGenericComponentType());
    assertTrue(genericArrayType.toString().contains("java.util.Collection<java.lang.String>[]"));
  }

  @Test
    @Timeout(8000)
  void arrayOf_withGenericArrayTypeComponentType_returnsGenericArrayType() {
    GenericArrayType innerGenericArrayType = $Gson$Types.arrayOf(String.class);
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(innerGenericArrayType);
    assertNotNull(genericArrayType);
    assertEquals(innerGenericArrayType, genericArrayType.getGenericComponentType());
    // toString should reflect nested array types
    String s = genericArrayType.toString();
    assertTrue(s.contains("java.lang.String[][]"));
  }

  @Test
    @Timeout(8000)
  void arrayOf_withTypeVariableComponentType_returnsGenericArrayType() throws Exception {
    // Create a TypeVariable from a dummy generic class
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(typeVariable);
    assertNotNull(genericArrayType);
    assertEquals(typeVariable, genericArrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void arrayOf_withWildcardTypeComponentType_returnsGenericArrayType() {
    WildcardType wildcardType = new WildcardType() {
      @Override public Type[] getUpperBounds() { return new Type[] {Number.class}; }
      @Override public Type[] getLowerBounds() { return new Type[] {}; }
      @Override public String toString() { return "? extends java.lang.Number"; }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WildcardType)) return false;
        WildcardType that = (WildcardType) o;
        return java.util.Arrays.equals(getUpperBounds(), that.getUpperBounds()) &&
               java.util.Arrays.equals(getLowerBounds(), that.getLowerBounds());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getUpperBounds()) ^ java.util.Arrays.hashCode(getLowerBounds());
      }
    };
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(wildcardType);
    assertNotNull(genericArrayType);
    assertEquals(wildcardType, genericArrayType.getGenericComponentType());
  }
}