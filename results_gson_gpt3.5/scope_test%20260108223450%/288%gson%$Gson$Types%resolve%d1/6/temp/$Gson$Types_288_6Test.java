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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class $Gson$Types_ResolveTest {

  private Map<TypeVariable<?>, Type> visitedTypeVariables;

  @BeforeEach
  void setUp() {
    visitedTypeVariables = new HashMap<>();
  }

  @Test
    @Timeout(8000)
  void resolve_withTypeVariable_resolvesToType() throws Exception {
    // Prepare a TypeVariable from a generic class
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];

    // Create a context and contextRawType
    Type context = GenericClass.class;
    Class<?> contextRawType = GenericClass.class;

    // Spy on $Gson$Types to mock resolveTypeVariable
    // Using reflection to invoke private static method resolve
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    // We expect resolveTypeVariable to be called internally and return String.class
    // But since it's private, we can't mock it easily, so just test actual behavior
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visitedTypeVariables);

    // The resolved type should not be the original TypeVariable (unless unresolved)
    assertNotNull(resolved);
    assertTrue(resolved instanceof TypeVariable || resolved instanceof Class);
  }

  @Test
    @Timeout(8000)
  void resolve_withClassArray_resolvesComponent() throws Exception {
    Class<?> contextRawType = ArrayList.class;
    Type context = ArrayList.class;

    Class<?> toResolve = String[].class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visitedTypeVariables);

    assertNotNull(resolved);
    assertTrue(resolved instanceof Class<?> || resolved instanceof GenericArrayType);
    if (resolved instanceof Class<?>) {
      assertTrue(((Class<?>) resolved).isArray());
    }
  }

  @Test
    @Timeout(8000)
  void resolve_withGenericArrayType_resolvesComponent() throws Exception {
    // Create a GenericArrayType with a TypeVariable component
    class GenericClass<T> {}
    TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override public Type getGenericComponentType() {
        return typeVariable;
      }
      @Override public String toString() {
        return typeVariable + "[]";
      }
    };

    Type context = GenericClass.class;
    Class<?> contextRawType = GenericClass.class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, genericArrayType, visitedTypeVariables);

    assertNotNull(resolved);
    assertTrue(resolved instanceof GenericArrayType || resolved instanceof Class<?>);
  }

  @Test
    @Timeout(8000)
  void resolve_withParameterizedType_resolvesArgumentsAndOwner() throws Exception {
    // Create ParameterizedType: Map<String, Integer>
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override public Type getRawType() {
        return Map.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
      @Override public String toString() {
        return "Map<String,Integer>";
      }
    };

    Type context = HashMap.class;
    Class<?> contextRawType = HashMap.class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, parameterizedType, visitedTypeVariables);

    assertNotNull(resolved);
    assertTrue(resolved instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) resolved;
    assertEquals(Map.class, pt.getRawType());
    assertEquals(2, pt.getActualTypeArguments().length);
  }

  @Test
    @Timeout(8000)
  void resolve_withWildcardType_resolvesBounds() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
      @Override public Type[] getLowerBounds() {
        return new Type[] {};
      }
      @Override public String toString() {
        return "? extends Number";
      }
    };

    Type context = Object.class;
    Class<?> contextRawType = Object.class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, wildcardType, visitedTypeVariables);

    assertNotNull(resolved);
    assertTrue(resolved instanceof WildcardType);
  }

  @Test
    @Timeout(8000)
  void resolve_withWildcardType_lowerBound_resolves() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }
      @Override public Type[] getLowerBounds() {
        return new Type[] {Integer.class};
      }
      @Override public String toString() {
        return "? super Integer";
      }
    };

    Type context = Object.class;
    Class<?> contextRawType = Object.class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, wildcardType, visitedTypeVariables);

    assertNotNull(resolved);
    assertTrue(resolved instanceof WildcardType);
  }

  @Test
    @Timeout(8000)
  void resolve_withOtherType_returnsSame() throws Exception {
    Type context = Object.class;
    Class<?> contextRawType = Object.class;
    Type toResolve = String.class;

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visitedTypeVariables);

    assertSame(toResolve, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_withRecursiveTypeVariable_doesNotLoop() throws Exception {
    class Recursive<T extends Comparable<T>> {}
    TypeVariable<?> typeVariable = Recursive.class.getTypeParameters()[0];
    Type context = Recursive.class;
    Class<?> contextRawType = Recursive.class;

    visitedTypeVariables.put(typeVariable, Void.TYPE);

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod(
        "resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);

    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visitedTypeVariables);

    assertSame(typeVariable, resolved);
  }
}