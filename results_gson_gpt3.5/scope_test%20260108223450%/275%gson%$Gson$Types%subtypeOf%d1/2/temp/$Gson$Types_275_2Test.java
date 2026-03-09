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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import org.junit.jupiter.api.Test;

class GsonTypesSubtypeOfTest {

  // Helper class to implement WildcardType for testing
  static class TestWildcardType implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    TestWildcardType(Type[] upperBounds, Type[] lowerBounds) {
      this.upperBounds = upperBounds != null ? upperBounds : new Type[0];
      this.lowerBounds = lowerBounds != null ? lowerBounds : new Type[0];
    }

    @Override
    public Type[] getUpperBounds() {
      return upperBounds.clone();
    }

    @Override
    public Type[] getLowerBounds() {
      return lowerBounds.clone();
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof WildcardType)) return false;
      WildcardType that = (WildcardType) other;
      return java.util.Arrays.equals(upperBounds, that.getUpperBounds())
          && java.util.Arrays.equals(lowerBounds, that.getLowerBounds());
    }

    @Override
    public int hashCode() {
      return java.util.Arrays.hashCode(upperBounds) ^ java.util.Arrays.hashCode(lowerBounds);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("?");
      if (lowerBounds.length > 0) {
        for (Type t : lowerBounds) {
          sb.append(" super ").append(t.getTypeName());
        }
      } else if (upperBounds.length > 0 && !(upperBounds.length == 1 && upperBounds[0] == Object.class)) {
        for (Type t : upperBounds) {
          sb.append(" extends ").append(t.getTypeName());
        }
      }
      return sb.toString();
    }
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBounds() throws Exception {
    // Arrange
    Type[] upperBounds = new Type[] {String.class};
    Type[] lowerBounds = new Type[0];
    WildcardType testWildcardType = new TestWildcardType(upperBounds, lowerBounds);

    // Act
    WildcardType result = $Gson$Types.subtypeOf(testWildcardType);

    // Assert
    assertNotNull(result);
    assertArrayEquals(upperBounds, result.getUpperBounds());
    assertArrayEquals(lowerBounds, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsUpperBound() {
    // Arrange
    Type bound = Integer.class;

    // Act
    WildcardType result = $Gson$Types.subtypeOf(bound);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] {bound}, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }
}