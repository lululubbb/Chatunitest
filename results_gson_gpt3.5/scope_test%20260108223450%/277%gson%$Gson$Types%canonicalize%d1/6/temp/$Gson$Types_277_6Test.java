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

public class $Gson$Types_277_6Test {

  @Test
    @Timeout(8000)
  public void canonicalize_withClassArray_returnsGenericArrayTypeImpl() throws Exception {
    Class<?> arrayClass = String[].class;
    Type result = $Gson$Types.canonicalize(arrayClass);
    assertNotNull(result);
    assertTrue(result instanceof GenericArrayType);
    GenericArrayType gat = (GenericArrayType) result;
    assertEquals(String.class, gat.getGenericComponentType());
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
    ParameterizedType mockParameterizedType = mock(ParameterizedType.class);
    Type ownerType = String.class;
    Type rawType = java.util.Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};
    when(mockParameterizedType.getOwnerType()).thenReturn(ownerType);
    when(mockParameterizedType.getRawType()).thenReturn(rawType);
    when(mockParameterizedType.getActualTypeArguments()).thenReturn(typeArgs);

    Type result = $Gson$Types.canonicalize(mockParameterizedType);

    assertNotNull(result);
    assertEquals(getInnerClass("$Gson$Types$ParameterizedTypeImpl"), result.getClass());
    Object pti = result;
    assertEquals(ownerType, invoke(pti, "getOwnerType"));
    assertEquals(rawType, invoke(pti, "getRawType"));
    assertArrayEquals(typeArgs, (Type[]) invoke(pti, "getActualTypeArguments"));
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withGenericArrayType_returnsGenericArrayTypeImpl() throws Exception {
    GenericArrayType mockGenericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(mockGenericArrayType.getGenericComponentType()).thenReturn(componentType);

    Type result = $Gson$Types.canonicalize(mockGenericArrayType);

    assertNotNull(result);
    assertEquals(getInnerClass("$Gson$Types$GenericArrayTypeImpl"), result.getClass());
    Object gati = result;
    assertEquals(componentType, invoke(gati, "getGenericComponentType"));
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withWildcardType_returnsWildcardTypeImpl() throws Exception {
    WildcardType mockWildcardType = mock(WildcardType.class);
    // Fix: upper bounds must not be empty; lower bounds must be empty or a subtype of upper bounds
    Type[] upperBounds = new Type[] {Number.class};
    Type[] lowerBounds = new Type[] {}; // empty lower bounds to satisfy checkArgument in constructor
    when(mockWildcardType.getUpperBounds()).thenReturn(upperBounds);
    when(mockWildcardType.getLowerBounds()).thenReturn(lowerBounds);

    Type result = $Gson$Types.canonicalize(mockWildcardType);

    assertNotNull(result);
    assertEquals(getInnerClass("$Gson$Types$WildcardTypeImpl"), result.getClass());
    Object wti = result;
    assertArrayEquals(upperBounds, (Type[]) invoke(wti, "getUpperBounds"));
    assertArrayEquals(lowerBounds, (Type[]) invoke(wti, "getLowerBounds"));
  }

  @Test
    @Timeout(8000)
  public void canonicalize_withOtherType_returnsSameType() {
    Type customType = new Type() {};
    Type result = $Gson$Types.canonicalize(customType);
    assertSame(customType, result);
  }

  private static Class<?> getInnerClass(String name) throws ClassNotFoundException {
    // The inner classes are private static inside $Gson$Types, so use their binary names
    String pkg = $Gson$Types.class.getPackageName();
    String fullName = pkg + "." + "$Gson$Types" + name.substring("$Gson$Types".length());
    // The above might not work if packageName is empty; fallback:
    try {
      return Class.forName(fullName);
    } catch (ClassNotFoundException e) {
      // fallback to $Gson$Types$InnerClassName style
      return Class.forName("$Gson$Types" + name);
    }
  }

  private static Object invoke(Object obj, String methodName) throws Exception {
    Method m = obj.getClass().getDeclaredMethod(methodName);
    m.setAccessible(true);
    return m.invoke(obj);
  }
}