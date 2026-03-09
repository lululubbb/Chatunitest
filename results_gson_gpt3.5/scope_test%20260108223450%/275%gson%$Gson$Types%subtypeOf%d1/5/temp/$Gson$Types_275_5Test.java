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

class GsonTypes_SubtypeOfTest {

  @Test
    @Timeout(8000)
  void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBoundsAndEmptyLowerBounds() throws Exception {
    // Arrange: create a WildcardType mock with known upper bounds
    WildcardType mockWildcard = mock(WildcardType.class);
    Type[] upperBounds = new Type[] { String.class, Number.class };
    when(mockWildcard.getUpperBounds()).thenReturn(upperBounds);
    when(mockWildcard.getLowerBounds()).thenReturn(new Type[0]);

    // Act
    WildcardType result = $Gson$Types.subtypeOf(mockWildcard);

    // Assert
    assertNotNull(result);
    assertArrayEquals(upperBounds, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsUpperBoundAndEmptyLowerBounds() {
    // Arrange
    Type bound = Integer.class;

    // Act
    WildcardType result = $Gson$Types.subtypeOf(bound);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] { bound }, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }
}