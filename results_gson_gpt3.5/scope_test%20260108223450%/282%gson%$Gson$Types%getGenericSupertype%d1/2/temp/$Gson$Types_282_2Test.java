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
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class $Gson$Types_282_2Test {

  // Helper classes and interfaces for testing
  interface SuperInterface {}
  interface SubInterface extends SuperInterface {}
  interface OtherInterface {}

  static class SuperClass {}
  static class SubClass extends SuperClass implements SubInterface {}
  static class OtherClass {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_sameType_returnsContext() throws Exception {
    // context and rawType and supertype all same
    Type context = String.class;
    Class<?> rawType = String.class;
    Class<?> supertype = String.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, context, rawType, supertype);

    assertSame(context, result);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_interfaceDirectlyImplemented_returnsGenericInterface() throws Exception {
    // SubClass implements SubInterface which extends SuperInterface
    // We test supertype is SubInterface directly implemented by SubClass
    Class<?> rawType = SubClass.class;
    Class<?> supertype = SubInterface.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, rawType, rawType, supertype);

    // Should return the generic interface from rawType.getGenericInterfaces()[i]
    Type[] genericInterfaces = rawType.getGenericInterfaces();
    boolean found = false;
    for (Type t : genericInterfaces) {
      if (t.getTypeName().equals(result.getTypeName())) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_interfaceAssignableFromInterface_returnsRecursiveCallResult() throws Exception {
    // SubInterface extends SuperInterface
    // rawType = SubClass, supertype = SuperInterface
    Class<?> rawType = SubClass.class;
    Class<?> supertype = SuperInterface.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, rawType, rawType, supertype);

    // Result should be the generic supertype of SubInterface for SuperInterface
    // which should be SubInterface's generic interface that is SuperInterface
    assertNotNull(result);
    // The returned Type should be a ParameterizedType or Class that corresponds to SuperInterface
    // We check that the raw type of result is SuperInterface.class or that result type name contains SuperInterface
    assertTrue(result.getTypeName().contains("SuperInterface"));
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_classDirectSuperclass_returnsGenericSuperclass() throws Exception {
    // rawType = SubClass, supertype = SuperClass
    Class<?> rawType = SubClass.class;
    Class<?> supertype = SuperClass.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, rawType, rawType, supertype);

    // Should return rawType.getGenericSuperclass()
    Type expected = rawType.getGenericSuperclass();
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_classAssignableFromSuperclass_returnsRecursiveCallResult() throws Exception {
    // rawType = SubClass, supertype = Object.class
    // Object is assignable from SuperClass and SubClass
    Class<?> rawType = SubClass.class;
    Class<?> supertype = Object.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, rawType, rawType, supertype);

    // Should eventually return Object.class as Type
    assertEquals(supertype, result);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_noMatch_returnsSupertype() throws Exception {
    // rawType = OtherClass, supertype = SuperInterface (which OtherClass does not implement)
    Class<?> rawType = OtherClass.class;
    Class<?> supertype = SuperInterface.class;

    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, rawType, rawType, supertype);

    // Should return supertype itself as no relation found
    assertEquals(supertype, result);
  }
}