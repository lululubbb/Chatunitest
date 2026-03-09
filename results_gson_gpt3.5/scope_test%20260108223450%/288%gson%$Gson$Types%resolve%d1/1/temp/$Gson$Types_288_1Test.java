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

class $Gson$Types_288_1Test {

  @Test
    @Timeout(8000)
  void testResolve_TypeVariable_resolvesToItselfWhenNoMapping() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];
    Type context = SampleClass.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    // Use reflection to invoke private method resolve
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visited);

    // Assert
    assertEquals(typeVariable, resolved);
    assertEquals(Void.TYPE, visited.get(typeVariable));
  }

  @Test
    @Timeout(8000)
  void testResolve_TypeVariable_resolvesToMapping() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];
    Type context = SampleClass.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();
    visited.put(typeVariable, String.class);

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visited);

    // Assert
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_ClassArray_resolvesComponentType() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;
    Class<?> arrayClass = String[].class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, arrayClass, visited);

    // Assert
    assertEquals(arrayClass, resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_GenericArrayType_resolvesComponentType() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
      @Override
      public String toString() {
        return "GenericArrayType";
      }
    };
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, genericArrayType, visited);

    // Assert
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_ParameterizedType_resolvesArgumentsAndOwner() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;

    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override
      public Type getRawType() {
        return SampleClass.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, parameterizedType, visited);

    // Assert
    assertNotNull(resolved);
    assertTrue(resolved instanceof ParameterizedType);
  }

  @Test
    @Timeout(8000)
  void testResolve_WildcardType_resolvesLowerBound() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;

    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getLowerBounds() {
        return new Type[] {String.class};
      }
      @Override
      public Type[] getUpperBounds() {
        return new Type[0];
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, wildcardType, visited);

    // Assert
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_WildcardType_resolvesUpperBound() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;

    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
    };

    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, wildcardType, visited);

    // Assert
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  void testResolve_OtherType_returnsSame() throws Exception {
    // Setup
    Class<?> contextRawType = SampleClass.class;
    Type context = SampleClass.class;
    Type toResolve = String.class;
    Map<TypeVariable<?>, Type> visited = new HashMap<>();

    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
        Map.class);
    resolveMethod.setAccessible(true);

    // Act
    Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);

    // Assert
    assertEquals(toResolve, resolved);
  }

  // Sample generic class to get TypeVariable for testing
  static class SampleClass<T> {
  }
}