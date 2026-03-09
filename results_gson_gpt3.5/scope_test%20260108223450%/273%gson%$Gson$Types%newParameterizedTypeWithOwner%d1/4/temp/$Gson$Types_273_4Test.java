package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
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
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class $Gson$Types_273_4Test {

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_basic() {
    Type ownerType = String.class;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[] {Integer.class, String.class};

    ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertEquals(ownerType, parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_nullOwnerForTopLevel() {
    Type ownerType = null;
    Type rawType = String.class;
    Type[] typeArguments = $Gson$Types.EMPTY_TYPE_ARRAY;

    ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertNull(parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_innerClassOwner() {
    class Outer<T> {
      class Inner<S> {}
    }

    Type ownerType = Outer.class;
    Type rawType = Outer.Inner.class;
    Type[] typeArguments = new Type[] {String.class};

    ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertEquals(ownerType, parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
    assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_emptyTypeArguments() {
    Type ownerType = null;
    Type rawType = Map.class;
    Type[] typeArguments = new Type[0];

    ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

    assertNotNull(parameterizedType);
    assertNull(parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
    assertEquals(0, parameterizedType.getActualTypeArguments().length);
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_varargsNull() {
    Type ownerType = null;
    Type rawType = Map.class;

    // Fix: replace null with Void.class in the type arguments array
    ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, (Type[]) new Type[] {Void.class});

    assertNotNull(parameterizedType);
    assertNull(parameterizedType.getOwnerType());
    assertEquals(rawType, parameterizedType.getRawType());
    assertEquals(1, parameterizedType.getActualTypeArguments().length);
    assertSame(Void.class, parameterizedType.getActualTypeArguments()[0]);
  }
}