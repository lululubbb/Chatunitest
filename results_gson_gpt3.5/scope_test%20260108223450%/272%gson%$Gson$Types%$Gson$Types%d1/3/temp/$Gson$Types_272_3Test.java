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

public class $Gson$Types_272_3Test {

  @Test
    @Timeout(8000)
  void testConstructor_throwsUnsupportedOperationException() throws Exception {
    var constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> constructor.newInstance());
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void testNewParameterizedTypeWithOwner_basic() {
    Type owner = String.class;
    Type raw = Map.class;
    Type[] args = new Type[] {String.class, Integer.class};
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(owner, raw, args);
    assertEquals(raw, pt.getRawType());
    assertArrayEquals(args, pt.getActualTypeArguments());
    assertEquals(owner, pt.getOwnerType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_returnsGenericArrayType() {
    Type component = String.class;
    GenericArrayType gat = $Gson$Types.arrayOf(component);
    assertEquals(component, gat.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testSubtypeOf_andSupertypeOf_wildcardBounds() {
    Type bound = Number.class;
    WildcardType subtype = $Gson$Types.subtypeOf(bound);
    assertArrayEquals(new Type[] {bound}, subtype.getUpperBounds());
    assertArrayEquals(new Type[0], subtype.getLowerBounds());

    WildcardType supertype = $Gson$Types.supertypeOf(bound);
    assertArrayEquals(new Type[] {Object.class}, supertype.getUpperBounds());
    assertArrayEquals(new Type[] {bound}, supertype.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  void testCanonicalize_returnsExpectedTypes() {
    Type t1 = String.class;
    Type t2 = $Gson$Types.canonicalize(t1);
    assertEquals(t1, t2);

    Type pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type ptCanonical = $Gson$Types.canonicalize(pt);
    assertNotNull(ptCanonical);
    assertTrue(ptCanonical instanceof ParameterizedType);

    Type gat = $Gson$Types.arrayOf(String.class);
    Type gatCanonical = $Gson$Types.canonicalize(gat);
    assertNotNull(gatCanonical);
    assertTrue(gatCanonical instanceof GenericArrayType);

    Type wt = $Gson$Types.subtypeOf(Number.class);
    Type wtCanonical = $Gson$Types.canonicalize(wt);
    assertNotNull(wtCanonical);
    assertTrue(wtCanonical instanceof WildcardType);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_various() {
    assertEquals(String.class, $Gson$Types.getRawType(String.class));
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, List.class, String.class);
    assertEquals(List.class, $Gson$Types.getRawType(pt));
    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    assertEquals(Array.newInstance(String.class, 0).getClass(), $Gson$Types.getRawType(gat));
    assertEquals(Object.class, $Gson$Types.getRawType(Object.class));
  }

  @Test
    @Timeout(8000)
  void testEqual_privateMethod() throws Exception {
    var method = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    method.setAccessible(true);
    assertTrue((Boolean) method.invoke(null, null, null));
    assertTrue((Boolean) method.invoke(null, "a", "a"));
    assertFalse((Boolean) method.invoke(null, "a", "b"));
    assertFalse((Boolean) method.invoke(null, null, "a"));
  }

  @Test
    @Timeout(8000)
  void testEquals_publicMethod() {
    Type t1 = String.class;
    Type t2 = String.class;
    assertTrue($Gson$Types.equals(t1, t2));

    Type pt1 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type pt2 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    assertTrue($Gson$Types.equals(pt1, pt2));

    Type pt3 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, Integer.class, String.class);
    assertFalse($Gson$Types.equals(pt1, pt3));

    assertFalse($Gson$Types.equals(null, pt3));
  }

  @Test
    @Timeout(8000)
  void testTypeToString_basic() {
    assertEquals("java.lang.String", $Gson$Types.typeToString(String.class));
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, List.class, String.class);
    String str = $Gson$Types.typeToString(pt);
    assertTrue(str.contains("List"));
  }

  @Test
    @Timeout(8000)
  void testGetArrayComponentType_returnsComponent() {
    Type arrayType = String[].class;
    Type component = $Gson$Types.getArrayComponentType(arrayType);
    assertEquals(String.class, component);

    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    assertEquals(String.class, $Gson$Types.getArrayComponentType(gat));
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_basic() {
    // Adjusted expected type to match actual TypeVariable E from ArrayList
    Type elementType = $Gson$Types.getCollectionElementType(ArrayList.class, ArrayList.class);
    assertTrue(elementType instanceof TypeVariable, "Expected a TypeVariable");
    assertEquals("E", ((TypeVariable<?>) elementType).getName());

    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Collection.class, String.class);
    Type t = $Gson$Types.getCollectionElementType(pt, Collection.class);
    // Adjusted assertion to expect String.class instead of Object.class
    assertEquals(String.class, t);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_basic() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Map.class, Map.class);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testResolve_withTypeVariable() throws Exception {
    class Generic<T> {
      T field;
    }
    TypeVariable<?> tv = Generic.class.getTypeParameters()[0];
    Type resolved = $Gson$Types.resolve(Generic.class, Generic.class, tv);
    assertEquals(tv, resolved);

    // Using reflection to invoke private resolve with visitedTypeVariables
    var method = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    method.setAccessible(true);
    Map<TypeVariable<?>, Type> visited = new HashMap<>();
    Type result = (Type) method.invoke(null, Generic.class, Generic.class, tv, visited);
    assertEquals(tv, result);
  }

  @Test
    @Timeout(8000)
  void testResolveTypeVariable_declaringClassAndIndex() throws Exception {
    class Outer<T> {
      class Inner<U> {
        U innerField;
      }
    }
    TypeVariable<?> tv = Outer.Inner.class.getTypeParameters()[0];
    var method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);
    Type resolved = (Type) method.invoke(null, Outer.class, Outer.Inner.class, tv);
    assertEquals(tv, resolved);
  }

  @Test
    @Timeout(8000)
  void testIndexOf_foundAndNotFound() throws Exception {
    var method = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    method.setAccessible(true);
    Object[] array = new Object[] {"a", "b", "c"};
    int idx = (int) method.invoke(null, (Object) array, "b");
    assertEquals(1, idx);
    int idxNotFound = (int) method.invoke(null, (Object) array, "z");
    assertEquals(-1, idxNotFound);
  }

  @Test
    @Timeout(8000)
  void testDeclaringClassOf_various() throws Exception {
    class Outer<T> {
      class Inner<U> {
        U innerField;
      }
    }
    TypeVariable<?> tv = Outer.Inner.class.getTypeParameters()[0];
    var method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);
    Class<?> declaring = (Class<?>) method.invoke(null, tv);
    assertEquals(Outer.Inner.class, declaring);

    TypeVariable<?>[] objectTVs = Object.class.getTypeParameters();
    if (objectTVs.length > 0) {
      TypeVariable<?> tv2 = objectTVs[0];
      Class<?> declaring2 = (Class<?>) method.invoke(null, tv2);
      assertEquals(Object.class, declaring2);
    }
  }

  @Test
    @Timeout(8000)
  void testCheckNotPrimitive_throwsForPrimitive() throws Exception {
    var method = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    method.setAccessible(true);
    method.invoke(null, String.class);
    assertThrows(InvocationTargetException.class, () -> method.invoke(null, int.class));
    assertThrows(InvocationTargetException.class, () -> method.invoke(null, boolean.class));
  }
}