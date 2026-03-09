package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

class $Gson$Types_291_3Test {

  @Test
    @Timeout(8000)
  void testDeclaringClassOf_withClassGenericDeclaration() throws Exception {
    // Create a TypeVariable from a known class with generics
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];

    // Use reflection to access private method declaringClassOf
    var method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Class<?> result = (Class<?>) method.invoke(null, typeVariable);

    assertEquals(SampleClass.class, result);
  }

  @Test
    @Timeout(8000)
  void testDeclaringClassOf_withNonClassGenericDeclaration() throws Exception {
    // Create a TypeVariable with a GenericDeclaration that is not a Class
    TypeVariable<?> typeVariable = new TypeVariableMock();

    // Use reflection to access private method declaringClassOf
    var method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    Class<?> result = (Class<?>) method.invoke(null, typeVariable);

    assertNull(result);
  }

  // Helper class with generic parameter
  static class SampleClass<T> {}

  // Mock TypeVariable with GenericDeclaration not instance of Class
  static class TypeVariableMock implements TypeVariable<GenericDeclaration> {

    @Override
    public Type[] getBounds() {
      return new Type[0];
    }

    @Override
    public GenericDeclaration getGenericDeclaration() {
      return new GenericDeclarationMock();
    }

    @Override
    public String getName() {
      return "mock";
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return null;
    }

    @Override
    public Annotation[] getAnnotations() {
      return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
      return new Annotation[0];
    }

    @Override
    public AnnotatedType[] getAnnotatedBounds() {
      return new AnnotatedType[0];
    }
  }

  // Mock GenericDeclaration with required methods
  static class GenericDeclarationMock implements GenericDeclaration {

    @Override
    public java.lang.reflect.TypeVariable<?>[] getTypeParameters() {
      return new java.lang.reflect.TypeVariable<?>[0];
    }

    @Override
    public Annotation[] getAnnotations() {
      return new Annotation[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return null;
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
      return new Annotation[0];
    }
  }
}