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

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class $Gson$Types_ResolveTest {

  // Helper to invoke private static resolve method with visitedTypeVariables map
  private Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
  }

  @Test
    @Timeout(8000)
  void resolve_TypeVariable_resolvesSuccessfully() throws Exception {
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];
    Type context = GenericClass.class;
    Class<?> rawType = GenericClass.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    // Provide a context where T resolves to String
    Type resolved = invokeResolve(context, rawType, typeVariable, visited);

    // It will resolve to itself because no mapping exists in resolveTypeVariable (default)
    assertNotNull(resolved);
    assertTrue(resolved instanceof TypeVariable);
    assertSame(typeVariable, resolved);
    assertTrue(visited.containsKey(typeVariable));
    assertSame(resolved, visited.get(typeVariable));
  }

  @Test
    @Timeout(8000)
  void resolve_ClassArrayType_componentTypeResolved() throws Exception {
    Class<?> contextRawType = String[].class;
    Type context = String[].class;
    Class<?> arrayClass = String[].class;
    Type toResolve = arrayClass;

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type result = invokeResolve(context, contextRawType, toResolve, visited);

    assertSame(arrayClass, result);
  }

  @Test
    @Timeout(8000)
  void resolve_GenericArrayType_componentTypeChanged() throws Exception {
    // Create a GenericArrayType with component type as TypeVariable that resolves to String
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];

    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return typeVariable;
      }
      @Override
      public String toString() {
        return typeVariable.getTypeName() + "[]";
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericArrayType)) return false;
        GenericArrayType that = (GenericArrayType) o;
        return getGenericComponentType().equals(that.getGenericComponentType());
      }
      @Override
      public int hashCode() {
        return getGenericComponentType().hashCode();
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(GenericClass.class, GenericClass.class, genericArrayType, visited);

    // Since resolveTypeVariable returns the variable itself, newComponentType equals componentType, so returns original
    assertSame(genericArrayType, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_ParameterizedType_ownerTypeAndArgsResolved() throws Exception {
    // Create a ParameterizedType: Map<String, T>
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];

    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, typeVariable};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public String toString() {
        return "Map<String, " + typeVariable.getTypeName() + ">";
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return (getOwnerType() == null ? that.getOwnerType() == null : getOwnerType().equals(that.getOwnerType()))
            && getRawType().equals(that.getRawType())
            && java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^ (getOwnerType() == null ? 0 : getOwnerType().hashCode()) ^ getRawType().hashCode();
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(GenericClass.class, GenericClass.class, parameterizedType, visited);

    // Since resolveTypeVariable returns variable itself, no change expected
    assertSame(parameterizedType, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WildcardType_lowerBoundChanged() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getLowerBounds() {
        return new Type[] {String.class};
      }
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }
      @Override
      public String toString() {
        return "? super String";
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WildcardType)) return false;
        WildcardType that = (WildcardType) o;
        return java.util.Arrays.equals(getLowerBounds(), that.getLowerBounds())
            && java.util.Arrays.equals(getUpperBounds(), that.getUpperBounds());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getLowerBounds()) ^ java.util.Arrays.hashCode(getUpperBounds());
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(Object.class, Object.class, wildcardType, visited);

    // Since lower bound resolves to itself, expect original wildcard type
    assertSame(wildcardType, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WildcardType_upperBoundChanged() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }
      @Override
      public String toString() {
        return "? extends Object";
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WildcardType)) return false;
        WildcardType that = (WildcardType) o;
        return java.util.Arrays.equals(getLowerBounds(), that.getLowerBounds())
            && java.util.Arrays.equals(getUpperBounds(), that.getUpperBounds());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getLowerBounds()) ^ java.util.Arrays.hashCode(getUpperBounds());
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(Object.class, Object.class, wildcardType, visited);

    // Since upper bound resolves to itself, expect original wildcard type
    assertSame(wildcardType, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_OtherType_returnsToResolve() throws Exception {
    Type toResolve = String.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Type resolved = invokeResolve(Object.class, Object.class, toResolve, visited);

    assertSame(toResolve, resolved);
  }
}