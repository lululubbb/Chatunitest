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
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class $Gson$Types_276_3Test {

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardTypeWithoutLowerBounds_returnsWildcardWithObjectAsLowerBound() {
    WildcardType wildcard = mock(WildcardType.class);
    when(wildcard.getLowerBounds()).thenReturn(new Type[0]);

    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withWildcardTypeWithLowerBounds_returnsWildcardWithObjectUpperBoundAndGivenLowerBounds() {
    Type lowerBound = String.class;
    WildcardType wildcard = mock(WildcardType.class);
    when(wildcard.getLowerBounds()).thenReturn(new Type[] { lowerBound });

    WildcardType result = $Gson$Types.supertypeOf(wildcard);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { lowerBound }, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_withNonWildcardType_returnsWildcardWithObjectUpperBoundAndGivenTypeAsLowerBound() {
    Type bound = Integer.class;

    WildcardType result = $Gson$Types.supertypeOf(bound);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { bound }, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_reflectivelyInvoked_withWildcardTypeWithLowerBounds_returnsCorrectWildcard() throws Exception {
    Type lowerBound = Number.class;
    WildcardType wildcard = mock(WildcardType.class);
    when(wildcard.getLowerBounds()).thenReturn(new Type[] { lowerBound });

    Method method = $Gson$Types.class.getDeclaredMethod("supertypeOf", Type.class);
    method.setAccessible(true);

    WildcardType result = (WildcardType) method.invoke(null, wildcard);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { lowerBound }, result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_reflectivelyInvoked_withNonWildcardType_returnsCorrectWildcard() throws Exception {
    Type bound = Double.class;

    Method method = $Gson$Types.class.getDeclaredMethod("supertypeOf", Type.class);
    method.setAccessible(true);

    WildcardType result = (WildcardType) method.invoke(null, bound);

    assertNotNull(result);
    assertArrayEquals(new Type[] { Object.class }, result.getUpperBounds());
    assertArrayEquals(new Type[] { bound }, result.getLowerBounds());
  }
}