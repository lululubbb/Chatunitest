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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

class GsonTypesSubtypeOfTest {

  private static WildcardType createWildcardType(Type[] upperBounds, Type[] lowerBounds) {
    try {
      Class<?> wildcardTypeImplClass = Class.forName("com.google.gson.internal.$Gson$Types$WildcardTypeImpl");
      Constructor<?> constructor = wildcardTypeImplClass.getDeclaredConstructor(Type[].class, Type[].class);
      constructor.setAccessible(true);
      return (WildcardType) constructor.newInstance((Object) upperBounds, (Object) lowerBounds);
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBounds() throws Exception {
    WildcardType wildcard = createWildcardType(new Type[] { String.class }, new Type[0]);

    WildcardType result = $Gson$Types.subtypeOf(wildcard);

    assertNotNull(result);
    assertArrayEquals(wildcard.getUpperBounds(), result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void subtypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsUpperBound() {
    Type bound = Number.class;

    WildcardType result = $Gson$Types.subtypeOf(bound);

    assertNotNull(result);
    assertArrayEquals(new Type[] { bound }, result.getUpperBounds());
    assertArrayEquals(new Type[0], result.getLowerBounds());
  }

}