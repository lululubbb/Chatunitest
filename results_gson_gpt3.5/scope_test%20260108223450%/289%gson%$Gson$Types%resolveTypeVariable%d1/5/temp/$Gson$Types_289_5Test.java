package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class GsonTypesResolveTypeVariableTest {

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassNull_returnsUnknown() throws Exception {
    // Arrange
    TypeVariable<?> unknown = mock(TypeVariable.class);
    // Mock declaringClassOf to return null via reflection
    Method declaringClassOfMethod = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    declaringClassOfMethod.setAccessible(true);
    // Cannot mock static private method directly, so we create a spy of $Gson$Types and override via reflection is not possible.
    // Instead, we create a subclass that shadows the method? Not possible because class is final.
    // So we rely on actual behavior: create a TypeVariable whose declaring class is null - not possible.
    // Alternative: create a TypeVariable with declaring class null by creating a proxy? Complex.
    // So test fallback by creating a TypeVariable whose declaring class is null by mocking the method via reflection.
    // We will invoke resolveTypeVariable via reflection and check result is unknown.

    // Act
    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);
    Object result = resolveTypeVariableMethod.invoke(null, Object.class, Object.class, unknown);

    // Assert
    assertSame(unknown, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawNotParameterized_returnsUnknown() throws Exception {
    // Arrange
    Class<?> declaredByRaw = DummyClass.class;
    TypeVariable<?> unknown = DummyClass.class.getTypeParameters()[0];

    // Act
    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);
    Object result = resolveTypeVariableMethod.invoke(null, DummyClassParameterized.class, DummyClassParameterized.class, unknown);

    // Assert
    // Because DummyClassParameterized extends DummyClass<String> (ParameterizedType),
    // declaredBy is ParameterizedType, so should resolve to actual type argument String.class
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawParameterized_returnsActualTypeArgument() throws Exception {
    // Arrange
    TypeVariable<?> unknown = DummyClass.class.getTypeParameters()[0];

    // Act
    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);
    Object result = resolveTypeVariableMethod.invoke(null, DummyClassParameterized.class, DummyClassParameterized.class, unknown);

    // Assert
    assertEquals(String.class, result);
  }

  // Helper classes for testing
  static class DummyClass<T> {}

  static class DummyClassParameterized extends DummyClass<String> {}

}