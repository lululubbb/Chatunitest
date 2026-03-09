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

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import org.junit.jupiter.api.Test;

class GsonTypesSupertypeOfTest {

  static class WildcardTypeImpl implements WildcardType {
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
      if (lowerBounds.length > 0) {
        return "? super " + lowerBounds[0].getTypeName();
      } else if (upperBounds.length == 1 && upperBounds[0] == Object.class) {
        return "?";
      } else {
        return "? extends " + upperBounds[0].getTypeName();
      }
    }
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_returnsWildcardWithLowerBounds() throws Exception {
    Type[] lowerBounds = new Type[] { String.class };
    WildcardType wildcard = new WildcardTypeImpl(new Type[] { Object.class }, lowerBounds);

    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(lowerBounds, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNonWildcardType_returnsWildcardWithLowerBoundsContainingBound() {
    Type bound = Number.class;

    WildcardType result = $Gson$Types.supertypeOf(bound);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { bound }, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_noLowerBounds_returnsWildcardWithEmptyLowerBounds() throws Exception {
    Type[] emptyLowerBounds = new Type[0];
    WildcardType wildcard = new WildcardTypeImpl(new Type[] { Object.class }, emptyLowerBounds);

    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(emptyLowerBounds, result.getLowerBounds());
  }
}