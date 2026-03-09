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

class $Gson$Types_291_5Test {

  @Test
    @Timeout(8000)
  void declaringClassOf_whenGenericDeclarationIsClass_returnsClass() throws Exception {
    // Create a TypeVariable whose generic declaration is a Class
    TypeVariable<Class<DummyClass>> typeVariable = DummyClass.class.getTypeParameters()[0];

    // Access private method declaringClassOf via reflection
    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Class<?> result = (Class<?>) method.invoke(null, typeVariable);

    assertEquals(DummyClass.class, result);
  }

  @Test
    @Timeout(8000)
  void declaringClassOf_whenGenericDeclarationNotClass_returnsNull() throws Exception {
    // Create a TypeVariable whose generic declaration is not a Class
    // We create a dummy GenericDeclaration implementation to simulate this case
    TypeVariable<?> typeVariable = createTypeVariableWithNonClassGenericDeclaration();

    Method method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Class<?> result = (Class<?>) method.invoke(null, typeVariable);

    assertNull(result);
  }

  private static TypeVariable<?> createTypeVariableWithNonClassGenericDeclaration() throws Exception {
    // Use a dummy method in DummyGenericDeclaration class and get its type parameter
    class DummyGenericDeclaration {
      @SuppressWarnings("unused")
      public <T> void dummyMethod() {}
    }

    Method dummyMethod = DummyGenericDeclaration.class.getDeclaredMethod("dummyMethod");
    TypeVariable<Method> typeVariable = dummyMethod.getTypeParameters()[0];
    return typeVariable;
  }

  // Dummy class with a type parameter for testing
  static class DummyClass<T> {}
}