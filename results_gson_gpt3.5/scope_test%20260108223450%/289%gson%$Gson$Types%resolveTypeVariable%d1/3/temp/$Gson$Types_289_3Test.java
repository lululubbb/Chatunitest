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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class $Gson$Types_289_3Test {

  private Method resolveTypeVariableMethod;
  private Method declaringClassOfMethod;
  private Method getGenericSupertypeMethod;
  private Method indexOfMethod;

  @BeforeEach
  void setUp() throws Exception {
    Class<?> clazz = $Gson$Types.class;

    resolveTypeVariableMethod = clazz.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    resolveTypeVariableMethod.setAccessible(true);

    declaringClassOfMethod = clazz.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    declaringClassOfMethod.setAccessible(true);

    getGenericSupertypeMethod = clazz.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
    getGenericSupertypeMethod.setAccessible(true);

    indexOfMethod = clazz.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassOfReturnsNull_returnsUnknown() throws Throwable {
    // Create a proxy TypeVariable whose getGenericDeclaration returns null
    TypeVariable<?> typeVariable = createTypeVariableWithNoDeclaringClass();

    Object result = resolveTypeVariableMethod.invoke(null, Object.class, Object.class, typeVariable);
    assertSame(typeVariable, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawIsParameterizedType_returnsActualTypeArgument() throws Throwable {
    // Create a context where declaredByRaw is a generic class with parameters
    class GenericSuper<T> {}
    class Concrete extends GenericSuper<String> {}

    TypeVariable<Class<GenericSuper>>[] typeParameters = GenericSuper.class.getTypeParameters();
    assertEquals(1, typeParameters.length);
    TypeVariable<?> unknown = typeParameters[0];

    // context is Concrete.class, contextRawType is Concrete.class, unknown is T from GenericSuper
    Object result = resolveTypeVariableMethod.invoke(null, Concrete.class, Concrete.class, unknown);

    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawIsNotParameterizedType_returnsUnknown() throws Throwable {
    class NonParameterizedSuper {}
    class Sub extends NonParameterizedSuper {}

    TypeVariable<Class<NonParameterizedSuper>>[] typeParameters = NonParameterizedSuper.class.getTypeParameters();
    if (typeParameters.length == 0) {
      // create dummy TypeVariable via proxy for test
      TypeVariable<?> unknown = createTypeVariableWithNoDeclaringClass();

      Object result = resolveTypeVariableMethod.invoke(null, Sub.class, Sub.class, unknown);
      assertSame(unknown, result);
    } else {
      // fallback to test with existing type variable
      TypeVariable<?> unknown = typeParameters[0];
      Object result = resolveTypeVariableMethod.invoke(null, Sub.class, Sub.class, unknown);
      assertSame(unknown, result);
    }
  }

  private TypeVariable<?> createTypeVariableWithNoDeclaringClass() throws Exception {
    InvocationHandler handler = (proxy, method, args) -> {
      String methodName = method.getName();
      switch (methodName) {
        case "getBounds":
          return new Type[] {Object.class};
        case "getGenericDeclaration":
          return null;
        case "getName":
          return "T";
        case "getAnnotatedBounds":
          return new AnnotatedType[0];
        case "equals":
          return proxy == args[0];
        case "hashCode":
          return System.identityHashCode(proxy);
        case "toString":
          return "T";
        default:
          throw new UnsupportedOperationException("Method not implemented: " + methodName);
      }
    };

    @SuppressWarnings("unchecked")
    TypeVariable<?> proxy = (TypeVariable<?>) Proxy.newProxyInstance(
        TypeVariable.class.getClassLoader(),
        new Class<?>[] { TypeVariable.class },
        handler);
    return proxy;
  }
}