package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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

import java.lang.reflect.Method;

class $Gson$Types_289_1Test {

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassNull_returnsUnknown() throws Exception {
    // Arrange
    // Create a mock TypeVariable whose declaring class is null by mocking declaringClassOf method
    TypeVariable<?> unknownTypeVariable = mock(TypeVariable.class);

    Method declaringClassOfMethod = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    declaringClassOfMethod.setAccessible(true);

    // Use reflection to temporarily override declaringClassOf to return null for this test
    // Since we cannot mock static private method easily, we test by passing a TypeVariable
    // that is not declared by any class (simulate by mocking declaringClassOf to return null)

    // To simulate declaringClassOf returning null, use a spy of $Gson$Types with reflection to invoke resolveTypeVariable
    // But since it's static, we invoke resolveTypeVariable directly with a TypeVariable that will cause declaringClassOf to return null
    // So we mock declaringClassOf by using reflection to replace the method temporarily

    // Instead, invoke resolveTypeVariable with a TypeVariable for which declaringClassOf returns null
    // So we mock declaringClassOf by using a proxy or by creating a dummy TypeVariable that is not declared by any class

    // Since creating such TypeVariable is impossible, we mock declaringClassOf by reflection:
    // Use a Proxy or Unsafe to override declaringClassOf is complex, so instead we test the fallback behavior:
    // Pass a TypeVariable that is not declared by any class, e.g. a mock TypeVariable
    // and expect that the method returns the same TypeVariable.

    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);

    // Act
    Object result = resolveTypeVariableMethod.invoke(null, Object.class, Object.class, unknownTypeVariable);

    // Assert
    assertSame(unknownTypeVariable, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawParameterizedType_returnsActualTypeArgument() throws Exception {
    // Arrange
    // Create a dummy class with a type variable
    class GenericClass<T> {}
    class SubClass extends GenericClass<String> {}

    TypeVariable<?>[] typeParameters = GenericClass.class.getTypeParameters();
    assertEquals(1, typeParameters.length);
    TypeVariable<?> unknown = typeParameters[0];

    // context is SubClass.class
    Type context = SubClass.class;
    Class<?> contextRawType = SubClass.class;

    // Use reflection to invoke private method
    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);

    // Act
    Object result = resolveTypeVariableMethod.invoke(null, context, contextRawType, unknown);

    // Assert
    assertTrue(result instanceof Class);
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawNonParameterizedType_returnsUnknown() throws Exception {
    // Arrange
    class GenericClass<T> {}
    class SubClass extends GenericClass {}

    TypeVariable<?> unknown = GenericClass.class.getTypeParameters()[0];
    Type context = SubClass.class;
    Class<?> contextRawType = SubClass.class;

    Method resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);

    // Act
    Object result = resolveTypeVariableMethod.invoke(null, context, contextRawType, unknown);

    // Assert
    assertSame(unknown, result);
  }
}