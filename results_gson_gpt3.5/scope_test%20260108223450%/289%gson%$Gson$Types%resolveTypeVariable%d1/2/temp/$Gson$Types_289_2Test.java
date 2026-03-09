package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

class $Gson$Types_289_2Test {

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassNull_returnsUnknown() throws Exception {
    // Prepare a TypeVariable with no declaring class (mock to return null)
    // Since we cannot create a TypeVariable with no declaring class or an interface inside method,
    // use a dummy class with a type variable.
    class Dummy<T> {}
    TypeVariable<?> dummyVar = Dummy.class.getTypeParameters()[0];

    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, Dummy.class, Dummy.class, dummyVar);

    // Since Dummy has no supertype declaring dummyVar, expected to return unknown
    assertSame(dummyVar, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawParameterized_returnsActualTypeArgument() throws Exception {
    class Base<T> {}
    class Derived extends Base<String> {}

    TypeVariable<?> tVar = Base.class.getTypeParameters()[0];

    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);

    Type resolved = (Type) method.invoke(null, Derived.class, Derived.class, tVar);

    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawNonParameterized_returnsUnknown() throws Exception {
    class Base<T> {}
    class Derived extends Base {}

    TypeVariable<?> tVar = Base.class.getTypeParameters()[0];

    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);

    Type resolved = (Type) method.invoke(null, Derived.class, Derived.class, tVar);

    assertSame(tVar, resolved);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_indexOfReturnsCorrectIndex() throws Exception {
    class Base<K, V> {}
    class Derived extends Base<String, Integer> {}

    TypeVariable<?> kVar = Base.class.getTypeParameters()[0];
    TypeVariable<?> vVar = Base.class.getTypeParameters()[1];

    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);

    Type resolvedK = (Type) method.invoke(null, Derived.class, Derived.class, kVar);
    Type resolvedV = (Type) method.invoke(null, Derived.class, Derived.class, vVar);

    assertEquals(String.class, resolvedK);
    assertEquals(Integer.class, resolvedV);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassOfReturnsNull_returnsUnknown() throws Exception {
    // Instead of interface inside method, define a static interface outside or use an existing one.
    // Here, use a static interface declared in this test class.

    TypeVariable<?> tVar = InterfaceWithTypeParam.class.getTypeParameters()[0];

    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, InterfaceWithTypeParam.class, InterfaceWithTypeParam.class, tVar);

    assertSame(tVar, result);
  }

  // Static interface declared outside methods to avoid compile error
  interface InterfaceWithTypeParam<T> {}
}