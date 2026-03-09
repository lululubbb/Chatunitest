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

class $Gson$TypesResolveTest {

  private Map<TypeVariable<?>, Type> visitedTypeVariables;

  private Class<?> contextRawType;
  private Type context;

  @BeforeEach
  void setUp() {
    visitedTypeVariables = new HashMap<>();
    contextRawType = SampleClass.class;
    context = SampleClass.class;
  }

  // Sample generic class to obtain TypeVariables and generic types
  static class SampleClass<T> {
    List<T[]> listField;
    Map<String, T> mapField;
    T field;
  }

  // Access private static method resolve(Type, Class<?>, Type, Map<TypeVariable<?>, Type>)
  private Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
  }

  @Test
    @Timeout(8000)
  void resolve_WithTypeVariable_ResolvesToConcreteType() throws Exception {
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];
    // Simulate resolveTypeVariable returning String.class for T
    // We cannot mock private static resolveTypeVariable directly,
    // but we can test resolve behavior with a known mapping
    visitedTypeVariables.put(typeVariable, Void.TYPE);

    // We add mapping for T -> String.class to visitedTypeVariables to simulate resolution
    visitedTypeVariables.put(typeVariable, String.class);

    Type resolved = invokeResolve(context, contextRawType, typeVariable, visitedTypeVariables);
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithTypeVariable_CircularReference_ReturnsOriginal() throws Exception {
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];
    // Put Void.TYPE to simulate in-progress resolving
    visitedTypeVariables.put(typeVariable, Void.TYPE);

    Type resolved = invokeResolve(context, contextRawType, typeVariable, visitedTypeVariables);
    // Should return the original type variable to avoid infinite recursion
    assertSame(typeVariable, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithClassArray_ResolvesComponentType() throws Exception {
    Class<?> arrayClass = String[][].class;
    Type resolved = invokeResolve(context, contextRawType, arrayClass, visitedTypeVariables);
    // Should resolve component type recursively and return same type because String is concrete
    assertEquals(arrayClass, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithGenericArrayType_ResolvesComponentType() throws Exception {
    // Create a GenericArrayType for T[]
    TypeVariable<?> tVar = SampleClass.class.getTypeParameters()[0];
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return tVar;
      }
      @Override
      public String toString() {
        return tVar + "[]";
      }
    };

    // Provide mapping T -> String.class
    visitedTypeVariables.put(tVar, String.class);

    Type resolved = invokeResolve(context, contextRawType, genericArrayType, visitedTypeVariables);
    // Should resolve to arrayOf(String.class)
    assertTrue(resolved instanceof GenericArrayType || resolved instanceof Class<?>);
    if (resolved instanceof GenericArrayType) {
      GenericArrayType gat = (GenericArrayType) resolved;
      assertEquals(String.class, gat.getGenericComponentType());
    } else if (resolved instanceof Class<?>) {
      Class<?> clazz = (Class<?>) resolved;
      assertTrue(clazz.isArray());
      assertEquals(String.class, clazz.getComponentType());
    }
  }

  @Test
    @Timeout(8000)
  void resolve_WithParameterizedType_ResolvesOwnerAndArgs() throws Exception {
    // Create ParameterizedType: Map<String, T>
    TypeVariable<?> tVar = SampleClass.class.getTypeParameters()[0];
    ParameterizedType mapType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, tVar};
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
        return "Map<String, T>";
      }
    };

    // Map T -> Integer.class
    visitedTypeVariables.put(tVar, Integer.class);

    Type resolved = invokeResolve(context, contextRawType, mapType, visitedTypeVariables);
    assertTrue(resolved instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) resolved;
    Type[] args = pt.getActualTypeArguments();
    assertEquals(String.class, args[0]);
    assertEquals(Integer.class, args[1]);
  }

  @Test
    @Timeout(8000)
  void resolve_WithWildcardType_ResolvesBounds() throws Exception {
    // Create WildcardType with upper bound T
    TypeVariable<?> tVar = SampleClass.class.getTypeParameters()[0];
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {tVar};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
      @Override
      public String toString() {
        return "? extends T";
      }
    };

    // Map T -> Number.class
    visitedTypeVariables.put(tVar, Number.class);

    Type resolved = invokeResolve(context, contextRawType, wildcard, visitedTypeVariables);
    // Should resolve upper bound to Number and return subtypeOf(Number)
    assertNotSame(wildcard, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithWildcardType_LowerBound_Resolves() throws Exception {
    // Create WildcardType with lower bound T
    TypeVariable<?> tVar = SampleClass.class.getTypeParameters()[0];
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[] {tVar};
      }
      @Override
      public String toString() {
        return "? super T";
      }
    };

    // Map T -> Integer.class
    visitedTypeVariables.put(tVar, Integer.class);

    Type resolved = invokeResolve(context, contextRawType, wildcard, visitedTypeVariables);
    // Should resolve lower bound to Integer and return supertypeOf(Integer)
    assertNotSame(wildcard, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithClassOtherThanArray_ReturnsSame() throws Exception {
    Type toResolve = String.class;
    Type resolved = invokeResolve(context, contextRawType, toResolve, visitedTypeVariables);
    assertSame(toResolve, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_WithNonHandledType_ReturnsSame() throws Exception {
    Type toResolve = new Type() {};
    Type resolved = invokeResolve(context, contextRawType, toResolve, visitedTypeVariables);
    assertSame(toResolve, resolved);
  }
}