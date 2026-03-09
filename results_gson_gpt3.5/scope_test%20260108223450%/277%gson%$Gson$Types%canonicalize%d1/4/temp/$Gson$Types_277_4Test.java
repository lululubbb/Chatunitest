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
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;

public class $Gson$Types_277_4Test {

  private static Class<?> getPrivateInnerClass(String simpleName) throws ClassNotFoundException {
    for (Class<?> inner : $Gson$Types.class.getDeclaredClasses()) {
      if (inner.getSimpleName().equals(simpleName)) {
        return inner;
      }
    }
    throw new ClassNotFoundException("Inner class " + simpleName + " not found");
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withClassArray_returnsGenericArrayTypeImpl() throws Exception {
    Class<?> arrayClass = String[].class;
    Type result = $Gson$Types.canonicalize(arrayClass);
    assertNotNull(result);

    Class<?> genericArrayTypeImplClass = getPrivateInnerClass("GenericArrayTypeImpl");
    assertTrue(genericArrayTypeImplClass.isInstance(result));

    // Check that component type is canonicalized String.class
    Type componentType = ((GenericArrayType) result).getGenericComponentType();
    assertEquals(String.class, componentType);
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withClassNonArray_returnsSameClass() {
    Class<?> clazz = Integer.class;
    Type result = $Gson$Types.canonicalize(clazz);
    assertSame(clazz, result);
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withParameterizedType_returnsParameterizedTypeImpl() throws Exception {
    ParameterizedType mockParamType = mock(ParameterizedType.class);
    Type ownerType = String.class;
    Type rawType = java.util.Map.class;
    Type[] typeArgs = new Type[] { String.class, Integer.class };
    when(mockParamType.getOwnerType()).thenReturn(ownerType);
    when(mockParamType.getRawType()).thenReturn(rawType);
    when(mockParamType.getActualTypeArguments()).thenReturn(typeArgs);

    Type result = $Gson$Types.canonicalize(mockParamType);
    assertNotNull(result);

    Class<?> parameterizedTypeImplClass = getPrivateInnerClass("ParameterizedTypeImpl");
    assertTrue(parameterizedTypeImplClass.isInstance(result));

    // Use reflection to access methods
    Object pti = result;
    Method getOwnerType = parameterizedTypeImplClass.getMethod("getOwnerType");
    Method getRawType = parameterizedTypeImplClass.getMethod("getRawType");
    Method getActualTypeArguments = parameterizedTypeImplClass.getMethod("getActualTypeArguments");

    assertEquals(ownerType, getOwnerType.invoke(pti));
    assertEquals(rawType, getRawType.invoke(pti));
    assertArrayEquals(typeArgs, (Object[]) getActualTypeArguments.invoke(pti));
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withGenericArrayType_returnsGenericArrayTypeImpl() throws Exception {
    GenericArrayType mockGenericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(mockGenericArrayType.getGenericComponentType()).thenReturn(componentType);

    Type result = $Gson$Types.canonicalize(mockGenericArrayType);
    assertNotNull(result);

    Class<?> genericArrayTypeImplClass = getPrivateInnerClass("GenericArrayTypeImpl");
    assertTrue(genericArrayTypeImplClass.isInstance(result));

    Type comp = ((GenericArrayType) result).getGenericComponentType();
    assertEquals(componentType, comp);
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withWildcardType_returnsWildcardTypeImpl() throws Exception {
    // Create real WildcardType instance instead of mocking
    Type upperBound = Number.class;
    Type lowerBound = null; // Use null for lower bound to satisfy WildcardType constraints

    // Use subtypeOf helper to create WildcardType instance with upper bound only
    WildcardType customWildcardType = $Gson$Types.subtypeOf(upperBound);

    Type result = $Gson$Types.canonicalize(customWildcardType);
    assertNotNull(result);

    Class<?> wildcardTypeImplClass = getPrivateInnerClass("WildcardTypeImpl");
    assertTrue(wildcardTypeImplClass.isInstance(result));

    Object wti = result;
    Method getUpperBounds = wildcardTypeImplClass.getMethod("getUpperBounds");
    Method getLowerBounds = wildcardTypeImplClass.getMethod("getLowerBounds");

    assertArrayEquals(new Type[] { upperBound }, (Object[]) getUpperBounds.invoke(wti));
    assertArrayEquals(new Type[0], (Object[]) getLowerBounds.invoke(wti));
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withOtherType_returnsSameType() {
    Type customType = new Type() {};
    Type result = $Gson$Types.canonicalize(customType);
    assertSame(customType, result);
  }
}