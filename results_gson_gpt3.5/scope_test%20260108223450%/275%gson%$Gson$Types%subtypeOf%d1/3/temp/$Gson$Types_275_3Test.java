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

class GsonTypes_SubtypeOfTest {

  // Helper class to implement WildcardType for testing
  static class TestWildcardType implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    TestWildcardType(Type[] upperBounds, Type[] lowerBounds) {
      this.upperBounds = upperBounds != null ? upperBounds.clone() : new Type[0];
      this.lowerBounds = lowerBounds != null ? lowerBounds.clone() : new Type[0];
    }

    @Override
    public Type[] getUpperBounds() {
      return upperBounds.clone();
    }

    @Override
    public Type[] getLowerBounds() {
      return lowerBounds.clone();
    }
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBounds() {
    Type[] upperBounds = new Type[] {String.class, Number.class};
    WildcardType testWildcard = new TestWildcardType(upperBounds, new Type[0]);

    WildcardType result = $Gson$Types.subtypeOf(testWildcard);

    assertNotNull(result);
    assertArrayEquals(upperBounds, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsUpperBound() {
    Type bound = Integer.class;

    WildcardType result = $Gson$Types.subtypeOf(bound);

    assertNotNull(result);
    assertArrayEquals(new Type[] {bound}, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }
}