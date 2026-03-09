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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.junit.jupiter.api.Test;

class GsonTypesSupertypeOfTest {

  @Test
    @Timeout(8000)
  void testSupertypeOf_withWildcardType_noLowerBounds() {
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
      @Override
      public String getTypeName() {
        return "? extends Number";
      }
    };
    WildcardType result = $Gson$Types.supertypeOf(wildcard);
    assertNotNull(result);
    // lower bounds should be wildcard lower bounds (empty)
    assertArrayEquals(new Type[0], result.getLowerBounds());
    // upper bounds should be Object.class
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
  }

  @Test
    @Timeout(8000)
  void testSupertypeOf_withWildcardType_withLowerBounds() {
    Type lowerBound = String.class;
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[] {lowerBound};
      }
      @Override
      public String getTypeName() {
        return "? super String";
      }
    };
    WildcardType result = $Gson$Types.supertypeOf(wildcard);
    assertNotNull(result);
    assertArrayEquals(new Type[] {lowerBound}, result.getLowerBounds());
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
  }

  @Test
    @Timeout(8000)
  void testSupertypeOf_withNonWildcardType() {
    Type type = Integer.class;
    WildcardType result = $Gson$Types.supertypeOf(type);
    assertNotNull(result);
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
    assertArrayEquals(new Type[] {type}, result.getLowerBounds());
  }
}