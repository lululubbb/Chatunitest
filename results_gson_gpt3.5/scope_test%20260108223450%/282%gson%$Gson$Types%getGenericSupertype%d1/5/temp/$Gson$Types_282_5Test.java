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

class $Gson$Types_282_5Test {

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_sameType_returnsContext() throws Exception {
    Class<?> clazz = String.class;
    Type context = clazz;
    Class<?> supertype = clazz;

    Type result = invokeGetGenericSupertype(context, clazz, supertype);

    assertEquals(context, result);
  }

  interface InterfaceA {}
  interface InterfaceB extends InterfaceA {}
  static class ClassWithInterfaces implements InterfaceB {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_interfaceDirectMatch_returnsGenericInterface() throws Exception {
    ClassWithInterfaces instance = new ClassWithInterfaces();
    Class<?> rawType = ClassWithInterfaces.class;
    Class<?> supertype = InterfaceB.class;

    Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

    Type[] genericInterfaces = rawType.getGenericInterfaces();
    int index = -1;
    Class<?>[] interfaces = rawType.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      if (interfaces[i] == supertype) {
        index = i;
        break;
      }
    }
    assertNotEquals(-1, index);
    assertEquals(genericInterfaces[index], result);
  }

  interface SuperInterface {}
  interface SubInterface extends SuperInterface {}
  static class ImplClass implements SubInterface {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_interfaceAssignableFromRecursive() throws Exception {
    Class<?> rawType = ImplClass.class;
    Class<?> supertype = SuperInterface.class;

    Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

    // Should return the generic interface of SubInterface from ImplClass or from SubInterface
    assertNotNull(result);
    assertTrue(result instanceof Type);
  }

  static class SuperClass<T> {}
  static class SubClass extends SuperClass<String> {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_superclassDirectMatch_returnsGenericSuperclass() throws Exception {
    Class<?> rawType = SubClass.class;
    Class<?> supertype = SuperClass.class;

    Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

    Type genericSuperclass = rawType.getGenericSuperclass();
    assertEquals(genericSuperclass, result);
  }

  static class GrandParent {}
  static class Parent extends GrandParent {}
  static class Child extends Parent {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_superclassAssignableFromRecursive() throws Exception {
    Class<?> rawType = Child.class;
    Class<?> supertype = GrandParent.class;

    Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

    assertNotNull(result);
    assertTrue(result instanceof Type);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_noMatch_returnsSupertype() throws Exception {
    Class<?> rawType = String.class;
    Class<?> supertype = Number.class;

    Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

    assertEquals(supertype, result);
  }

  private Type invokeGetGenericSupertype(Type context, Class<?> rawType, Class<?> supertype) throws Exception {
    java.lang.reflect.Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, context, rawType, supertype);
  }
}