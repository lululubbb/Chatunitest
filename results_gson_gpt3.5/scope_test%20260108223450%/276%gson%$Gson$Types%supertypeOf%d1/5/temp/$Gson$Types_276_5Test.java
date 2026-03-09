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

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import org.junit.jupiter.api.Test;

class GsonTypesSupertypeOfTest {

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardType_returnsWildcardTypeWithLowerBounds() throws Exception {
    WildcardType mockWildcard = mock(WildcardType.class);
    Type[] lowerBounds = new Type[] { String.class };
    when(mockWildcard.getLowerBounds()).thenReturn(lowerBounds);

    WildcardType result = $Gson$Types.supertypeOf(mockWildcard);

    assertNotNull(result);
    // The upper bound should be Object.class
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    // The lower bounds should be the same as the mocked lower bounds
    assertArrayEquals(lowerBounds, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNonWildcardType_returnsWildcardTypeWithLowerBoundOfType() throws Exception {
    Type type = Integer.class;

    WildcardType result = $Gson$Types.supertypeOf(type);

    assertNotNull(result);
    // The upper bound should be Object.class
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    // The lower bound should be the single element array containing the input type
    assertArrayEquals(new Type[] { type }, result.getLowerBounds());
  }
}