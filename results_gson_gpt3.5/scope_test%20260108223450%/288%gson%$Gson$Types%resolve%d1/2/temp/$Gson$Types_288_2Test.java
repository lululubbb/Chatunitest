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

  @BeforeEach
  void setUp() {
    visitedTypeVariables = new HashMap<>();
  }

  @Test
    @Timeout(8000)
  void resolve_withTypeVariable_resolvesToConcreteType() throws Exception {
    // Arrange
    Class<?> contextRawType = SampleGeneric.class;
    Type context = SampleGeneric.class.getMethod("getT").getGenericReturnType();
    TypeVariable<?> typeVariable = SampleGeneric.class.getTypeParameters()[0];

    // Act
    Type resolved = invokeResolve(context, contextRawType, typeVariable, visitedTypeVariables);

    // Assert
    assertEquals(String.class, resolved);
    assertEquals(String.class, visitedTypeVariables.get(typeVariable));
  }

  @Test
    @Timeout(8000)
  void resolve_withTypeVariable_recursive_returnsOriginal() throws Exception {
    // Arrange
    Class<?> contextRawType = RecursiveGeneric.class;
    TypeVariable<?> typeVariable = RecursiveGeneric.class.getTypeParameters()[0];
    visitedTypeVariables.put(typeVariable, Void.TYPE);

    // Act
    Type resolved = invokeResolve(null, contextRawType, typeVariable, visitedTypeVariables);

    // Assert
    assertEquals(typeVariable, resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_withArrayClass_resolvesComponentType() throws Exception {
    // Arrange
    Class<?> contextRawType = String[].class;
    Type toResolve = String[][].class;

    // Act
    Type resolved = invokeResolve(null, contextRawType, toResolve, visitedTypeVariables);

    // Assert
    assertTrue(resolved instanceof GenericArrayType || resolved instanceof Class);
    Type componentType = resolved instanceof GenericArrayType
        ? ((GenericArrayType) resolved).getGenericComponentType()
        : ((Class<?>) resolved).getComponentType();
    assertEquals(String[].class.getComponentType(), componentType);
  }

  @Test
    @Timeout(8000)
  void resolve_withGenericArrayType_resolvesComponentType() throws Exception {
    // Arrange
    Type genericArrayType = new $Gson$Types.GenericArrayTypeImpl(String.class);
    Class<?> contextRawType = String.class;

    // Act
    Type resolved = invokeResolve(null, contextRawType, genericArrayType, visitedTypeVariables);

    // Assert
    assertTrue(resolved instanceof GenericArrayType || resolved instanceof Class);
    Type componentType = resolved instanceof GenericArrayType
        ? ((GenericArrayType) resolved).getGenericComponentType()
        : ((Class<?>) resolved).getComponentType();
    assertEquals(String.class, componentType);
  }

  @Test
    @Timeout(8000)
  void resolve_withParameterizedType_resolvesArgumentsAndOwner() throws Exception {
    // Arrange
    ParameterizedType paramType = (ParameterizedType) SampleGeneric.class.getMethod("getParameterized").getGenericReturnType();
    Class<?> contextRawType = SampleGeneric.class;

    // Act
    Type resolved = invokeResolve(null, contextRawType, paramType, visitedTypeVariables);

    // Assert
    assertTrue(resolved instanceof ParameterizedType);
    ParameterizedType resolvedParam = (ParameterizedType) resolved;
    assertEquals(paramType.getRawType(), resolvedParam.getRawType());
    assertEquals(paramType.getOwnerType(), resolvedParam.getOwnerType());
    assertEquals(paramType.getActualTypeArguments().length, resolvedParam.getActualTypeArguments().length);
  }

  @Test
    @Timeout(8000)
  void resolve_withWildcardType_resolvesBounds() throws Exception {
    // Arrange
    WildcardType wildcard = (WildcardType) SampleGeneric.class.getMethod("getWildcard").getGenericReturnType();
    Class<?> contextRawType = SampleGeneric.class;

    // Act
    Type resolved = invokeResolve(null, contextRawType, wildcard, visitedTypeVariables);

    // Assert
    assertNotNull(resolved);
  }

  @Test
    @Timeout(8000)
  void resolve_withOtherType_returnsSame() throws Exception {
    // Arrange
    Type toResolve = String.class;

    // Act
    Type resolved = invokeResolve(null, String.class, toResolve, visitedTypeVariables);

    // Assert
    assertSame(toResolve, resolved);
  }

  private Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
    Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
    resolveMethod.setAccessible(true);
    return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
  }

  // Helper classes for testing
  static class SampleGeneric<T> {
    public T getT() {return null;}
    public ParameterizedType getParameterized() throws NoSuchMethodException {
      Method m = SampleGeneric.class.getDeclaredMethod("getT");
      return (ParameterizedType) m.getGenericReturnType();
    }
    public WildcardType getWildcard() throws NoSuchMethodException {
      Method m = SampleGenericWildcard.class.getDeclaredMethod("method");
      return (WildcardType) m.getGenericReturnType();
    }
  }

  static class SampleGenericWildcard {
    public List<? extends Number> method() {return null;}
  }

  static class RecursiveGeneric<T extends RecursiveGeneric<T>> {}

  // Minimal GenericArrayType implementation for testing
  static class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;
    GenericArrayTypeImpl(Type componentType) {
      this.componentType = componentType;
    }
    @Override
    public Type getGenericComponentType() {
      return componentType;
    }
    @Override
    public boolean equals(Object o) {
      if (o instanceof GenericArrayType) {
        GenericArrayType that = (GenericArrayType) o;
        return Objects.equals(componentType, that.getGenericComponentType());
      }
      return false;
    }
    @Override
    public int hashCode() {
      return componentType.hashCode();
    }
    @Override
    public String toString() {
      return componentType + "[]";
    }
  }
}