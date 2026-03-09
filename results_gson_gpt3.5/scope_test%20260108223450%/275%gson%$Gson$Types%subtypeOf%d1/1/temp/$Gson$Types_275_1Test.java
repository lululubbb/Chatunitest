package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

class $Gson$Types_275_1Test {

  @Test
    @Timeout(8000)
  void subtypeOf_withNonWildcardType_returnsWildcardTypeWithUpperBound() {
    Type bound = String.class;
    WildcardType result = $Gson$Types.subtypeOf(bound);

    assertNotNull(result);
    Type[] upperBounds = result.getUpperBounds();
    Type[] lowerBounds = result.getLowerBounds();

    assertEquals(1, upperBounds.length);
    assertEquals(bound, upperBounds[0]);
    assertEquals(0, lowerBounds.length);
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBounds() throws Exception {
    // Create a WildcardType with upper bounds Number.class
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] { Number.class };
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };

    WildcardType result = $Gson$Types.subtypeOf(wildcard);

    assertNotNull(result);
    Type[] upperBounds = result.getUpperBounds();
    Type[] lowerBounds = result.getLowerBounds();

    assertEquals(1, upperBounds.length);
    assertEquals(Number.class, upperBounds[0]);
    assertEquals(0, lowerBounds.length);
  }
}