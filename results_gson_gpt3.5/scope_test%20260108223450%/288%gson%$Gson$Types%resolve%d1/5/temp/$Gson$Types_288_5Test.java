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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class $Gson$Types_288_5Test {

  // Using reflection to access the private static resolve method with 4 parameters
  private static Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
  }

  @Test
    @Timeout(8000)
  void testResolve_withTypeVariable_resolvesProperly() throws Exception {
    // Setup a class with a type variable
    class GenericClass<T> {}
    TypeVariable<?>[] typeParameters = GenericClass.class.getTypeParameters();
    TypeVariable<?> tVar = typeParameters[0];

    // Context and raw type are GenericClass and GenericClass.class respectively
    Type context = GenericClass.class;
    Class<?> contextRawType = GenericClass.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    // We expect resolveTypeVariable to be called internally and return something different than the original variable
    // Since resolveTypeVariable is private, we cannot mock it directly, but we can test that resolve returns the variable if no resolution possible
    Type resolved = invokeResolve(context, contextRawType, tVar, visited);

    // Because GenericClass<T> has no bounds, resolveTypeVariable returns the variable itself, so resolved == tVar
    assertSame(tVar, resolved);
    // visited map should contain the type variable mapped to Void.TYPE (placeholder)
    assertEquals(Void.TYPE, visited.get(tVar));
  }

  @Test
    @Timeout(8000)
  void testResolve_withClassArray_resolvesComponentType() throws Exception {
    Class<?> contextRawType = String[].class;
    Type context = String[].class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Class<?> arrayClass = Integer[].class;
    Type resolved = invokeResolve(context, contextRawType, arrayClass, visited);

    // Should return the same array class because component type is a class and no change expected
    assertSame(arrayClass, resolved);

    // Now test with an array whose component type is a type variable
    class GenericArray<T> { T[] array; }
    TypeVariable<?> tVar = GenericArray.class.getTypeParameters()[0];
    Type arrayType = Array.newInstance(Object.class, 0).getClass(); // Object[]
    Type toResolve = Array.newInstance(Object.class, 0).getClass();

    // Actually, to test the branch, create a GenericArrayType with component type as TypeVariable
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override public Type getGenericComponentType() { return tVar; }
      @Override public String toString() { return tVar.getTypeName() + "[]"; }
    };

    Map<TypeVariable<?>, Type> visited2 = new HashMap<>();
    Type resolvedGenericArray = invokeResolve(context, contextRawType, genericArrayType, visited2);

    // Since resolveTypeVariable returns the type variable itself, no change expected
    assertSame(genericArrayType, resolvedGenericArray);
  }

  @Test
    @Timeout(8000)
  void testResolve_withParameterizedType_resolvesArguments() throws Exception {
    // Create a ParameterizedType mock with type arguments to test resolution
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Type ownerType = null;
    Type rawType = Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};

    when(parameterizedType.getOwnerType()).thenReturn(ownerType);
    when(parameterizedType.getRawType()).thenReturn(rawType);
    when(parameterizedType.getActualTypeArguments()).thenReturn(typeArgs);

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(null, Map.class, parameterizedType, visited);

    // Because all type arguments are concrete classes, resolved should be the same instance (no changes)
    assertSame(parameterizedType, resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_withWildcardType_resolvesBounds() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override public Type[] getUpperBounds() { return new Type[] {Number.class}; }
      @Override public Type[] getLowerBounds() { return new Type[0]; }
      @Override public String toString() { return "? extends Number"; }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(null, Object.class, wildcardType, visited);

    // Because upper bound is Number and no changes expected, resolved should be the same instance
    assertSame(wildcardType, resolved);

    // Now test with wildcard with lower bound
    WildcardType wildcardWithLower = new WildcardType() {
      @Override public Type[] getUpperBounds() { return new Type[] {Object.class}; }
      @Override public Type[] getLowerBounds() { return new Type[] {Integer.class}; }
      @Override public String toString() { return "? super Integer"; }
    };

    Type resolvedLower = invokeResolve(null, Object.class, wildcardWithLower, visited);

    assertSame(wildcardWithLower, resolvedLower);
  }

  @Test
    @Timeout(8000)
  void testResolve_withOtherType_breaksLoopAndReturns() throws Exception {
    Type toResolve = String.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(null, Object.class, toResolve, visited);

    assertSame(toResolve, resolved);
  }
}