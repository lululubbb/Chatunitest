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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

class GsonTypesTest {

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_basic() {
    Type ownerType = null; // Map is a top-level class, so ownerType must be null
    Type rawType = Map.class;
    Type[] typeArguments = new Type[] {Integer.class, Boolean.class};

    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(pt);
    assertEquals(ownerType, pt.getOwnerType());
    assertEquals(rawType, pt.getRawType());
    assertArrayEquals(typeArguments, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_nullOwnerType() {
    Type ownerType = null;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[] {String.class, Integer.class};

    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(pt);
    assertNull(pt.getOwnerType());
    assertEquals(rawType, pt.getRawType());
    assertArrayEquals(typeArguments, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_emptyTypeArguments() {
    Type ownerType = null; // Map is top-level, so ownerType must be null
    Type rawType = Map.class;
    Type[] typeArguments = new Type[0];

    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(pt);
    assertEquals(ownerType, pt.getOwnerType());
    assertEquals(rawType, pt.getRawType());
    assertArrayEquals(typeArguments, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_varargs() {
    Type ownerType = null; // Map is top-level, so ownerType must be null
    Type rawType = Map.class;

    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, Integer.class, Boolean.class);

    assertNotNull(pt);
    assertEquals(ownerType, pt.getOwnerType());
    assertEquals(rawType, pt.getRawType());
    assertArrayEquals(new Type[] {Integer.class, Boolean.class}, pt.getActualTypeArguments());
  }
}