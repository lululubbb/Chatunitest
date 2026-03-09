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
import java.lang.reflect.Type;
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

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

class GsonTypesCanonicalizeTest {

  @Test
    @Timeout(8000)
  public void testCanonicalize_withClass_nonArray() {
    Type input = String.class;
    Type result = $Gson$Types.canonicalize(input);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withClass_array() {
    Class<?> arrayClass = String[].class;
    Type result = $Gson$Types.canonicalize(arrayClass);
    assertTrue(result instanceof GenericArrayType);
    GenericArrayType gat = (GenericArrayType) result;
    Type componentType = gat.getGenericComponentType();
    assertSame(String.class, componentType);
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withParameterizedType() {
    ParameterizedType pType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{Integer.class, String.class};
      }

      @Override
      public Type getRawType() {
        return java.util.Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    Type result = $Gson$Types.canonicalize(pType);
    assertNotNull(result);
    assertTrue(result instanceof ParameterizedType);

    ParameterizedType resultPType = (ParameterizedType) result;
    assertEquals(pType.getOwnerType(), resultPType.getOwnerType());
    assertEquals(pType.getRawType(), resultPType.getRawType());
    assertArrayEquals(pType.getActualTypeArguments(), resultPType.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withGenericArrayType() {
    GenericArrayType gArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return Integer.class;
      }
    };

    Type result = $Gson$Types.canonicalize(gArrayType);
    assertNotNull(result);
    assertTrue(result instanceof GenericArrayType);

    GenericArrayType resultGAT = (GenericArrayType) result;
    assertEquals(gArrayType.getGenericComponentType(), resultGAT.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withWildcardType() {
    WildcardType wType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[]{Number.class};
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[]{Integer.class};
      }
    };

    Type result = $Gson$Types.canonicalize(wType);
    assertNotNull(result);
    assertTrue(result instanceof WildcardType);

    WildcardType resultWType = (WildcardType) result;
    assertArrayEquals(wType.getUpperBounds(), resultWType.getUpperBounds());
    assertArrayEquals(wType.getLowerBounds(), resultWType.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withOtherType() {
    // Use a custom Type implementation for unsupported type
    Type customType = new Type() {};
    Type result = $Gson$Types.canonicalize(customType);
    assertSame(customType, result);
  }
}