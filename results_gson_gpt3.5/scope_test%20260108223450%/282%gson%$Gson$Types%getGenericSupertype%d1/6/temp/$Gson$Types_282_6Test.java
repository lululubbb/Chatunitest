package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class $Gson$Types_282_6Test {

  private static Method getGenericSupertypeMethod() throws NoSuchMethodException {
    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_sameTypeReturnsContext() throws Exception {
    Class<?> clazz = String.class;
    Type context = clazz;
    Class<?> rawType = clazz;
    Class<?> supertype = clazz;

    Method method = getGenericSupertypeMethod();

    Object result = method.invoke(null, context, rawType, supertype);

    assertSame(context, result);
  }

  interface InterfaceA {}
  interface InterfaceB extends InterfaceA {}
  interface InterfaceC {}

  static class SuperClass {}
  static class SubClass extends SuperClass implements InterfaceB {}

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_interfaceDirectMatch() throws Exception {
    Class<?> rawType = SubClass.class;
    Class<?> supertype = InterfaceB.class;
    Type context = rawType;

    Method method = getGenericSupertypeMethod();

    // Should return the generic interface type of InterfaceB in SubClass
    Object result = method.invoke(null, context, rawType, supertype);

    Type[] genericInterfaces = rawType.getGenericInterfaces();

    assertEquals(genericInterfaces.length, 1);
    assertSame(genericInterfaces[0], result);
  }

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_interfaceAssignableFrom() throws Exception {
    Class<?> rawType = SubClass.class;
    Class<?> supertype = InterfaceA.class;
    Type context = rawType;

    Method method = getGenericSupertypeMethod();

    // InterfaceA is assignable from InterfaceB which SubClass implements
    Object result = method.invoke(null, context, rawType, supertype);

    // The returned type should be the generic interface of InterfaceA in InterfaceB
    // Because getGenericSupertype returns the generic supertype closest to the rawType,
    // which is InterfaceA in InterfaceB, not InterfaceB in SubClass.
    Type[] genericInterfaces = InterfaceB.class.getGenericInterfaces();

    assertEquals(genericInterfaces.length, 1);
    assertSame(genericInterfaces[0], result);
  }

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_classDirectSuperclass() throws Exception {
    Class<?> rawType = SubClass.class;
    Class<?> supertype = SuperClass.class;
    Type context = rawType;

    Method method = getGenericSupertypeMethod();

    Object result = method.invoke(null, context, rawType, supertype);

    Type genericSuperclass = rawType.getGenericSuperclass();

    assertSame(genericSuperclass, result);
  }

  static class SubSubClass extends SubClass {}

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_classAssignableFromSuperclass() throws Exception {
    Class<?> rawType = SubSubClass.class;
    Class<?> supertype = SuperClass.class;
    Type context = rawType;

    Method method = getGenericSupertypeMethod();

    Object result = method.invoke(null, context, rawType, supertype);

    // The result should be the generic superclass of SubClass since SuperClass is superclass of SubClass
    Type genericSuperclass = SubSubClass.class.getGenericSuperclass(); // SubClass
    // We expect recursion to return SubClass's generic superclass (SuperClass)
    // So result should be SubClass.class.getGenericSuperclass()
    assertSame(SubClass.class.getGenericSuperclass(), result);
  }

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype_returnSupertypeWhenNotFound() throws Exception {
    Class<?> rawType = SubClass.class;
    Class<?> supertype = InterfaceC.class; // unrelated interface
    Type context = rawType;

    Method method = getGenericSupertypeMethod();

    Object result = method.invoke(null, context, rawType, supertype);

    assertSame(supertype, result);
  }

}