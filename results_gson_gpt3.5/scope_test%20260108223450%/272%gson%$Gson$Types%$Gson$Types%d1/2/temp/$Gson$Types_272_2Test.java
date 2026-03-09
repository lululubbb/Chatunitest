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

public class $Gson$Types_272_2Test {

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    Constructor<$Gson$Types> constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    assertNotNull(ex);
    assertTrue(ex.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  public void testNewParameterizedTypeWithOwner() {
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
  public void testArrayOf() {
    Type component = String.class;
    GenericArrayType gat = $Gson$Types.arrayOf(component);
    assertEquals(component, gat.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  public void testSubtypeOf() {
    Type bound = Number.class;
    WildcardType wt = $Gson$Types.subtypeOf(bound);
    assertArrayEquals(new Type[] {bound}, wt.getUpperBounds());
    assertArrayEquals(new Type[0], wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void testSupertypeOf() {
    Type bound = Integer.class;
    WildcardType wt = $Gson$Types.supertypeOf(bound);
    assertArrayEquals(new Type[] {Object.class}, wt.getUpperBounds());
    assertArrayEquals(new Type[] {bound}, wt.getLowerBounds());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_ClassType() {
    Type type = String.class;
    Type canonical = $Gson$Types.canonicalize(type);
    assertSame(type, canonical);
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_ParameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    Type canonical = $Gson$Types.canonicalize(pt);
    assertTrue(canonical instanceof ParameterizedType);
    ParameterizedType pt2 = (ParameterizedType) canonical;
    assertEquals(Map.class, pt2.getRawType());
    assertArrayEquals(new Type[] {String.class, Integer.class}, pt2.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_GenericArrayType() {
    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    Type canonical = $Gson$Types.canonicalize(gat);
    assertTrue(canonical instanceof GenericArrayType);
    assertEquals(String.class, ((GenericArrayType) canonical).getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  public void testCanonicalize_WildcardType() {
    WildcardType wt = $Gson$Types.subtypeOf(Number.class);
    Type canonical = $Gson$Types.canonicalize(wt);
    assertTrue(canonical instanceof WildcardType);
    assertArrayEquals(new Type[] {Number.class}, ((WildcardType) canonical).getUpperBounds());
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_Class() {
    assertEquals(String.class, $Gson$Types.getRawType(String.class));
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_ParameterizedType() {
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    assertEquals(Map.class, $Gson$Types.getRawType(pt));
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_GenericArrayType() {
    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    Class<?> rawType = $Gson$Types.getRawType(gat);
    assertEquals(String[].class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_TypeVariable() throws Exception {
    // Create a mock TypeVariable with declaring class Object and bounds String.class
    TypeVariable<?> tv = createTypeVariableMock();
    Class<?> rawType = $Gson$Types.getRawType(tv);
    assertEquals(Object.class, rawType);
  }

  private TypeVariable<?> createTypeVariableMock() throws Exception {
    Class<?> clazz = DummyClass.class;
    TypeVariable<?>[] typeVars = clazz.getTypeParameters();
    return typeVars[0];
  }

  static class DummyClass<T> {}

  @Test
    @Timeout(8000)
  public void testEqual() throws Exception {
    // Use reflection to invoke private static equal(Object,Object)
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);

    assertTrue((Boolean) equalMethod.invoke(null, "a", "a"));
    assertFalse((Boolean) equalMethod.invoke(null, "a", "b"));
    assertTrue((Boolean) equalMethod.invoke(null, null, null));
    assertFalse((Boolean) equalMethod.invoke(null, "a", null));
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    Type t1 = String.class;
    Type t2 = String.class;
    assertTrue($Gson$Types.equals(t1, t2));
    assertFalse($Gson$Types.equals(t1, Integer.class));

    ParameterizedType pt1 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    ParameterizedType pt2 = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    assertTrue($Gson$Types.equals(pt1, pt2));

    GenericArrayType gat1 = $Gson$Types.arrayOf(String.class);
    GenericArrayType gat2 = $Gson$Types.arrayOf(String.class);
    assertTrue($Gson$Types.equals(gat1, gat2));

    WildcardType wt1 = $Gson$Types.subtypeOf(Number.class);
    WildcardType wt2 = $Gson$Types.subtypeOf(Number.class);
    assertTrue($Gson$Types.equals(wt1, wt2));
  }

  @Test
    @Timeout(8000)
  public void testTypeToString() {
    assertEquals("java.lang.String", $Gson$Types.typeToString(String.class));
    ParameterizedType pt = $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, String.class, Integer.class);
    String s = $Gson$Types.typeToString(pt);
    assertTrue(s.startsWith("java.util.Map"));
  }

  @Test
    @Timeout(8000)
  public void testGetArrayComponentType() {
    Type component = $Gson$Types.getArrayComponentType(String[].class);
    assertEquals(String.class, component);
    GenericArrayType gat = $Gson$Types.arrayOf(String.class);
    assertEquals(String.class, $Gson$Types.getArrayComponentType(gat));
  }

  @Test
    @Timeout(8000)
  public void testGetCollectionElementType() {
    Type elementType = $Gson$Types.getCollectionElementType(new ArrayList<String>(){}.getClass().getGenericSuperclass(), ArrayList.class);
    assertNotNull(elementType);
  }

  @Test
    @Timeout(8000)
  public void testGetMapKeyAndValueTypes() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(new HashMap<String, Integer>(){}.getClass().getGenericSuperclass(), HashMap.class);
    assertEquals(2, types.length);
  }

  @Test
    @Timeout(8000)
  public void testResolve_simple() {
    Type toResolve = String.class;
    Type resolved = $Gson$Types.resolve(null, Object.class, toResolve);
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  public void testCheckNotPrimitive() {
    try {
      Method checkNotPrimitiveMethod = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
      checkNotPrimitiveMethod.setAccessible(true);

      try {
        checkNotPrimitiveMethod.invoke(null, String.class);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
        try {
          checkNotPrimitiveMethod.invoke(null, int.class);
        } catch (InvocationTargetException e) {
          // Throw the cause to be caught by assertThrows
          throw e.getCause();
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      });

      assertNotNull(ex);
      assertTrue(ex.getMessage().contains("primitive"));
    } catch (NoSuchMethodException e) {
      fail("Failed to find method checkNotPrimitive: " + e);
    } catch (IllegalAccessException e) {
      fail("Failed to access method checkNotPrimitive: " + e);
    } catch (Throwable e) {
      fail("Unexpected exception: " + e);
    }
  }
}