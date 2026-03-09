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

class GsonTypesSupertypeOfTest {

  private static class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
      this.upperBounds = upperBounds.clone();
      this.lowerBounds = lowerBounds.clone();
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
      if (!(other instanceof WildcardType)) {
        return false;
      }
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
        sb.append(" super ").append(lowerBounds[0].getTypeName());
      } else if (upperBounds.length > 0 && !upperBounds[0].equals(Object.class)) {
        sb.append(" extends ").append(upperBounds[0].getTypeName());
      }
      return sb.toString();
    }
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_noLowerBounds_returnsWildcardWithObjectUpperBoundAndEmptyLowerBound() throws Exception {
    // Arrange
    WildcardType wildcard = mock(WildcardType.class);
    when(wildcard.getLowerBounds()).thenReturn(new Type[0]);
    when(wildcard.getUpperBounds()).thenReturn(new Type[] {Object.class});

    // Act
    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_withLowerBounds_returnsWildcardWithObjectUpperBoundAndGivenLowerBounds() throws Exception {
    // Arrange
    Type lowerBound = String.class;
    WildcardType wildcard = mock(WildcardType.class);
    when(wildcard.getLowerBounds()).thenReturn(new Type[] {lowerBound});
    when(wildcard.getUpperBounds()).thenReturn(new Type[] {Object.class});

    // Act
    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
    assertArrayEquals(new Type[] {lowerBound}, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNonWildcardType_returnsWildcardWithObjectUpperBoundAndGivenTypeAsLowerBound() throws Exception {
    // Arrange
    Type type = String.class;

    // Act
    WildcardType result = $Gson$Types.supertypeOf(type);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] {Object.class}, result.getUpperBounds());
    assertArrayEquals(new Type[] {type}, result.getLowerBounds());
  }
}