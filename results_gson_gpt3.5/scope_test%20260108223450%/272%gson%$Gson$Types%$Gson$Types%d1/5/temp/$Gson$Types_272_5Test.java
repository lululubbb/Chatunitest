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
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class $Gson$Types_272_5Test<T> {

  @Test
    @Timeout(8000)
  void testConstructor_throwsUnsupportedOperationException() throws Exception {
    var constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertThrows(InvocationTargetException.class, () -> constructor.newInstance());
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_basic() {
    Type owner = String.class;
    Type raw = Map.class;
    Type[] args = new Type[] { Integer.class, Long.class };
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(owner, raw, args);
    assertNotNull(pt);
    assertEquals(owner, pt.getOwnerType());
    assertEquals(raw, pt.getRawType());
    assertArrayEquals(args, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_nullOwnerAndRaw() {
    Type raw = List.class;
    Type[] args = new Type[] { String.class };
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, raw, args);
    assertNotNull(pt);
    assertNull(pt.getOwnerType());
    assertEquals(raw, pt.getRawType());
    assertArrayEquals(args, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_basic() {
    Type component = String.class;
    GenericArrayType gat = $Gson$Types.arrayOf(component);
    assertNotNull(gat);
    assertEquals(component, gat.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testSubtypeOf_basic() {
    Type bound = Number.class;
    WildcardType wt = $Gson$Types.subtypeOf(bound);
    assertNotNull(wt);
    assertArrayEquals(new Type[] { bound }, wt.getUpperBounds());
    assertArrayEquals(new Type[] { Object.class }, wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void testSupertypeOf_basic() {
    Type bound = Integer.class;
    WildcardType wt = $Gson$Types.supertypeOf(bound);
    assertNotNull(wt);
    assertArrayEquals(new Type[] { Object.class }, wt.getUpperBounds());
    assertArrayEquals(new Type[] { bound }, wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void testCanonicalize_classType() {
    Type t = String.class;
    Type result = $Gson$Types.canonicalize(t);
    assertSame(t, result);
  }

  @Test
    @Timeout(8000)
  void testCanonicalize_parameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type canonical = $Gson$Types.canonicalize(pt);
    assertNotNull(canonical);
    assertTrue(canonical instanceof ParameterizedType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_class() {
    Class<?> c = Integer.class;
    Class<?> raw = $Gson$Types.getRawType(c);
    assertEquals(Integer.class, raw);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_parameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Class<?> raw = $Gson$Types.getRawType(pt);
    assertEquals(Map.class, raw);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_genericArrayType() {
    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    Class<?> raw = $Gson$Types.getRawType(gat);
    assertEquals(Object[].class, raw);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_typeVariable() {
    TypeVariable<?> tv = $Gson$TypesTest.class.getTypeParameters()[0];
    Class<?> raw = $Gson$Types.getRawType(tv);
    assertEquals(Object.class, raw);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_wildcardType() {
    WildcardType wt = $Gson$Types.subtypeOf(Number.class);
    Class<?> raw = $Gson$Types.getRawType(wt);
    assertEquals(Number.class, raw);
  }

  @Test
    @Timeout(8000)
  void testEqual_trueAndFalse() throws Exception {
    var method = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    method.setAccessible(true);
    assertTrue((Boolean) method.invoke(null, "a", "a"));
    assertFalse((Boolean) method.invoke(null, "a", "b"));
    assertTrue((Boolean) method.invoke(null, null, null));
    assertFalse((Boolean) method.invoke(null, null, "a"));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeEquality() {
    Type t1 = String.class;
    Type t2 = String.class;
    assertTrue($Gson$Types.equals(t1, t2));
    Type pt1 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type pt2 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    assertTrue($Gson$Types.equals(pt1, pt2));
    Type pt3 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, Integer.class, String.class);
    assertFalse($Gson$Types.equals(pt1, pt3));
  }

  @Test
    @Timeout(8000)
  void testTypeToString_basic() {
    String s = $Gson$Types.typeToString(String.class);
    assertEquals("class java.lang.String", s);
  }

  @Test
    @Timeout(8000)
  void testGetGenericSupertype_andGetSupertype_reflection() throws Exception {
    var getGenericSupertype = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    getGenericSupertype.setAccessible(true);
    var getSupertype = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertype.setAccessible(true);

    Type result1 = (Type) getGenericSupertype.invoke(null, ArrayList.class.getGenericSuperclass(), ArrayList.class, Collection.class);
    assertNotNull(result1);

    Type result2 = (Type) getSupertype.invoke(null, ArrayList.class.getGenericSuperclass(), ArrayList.class, Collection.class);
    assertNotNull(result2);
  }

  @Test
    @Timeout(8000)
  void testGetArrayComponentType_basic() {
    Type arrayType = String[].class;
    Type comp = $Gson$Types.getArrayComponentType(arrayType);
    assertEquals(String.class, comp);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_basic() {
    Type elementType = $Gson$Types.getCollectionElementType(ArrayList.class, ArrayList.class);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_basic() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(HashMap.class, HashMap.class);
    assertEquals(2, types.length);
  }

  @Test
    @Timeout(8000)
  void testResolve_basic() {
    Type resolved = $Gson$Types.resolve(null, Object.class, String.class);
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_privateMethod() throws Exception {
    var resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    Type resolved = (Type) resolveMethod.invoke(null, null, Object.class, String.class, new HashMap<>());
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void testResolveTypeVariable_privateMethod() throws Exception {
    var resolveTypeVariable = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariable.setAccessible(true);
    TypeVariable<?> tv = $Gson$TypesTest.class.getTypeParameters()[0];
    Type resolved = (Type) resolveTypeVariable.invoke(null, null, $Gson$TypesTest.class, tv);
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  void testIndexOf_privateMethod() throws Exception {
    var indexOf = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOf.setAccessible(true);
    Object[] arr = new Object[] { "a", "b", "c" };
    int idx = (int) indexOf.invoke(null, (Object) arr, "b");
    assertEquals(1, idx);
    int idxNotFound = (int) indexOf.invoke(null, (Object) arr, "d");
    assertEquals(-1, idxNotFound);
  }

  @Test
    @Timeout(8000)
  void testDeclaringClassOf_privateMethod() throws Exception {
    var declaringClassOf = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    declaringClassOf.setAccessible(true);
    TypeVariable<?> tv = $Gson$TypesTest.class.getTypeParameters()[0];
    Class<?> declaring = (Class<?>) declaringClassOf.invoke(null, tv);
    assertEquals($Gson$TypesTest.class, declaring);
  }

  @Test
    @Timeout(8000)
  void testCheckNotPrimitive_throws() throws Exception {
    var checkNotPrimitive = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    checkNotPrimitive.setAccessible(true);
    checkNotPrimitive.invoke(null, String.class);
    assertThrows(InvocationTargetException.class, () -> checkNotPrimitive.invoke(null, int.class));
  }
}