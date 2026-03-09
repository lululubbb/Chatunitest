package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
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
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class $Gson$Types_291_1Test {

  private static Class<?> invokeDeclaringClassOf(TypeVariable<?> typeVariable) throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);
    return (Class<?>) method.invoke(null, typeVariable);
  }

  static class GenericClass<T> {}
  interface GenericInterface<T> {}

  @Test
    @Timeout(8000)
  void declaringClassOf_returnsClass_whenGenericDeclarationIsClass() throws Exception {
    TypeVariable<Class<GenericClass>>[] typeParameters = GenericClass.class.getTypeParameters();
    assertEquals(GenericClass.class, invokeDeclaringClassOf(typeParameters[0]));
  }

  @Test
    @Timeout(8000)
  void declaringClassOf_returnsNull_whenGenericDeclarationIsNotClass() throws Exception {
    TypeVariable<Class<GenericInterface>>[] typeParameters = GenericInterface.class.getTypeParameters();
    // The generic declaration for interface type variables is Class as well, so to get a non-Class generic declaration,
    // we create a local type variable from a method or constructor.

    class Local {
      <T> void method() {}
    }
    Method method = Local.class.getDeclaredMethod("method");
    TypeVariable<Method> methodTypeVariable = method.getTypeParameters()[0];

    assertNull(invokeDeclaringClassOf(methodTypeVariable));
  }
}