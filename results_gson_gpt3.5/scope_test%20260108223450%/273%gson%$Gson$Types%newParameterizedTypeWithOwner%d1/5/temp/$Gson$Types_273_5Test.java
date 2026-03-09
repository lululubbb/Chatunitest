package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class $Gson$Types_273_5Test {

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_givenOwnerAndRawTypeAndTypeArguments_returnsCorrectParameterizedType() {
    Type ownerType = String.class;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[] {Integer.class, Boolean.class};

    ParameterizedType parameterizedType =
        $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    assertEquals(ownerType, parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_givenNullOwnerAndRawTypeAndTypeArguments_returnsCorrectParameterizedType() {
    Type ownerType = null;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[] {Integer.class, Boolean.class};

    ParameterizedType parameterizedType =
        $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    assertNull(parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_givenEmptyTypeArguments_returnsCorrectParameterizedType() {
    Type ownerType = String.class;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[0];

    ParameterizedType parameterizedType =
        $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    assertEquals(ownerType, parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
  }
}