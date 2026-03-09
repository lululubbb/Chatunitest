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
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Method;

class $Gson$Types_declaringClassOf_Test {

  @Test
    @Timeout(8000)
  void declaringClassOf_returnsDeclaringClass_whenGenericDeclarationIsClass() throws Exception {
    // Create a TypeVariable from a real class with type parameters
    class GenericClass<T> {}
    TypeVariable<?>[] typeParameters = GenericClass.class.getTypeParameters();
    assertTrue(typeParameters.length > 0);
    TypeVariable<?> typeVariable = typeParameters[0];

    // Use reflection to access private static method declaringClassOf
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Object result = method.invoke(null, typeVariable);
    assertNotNull(result);
    assertTrue(result instanceof Class<?>);
    assertEquals(GenericClass.class, result);
  }

  @Test
    @Timeout(8000)
  void declaringClassOf_returnsNull_whenGenericDeclarationIsNotClass() throws Exception {
    // Create a TypeVariable whose GenericDeclaration is not a Class
    // For that, create a TypeVariable from a method or constructor

    class Holder {
      <T> void method() {}
    }
    Method methodRef = Holder.class.getDeclaredMethod("method");
    TypeVariable<?>[] methodTypeParameters = methodRef.getTypeParameters();
    assertTrue(methodTypeParameters.length > 0);
    TypeVariable<?> typeVariable = methodTypeParameters[0];

    // Use reflection to access private static method declaringClassOf
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Object result = method.invoke(null, typeVariable);
    // The genericDeclaration is the method, not a Class, so should return null
    assertNull(result);
  }
}