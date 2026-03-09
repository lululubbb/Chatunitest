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

class GsonTypesTest {

  @Test
    @Timeout(8000)
  public void testCanonicalize_withClass_nonArray() {
    Class<String> input = String.class;
    Type result = $Gson$Types.canonicalize(input);
    assertSame(input, result);
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withClass_array() {
    Class<int[]> input = int[].class;
    Type result = $Gson$Types.canonicalize(input);
    assertFalse(result instanceof ParameterizedType);
    assertNotSame(input, result);
    assertTrue(result instanceof GenericArrayType || result.getClass().getName().endsWith("$GenericArrayTypeImpl"));
    // Check component type canonicalized
    Type componentType;
    if (result instanceof GenericArrayType) {
      componentType = ((GenericArrayType) result).getGenericComponentType();
    } else {
      try {
        Field componentTypeField = result.getClass().getDeclaredField("componentType");
        componentTypeField.setAccessible(true);
        componentType = (Type) componentTypeField.get(result);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    assertEquals(int.class, componentType);
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withParameterizedType() throws Exception {
    ParameterizedType mockParamType = mock(ParameterizedType.class);
    Type ownerType = String.class;
    Type rawType = java.util.Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};
    when(mockParamType.getOwnerType()).thenReturn(ownerType);
    when(mockParamType.getRawType()).thenReturn(rawType);
    when(mockParamType.getActualTypeArguments()).thenReturn(typeArgs);

    Type result = $Gson$Types.canonicalize(mockParamType);
    assertNotNull(result);
    assertNotSame(mockParamType, result);
    Class<?> clazz = result.getClass();
    assertTrue(clazz.getName().endsWith("$ParameterizedTypeImpl"));
    // Use reflection to verify fields
    Field ownerTypeField = clazz.getDeclaredField("ownerType");
    ownerTypeField.setAccessible(true);
    assertEquals(ownerType, ownerTypeField.get(result));
    Field rawTypeField = clazz.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    assertEquals(rawType, rawTypeField.get(result));
    Field typeArgsField = clazz.getDeclaredField("typeArguments");
    typeArgsField.setAccessible(true);
    assertArrayEquals(typeArgs, (Type[]) typeArgsField.get(result));
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withGenericArrayType() throws Exception {
    GenericArrayType mockGenericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(mockGenericArrayType.getGenericComponentType()).thenReturn(componentType);

    Type result = $Gson$Types.canonicalize(mockGenericArrayType);
    assertNotNull(result);
    assertNotSame(mockGenericArrayType, result);
    Class<?> clazz = result.getClass();
    assertTrue(clazz.getName().endsWith("$GenericArrayTypeImpl"));
    Field componentTypeField = clazz.getDeclaredField("componentType");
    componentTypeField.setAccessible(true);
    assertEquals(componentType, componentTypeField.get(result));
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withWildcardType() throws Exception {
    WildcardType mockWildcardType = mock(WildcardType.class);
    Type[] upperBounds = new Type[] {Number.class};
    Type[] lowerBounds = new Type[] {};
    when(mockWildcardType.getUpperBounds()).thenReturn(upperBounds);
    when(mockWildcardType.getLowerBounds()).thenReturn(lowerBounds);

    Type result = $Gson$Types.canonicalize(mockWildcardType);
    assertNotNull(result);
    assertNotSame(mockWildcardType, result);
    Class<?> clazz = result.getClass();
    assertTrue(clazz.getName().endsWith("$WildcardTypeImpl"));
    Field upperBoundsField = clazz.getDeclaredField("upperBounds");
    upperBoundsField.setAccessible(true);
    assertArrayEquals(upperBounds, (Type[]) upperBoundsField.get(result));
    Field lowerBoundsField = clazz.getDeclaredField("lowerBounds");
    lowerBoundsField.setAccessible(true);
    assertArrayEquals(lowerBounds, (Type[]) lowerBoundsField.get(result));
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_withOtherType_returnsSame() {
    Type unknownType = new Type() {};
    Type result = $Gson$Types.canonicalize(unknownType);
    assertSame(unknownType, result);
  }
}