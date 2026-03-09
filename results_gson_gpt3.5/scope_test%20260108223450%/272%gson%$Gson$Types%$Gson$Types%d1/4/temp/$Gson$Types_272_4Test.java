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

class $Gson$Types_272_4Test<T> {

  @Test
    @Timeout(8000)
  void constructor_shouldThrowUnsupportedOperationException() throws Exception {
    var constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      constructor.newInstance();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void newParameterizedTypeWithOwner_basicUsage() {
    Type owner = String.class;
    Type rawType = Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(owner, rawType, typeArgs);
    assertEquals(rawType, pt.getRawType());
    assertEquals(owner, pt.getOwnerType());
    assertArrayEquals(typeArgs, pt.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void arrayOf_shouldReturnGenericArrayType() {
    Type component = String.class;
    GenericArrayType gat = $Gson$Types.arrayOf(component);
    assertEquals(component, gat.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void subtypeOf_shouldReturnWildcardTypeWithUpperBound() {
    Type bound = Number.class;
    WildcardType wt = $Gson$Types.subtypeOf(bound);
    assertArrayEquals(new Type[] {bound}, wt.getUpperBounds());
    assertArrayEquals(new Type[] {Object.class}, wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void supertypeOf_shouldReturnWildcardTypeWithLowerBound() {
    Type bound = Integer.class;
    WildcardType wt = $Gson$Types.supertypeOf(bound);
    assertArrayEquals(new Type[] {Object.class}, wt.getUpperBounds());
    assertArrayEquals(new Type[] {bound}, wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void canonicalize_shouldReturnClassForClass() {
    Type t = String.class;
    assertSame(t, $Gson$Types.canonicalize(t));
  }

  @Test
    @Timeout(8000)
  void canonicalize_shouldReturnParameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type canonical = $Gson$Types.canonicalize(pt);
    assertTrue(canonical instanceof ParameterizedType);
    ParameterizedType cpt = (ParameterizedType) canonical;
    assertEquals(Map.class, cpt.getRawType());
  }

  @Test
    @Timeout(8000)
  void getRawType_shouldReturnClassForClass() {
    assertEquals(String.class, $Gson$Types.getRawType(String.class));
  }

  @Test
    @Timeout(8000)
  void getRawType_shouldReturnRawTypeForParameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    assertEquals(Map.class, $Gson$Types.getRawType(pt));
  }

  @Test
    @Timeout(8000)
  void equal_privateMethod() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    assertTrue((Boolean) equalMethod.invoke(null, "a", "a"));
    assertFalse((Boolean) equalMethod.invoke(null, "a", "b"));
    assertTrue((Boolean) equalMethod.invoke(null, null, null));
    assertFalse((Boolean) equalMethod.invoke(null, null, "b"));
  }

  @Test
    @Timeout(8000)
  void equals_shouldReturnTrueForEqualTypes() {
    Type t1 = String.class;
    Type t2 = String.class;
    assertTrue($Gson$Types.equals(t1, t2));
  }

  @Test
    @Timeout(8000)
  void equals_shouldReturnFalseForDifferentTypes() {
    Type t1 = String.class;
    Type t2 = Integer.class;
    assertFalse($Gson$Types.equals(t1, t2));
  }

  @Test
    @Timeout(8000)
  void typeToString_shouldReturnClassName() {
    assertEquals("java.lang.String", $Gson$Types.typeToString(String.class));
  }

  @Test
    @Timeout(8000)
  void getGenericSupertype_privateMethod() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, ArrayList.class, ArrayList.class, Collection.class);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void getSupertype_privateMethod() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, ArrayList.class, ArrayList.class, Collection.class);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void getArrayComponentType_shouldReturnComponentType() {
    Type arrayType = String[].class;
    Type component = $Gson$Types.getArrayComponentType(arrayType);
    assertEquals(String.class, component);
  }

  @Test
    @Timeout(8000)
  void getCollectionElementType_shouldReturnElementType() {
    Type elementType = $Gson$Types.getCollectionElementType(ArrayList.class, ArrayList.class);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void getMapKeyAndValueTypes_shouldReturnKeyValueTypes() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(HashMap.class, HashMap.class);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void resolve_shouldReturnResolvedType() {
    Type resolved = $Gson$Types.resolve(null, getClass(), String.class);
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_privateMethod_withVisitedTypeVariables() throws Exception {
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    Map<TypeVariable<?>, Type> visited = new HashMap<>();
    Type result = (Type) resolveMethod.invoke(null, null, getClass(), String.class, visited);
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_privateMethod() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);
    TypeVariable<?> tv = $Gson$TypesTest.class.getTypeParameters()[0];
    Type result = (Type) method.invoke(null, null, getClass(), tv);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void indexOf_privateMethod() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    method.setAccessible(true);
    Object[] arr = new Object[] {"a", "b", "c"};
    int index = (int) method.invoke(null, arr, "b");
    assertEquals(1, index);
    int notFound = (int) method.invoke(null, arr, "z");
    assertEquals(-1, notFound);
  }

  @Test
    @Timeout(8000)
  void declaringClassOf_privateMethod() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);
    TypeVariable<?> tv = $Gson$TypesTest.class.getTypeParameters()[0];
    Class<?> declaring = (Class<?>) method.invoke(null, tv);
    assertEquals($Gson$TypesTest.class, declaring);
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_shouldThrowForPrimitive() {
    assertThrows(IllegalArgumentException.class, () -> $Gson$Types.checkNotPrimitive(int.class));
  }

  @Test
    @Timeout(8000)
  void checkNotPrimitive_shouldPassForNonPrimitive() {
    $Gson$Types.checkNotPrimitive(String.class);
  }
}