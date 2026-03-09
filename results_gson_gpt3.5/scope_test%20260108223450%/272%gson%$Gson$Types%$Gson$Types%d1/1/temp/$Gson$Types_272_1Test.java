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

public class $Gson$Types_272_1Test {

  @Test
    @Timeout(8000)
  public void testPrivateConstructorThrows() throws Exception {
    Constructor<$Gson$Types> constructor = $Gson$Types.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      try {
        constructor.newInstance();
      } catch (InvocationTargetException e) {
        // unwrap the cause and throw it to be caught by assertThrows
        throw e.getCause();
      }
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void testEqual() throws Exception {
    Method equalMethod = $Gson$Types.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);

    // both null
    assertTrue((Boolean) equalMethod.invoke(null, null, null));
    // one null
    assertFalse((Boolean) equalMethod.invoke(null, null, "test"));
    assertFalse((Boolean) equalMethod.invoke(null, "test", null));
    // equal objects
    Object obj = new Object();
    assertTrue((Boolean) equalMethod.invoke(null, obj, obj));
    // different objects
    assertFalse((Boolean) equalMethod.invoke(null, new Object(), new Object()));
    // equal strings
    assertTrue((Boolean) equalMethod.invoke(null, "abc", "abc"));
    // different strings
    assertFalse((Boolean) equalMethod.invoke(null, "abc", "def"));
  }

  @Test
    @Timeout(8000)
  public void testGetGenericSupertype() throws Exception {
    Method getGenericSupertype = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    getGenericSupertype.setAccessible(true);

    class A {}
    class B extends A {}
    Type context = B.class;
    Class<?> rawType = B.class;
    Class<?> supertype = A.class;

    Type result = (Type) getGenericSupertype.invoke(null, context, rawType, supertype);
    assertEquals(A.class, result);
  }

  @Test
    @Timeout(8000)
  public void testGetSupertype() throws Exception {
    Method getSupertype = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertype.setAccessible(true);

    class A {}
    class B extends A {}
    Type context = B.class;
    Class<?> contextRawType = B.class;
    Class<?> supertype = A.class;

    Type result = (Type) getSupertype.invoke(null, context, contextRawType, supertype);
    assertEquals(A.class, result);
  }

  @Test
    @Timeout(8000)
  public void testResolveTypeVariable() throws Exception {
    Method resolveTypeVariable = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariable.setAccessible(true);

    class GenericClass<T> {}
    TypeVariable<?>[] typeVars = GenericClass.class.getTypeParameters();
    TypeVariable<?> typeVar = typeVars[0];

    Type resolved = (Type) resolveTypeVariable.invoke(null, GenericClass.class, GenericClass.class, typeVar);
    assertEquals(typeVar, resolved); // no mapping, returns same
  }

  @Test
    @Timeout(8000)
  public void testIndexOf() throws Exception {
    Method indexOf = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOf.setAccessible(true);

    Object[] array = new Object[] {"a", "b", "c"};
    // Pass the array as Object[] directly, wrapped in an Object[] for varargs
    int idx = (int) indexOf.invoke(null, (Object) array, "b");
    assertEquals(1, idx);

    int idxNotFound = (int) indexOf.invoke(null, (Object) array, "z");
    assertEquals(-1, idxNotFound);
  }

  @Test
    @Timeout(8000)
  public void testDeclaringClassOf() throws Exception {
    Method declaringClassOf = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    declaringClassOf.setAccessible(true);

    class Outer<T> {
      class Inner<U> {}
    }
    TypeVariable<?>[] typeVars = Outer.class.getTypeParameters();
    TypeVariable<?> typeVar = typeVars[0];
    Class<?> declaring = (Class<?>) declaringClassOf.invoke(null, typeVar);
    assertEquals(Outer.class, declaring);
  }

  @Test
    @Timeout(8000)
  public void testCheckNotPrimitive() throws Exception {
    Method checkNotPrimitive = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
    checkNotPrimitive.setAccessible(true);

    // Should not throw for non-primitive
    checkNotPrimitive.invoke(null, String.class);

    // Should throw for primitive
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        checkNotPrimitive.invoke(null, int.class);
      } catch (InvocationTargetException e) {
        // unwrap the cause and throw it to be caught by assertThrows
        throw e.getCause();
      }
    });
    assertNotNull(thrown);
  }
}