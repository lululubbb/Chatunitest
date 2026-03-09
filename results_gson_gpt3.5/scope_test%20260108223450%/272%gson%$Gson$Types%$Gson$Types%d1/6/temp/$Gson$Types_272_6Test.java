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

public class $Gson$Types_272_6Test {

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_throwsUnsupportedOperationException() throws Exception {
    Constructor<$Gson$Types> constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    assertNotNull(thrown);
    // Check that the cause is UnsupportedOperationException
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  public void testNewParameterizedTypeWithOwner_basic() {
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
  public void testArrayOf_basic() {
    Type component = String.class;
    GenericArrayType arrayType = $Gson$Types.arrayOf(component);
    assertEquals(component, arrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  public void testSubtypeOf_basic() {
    Type bound = Number.class;
    WildcardType wildcard = $Gson$Types.subtypeOf(bound);
    assertArrayEquals(new Type[] {bound}, wildcard.getUpperBounds());
    assertArrayEquals(new Type[0], wildcard.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void testSupertypeOf_basic() {
    Type bound = Integer.class;
    WildcardType wildcard = $Gson$Types.supertypeOf(bound);
    assertArrayEquals(new Type[] {Object.class}, wildcard.getUpperBounds());
    assertArrayEquals(new Type[] {bound}, wildcard.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_classType() {
    Type type = String.class;
    Type canonical = $Gson$Types.canonicalize(type);
    assertEquals(String.class, canonical);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_class() {
    Class<?> raw = $Gson$Types.getRawType(String.class);
    assertEquals(String.class, raw);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_parameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Class<?> raw = $Gson$Types.getRawType(pt);
    assertEquals(Map.class, raw);
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameType() {
    Type a = String.class;
    Type b = String.class;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentType() {
    Type a = String.class;
    Type b = Integer.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testTypeToString_class() {
    String s = $Gson$Types.typeToString(String.class);
    assertEquals("java.lang.String", s);
  }

  @Test
    @Timeout(8000)
  public void testGetArrayComponentType_arrayClass() {
    Type arrayType = String[].class;
    Type component = $Gson$Types.getArrayComponentType(arrayType);
    assertEquals(String.class, component);
  }

  @Test
    @Timeout(8000)
  public void testGetCollectionElementType() {
    Type collectionType = new ParameterizedType() {
      public Type[] getActualTypeArguments() { return new Type[] {String.class}; }
      public Type getRawType() { return Collection.class; }
      public Type getOwnerType() { return null; }
    };
    Type elementType = $Gson$Types.getCollectionElementType(collectionType, Collection.class);
    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  public void testGetMapKeyAndValueTypes() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(pt, Map.class);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(Integer.class, types[1]);
  }

  @Test
    @Timeout(8000)
  public void testResolve_withTypeVariable() throws Exception {
    class GenericClass<T> {
      T field;
    }
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];
    Type resolved = $Gson$Types.resolve(GenericClass.class, GenericClass.class, typeVariable);
    assertEquals(typeVariable, resolved);
  }

  @Test
    @Timeout(8000)
  public void testCheckNotPrimitive_throwsOnPrimitive() {
    assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.checkNotPrimitive(int.class);
    });
  }

  @Test
    @Timeout(8000)
  public void testCheckNotPrimitive_noThrowOnNonPrimitive() {
    $Gson$Types.checkNotPrimitive(String.class);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_equal() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    assertTrue((Boolean) equalMethod.invoke(null, "a", "a"));
    assertFalse((Boolean) equalMethod.invoke(null, "a", "b"));
    assertTrue((Boolean) equalMethod.invoke(null, null, null));
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_getGenericSupertype() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, ArrayList.class, ArrayList.class, Collection.class);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_getSupertype() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);
    Type result = (Type) method.invoke(null, ArrayList.class, ArrayList.class, Collection.class);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_resolveTypeVariable() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);
    TypeVariable<?> tv = Map.class.getTypeParameters()[0];
    Type resolved = (Type) method.invoke(null, Map.class, Map.class, tv);
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_indexOf() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    method.setAccessible(true);
    Object[] array = new Object[] {"a", "b", "c"};
    // Pass array as Object[] directly, not wrapped in Object
    int index = (Integer) method.invoke(null, array, "b");
    assertEquals(1, index);
    int notFound = (Integer) method.invoke(null, array, "x");
    assertEquals(-1, notFound);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethod_declaringClassOf() throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);
    TypeVariable<?> tv = Map.class.getTypeParameters()[0];
    Class<?> declaring = (Class<?>) method.invoke(null, tv);
    assertEquals(Map.class, declaring);
  }
}