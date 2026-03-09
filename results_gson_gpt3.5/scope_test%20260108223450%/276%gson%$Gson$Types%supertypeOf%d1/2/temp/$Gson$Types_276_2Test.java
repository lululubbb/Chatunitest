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
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.ParameterizedType;

import org.junit.jupiter.api.Test;

class $Gson$Types_276_2Test {

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_returnsWildcardTypeWithLowerBounds() throws Exception {
    // Arrange
    WildcardType wildcard = mock(WildcardType.class);
    Type[] lowerBounds = new Type[] { String.class };
    when(wildcard.getLowerBounds()).thenReturn(lowerBounds);

    // Act
    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(lowerBounds, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsLowerBound() {
    // Arrange
    Type bound = Integer.class;

    // Act
    WildcardType result = $Gson$Types.supertypeOf(bound);

    // Assert
    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { bound }, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNull_throwsNullPointerException() {
    // Arrange
    Type bound = null;

    // Act & Assert
    assertThrows(NullPointerException.class, () -> $Gson$Types.supertypeOf(bound));
  }
}