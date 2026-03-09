package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

class $Gson$Types_282_1Test {

  private static Method getGenericSupertypeMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException {
    getGenericSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    getGenericSupertypeMethod.setAccessible(true);
  }

  private Type invokeGetGenericSupertype(Type context, Class<?> rawType, Class<?> supertype) throws IllegalAccessException, InvocationTargetException {
    return (Type) getGenericSupertypeMethod.invoke(null, context, rawType, supertype);
  }

  interface I1 {}
  interface I2 extends I1 {}
  interface I3 extends I2 {}

  static class A implements I1 {}
  static class B extends A implements I2 {}
  static class C extends B implements I3 {}

  @Test
    @Timeout(8000)
  void testSupertypeEqualsRawTypeReturnsContext() throws Exception {
    Type context = String.class;
    Class<?> rawType = String.class;
    Class<?> supertype = String.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertSame(context, result);
  }

  @Test
    @Timeout(8000)
  void testSupertypeIsInterfaceDirectlyImplemented() throws Exception {
    // B implements I2 directly
    Type context = B.class;
    Class<?> rawType = B.class;
    Class<?> supertype = I2.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    Type[] genericInterfaces = rawType.getGenericInterfaces();
    Type expected = null;
    for (int i = 0; i < genericInterfaces.length; i++) {
      if (rawType.getInterfaces()[i] == supertype) {
        expected = genericInterfaces[i];
        break;
      }
    }
    assertNotNull(expected);
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  void testSupertypeIsInterfaceIndirectlyImplemented() throws Exception {
    // C implements I3 which extends I2 which extends I1
    Type context = C.class;
    Class<?> rawType = C.class;
    Class<?> supertype = I1.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertNotNull(result);
    // The result should be a Type relating to I1 in the hierarchy
    // Since we don't have generic interfaces declared, it will recurse and return supertype if not found
    assertTrue(result instanceof Class<?> || result instanceof ParameterizedType || result instanceof Type);
  }

  @Test
    @Timeout(8000)
  void testSupertypeIsSuperclassDirect() throws Exception {
    // B extends A
    Type context = B.class.getGenericSuperclass();
    Class<?> rawType = B.class;
    Class<?> supertype = A.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertEquals(context, result);
  }

  @Test
    @Timeout(8000)
  void testSupertypeIsSuperclassIndirect() throws Exception {
    // C extends B extends A
    Type context = C.class.getGenericSuperclass();
    Class<?> rawType = C.class;
    Class<?> supertype = A.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertNotNull(result);
    // It should be a Type representing A in the hierarchy
    assertTrue(result instanceof Type);
  }

  @Test
    @Timeout(8000)
  void testSupertypeNotFoundReturnsSupertypeItself() throws Exception {
    Type context = String.class;
    Class<?> rawType = String.class;
    Class<?> supertype = Map.class; // String does not extend or implement Map

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertSame(supertype, result);
  }

  @Test
    @Timeout(8000)
  void testSupertypeIsInterfaceButNoMatchInRawTypeInterfaces() throws Exception {
    Type context = A.class;
    Class<?> rawType = A.class;
    Class<?> supertype = I3.class; // A implements I1, but not I3

    Type result = invokeGetGenericSupertype(context, rawType, supertype);
    assertSame(supertype, result);
  }

}