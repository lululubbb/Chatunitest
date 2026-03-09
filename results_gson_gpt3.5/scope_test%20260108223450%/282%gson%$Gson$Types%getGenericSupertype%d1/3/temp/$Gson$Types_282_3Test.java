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
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class GsonTypesTest {

  private Type invokeGetGenericSupertype(Type context, Class<?> rawType, Class<?> supertype) throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, context, rawType, supertype);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeEqualsRawType_returnsContext() throws Exception {
    Type context = List.class;
    Class<?> rawType = List.class;
    Class<?> supertype = List.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);

    assertSame(context, result);
  }

  interface I1 {}
  interface I2 extends I1 {}
  static class A implements I2 {}
  static class B extends A {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeIsInterfaceDirectlyImplemented_returnsGenericInterface() throws Exception {
    Type context = A.class.getGenericInterfaces()[0];
    Class<?> rawType = A.class;
    Class<?> supertype = I2.class;

    Type result = invokeGetGenericSupertype(A.class, rawType, supertype);

    assertEquals(context, result);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeIsInterfaceIndirectlyImplemented_returnsSupertypeItself() throws Exception {
    Class<?> rawType = B.class;
    Class<?> supertype = I1.class;

    Type result = invokeGetGenericSupertype(B.class, rawType, supertype);

    assertEquals(supertype, result); // Because B->A->I2->I1 but no generic interface for I1 on B or A, returns supertype
  }

  static class C<T> {}
  static class D extends C<String> {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeIsSuperclassDirect_returnsGenericSuperclass() throws Exception {
    Type context = D.class.getGenericSuperclass();
    Class<?> rawType = D.class;
    Class<?> supertype = C.class;

    Type result = invokeGetGenericSupertype(D.class, rawType, supertype);

    assertEquals(context, result);
  }

  static class E extends D {}

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeIsSuperclassIndirect_returnsGenericSuperclassRecursive() throws Exception {
    Class<?> rawType = E.class;
    Class<?> supertype = C.class;

    Type result = invokeGetGenericSupertype(E.class, rawType, supertype);

    assertEquals(D.class.getGenericSuperclass(), result);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_whenSupertypeNotFound_returnsSupertypeItself() throws Exception {
    Type context = List.class;
    Class<?> rawType = List.class;
    Class<?> supertype = Map.class;

    Type result = invokeGetGenericSupertype(context, rawType, supertype);

    assertSame(supertype, result);
  }
}