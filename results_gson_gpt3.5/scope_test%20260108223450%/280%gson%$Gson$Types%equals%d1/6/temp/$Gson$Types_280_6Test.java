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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

class GsonTypesEqualsTest {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    Type a = String.class;
    Type b = a;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_bothNull() {
    Type a = null;
    Type b = null;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_classTypeDifferent() {
    Type a = String.class;
    Type b = Integer.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_classTypeSame() {
    Type a = String.class;
    Type b = String.class;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_equal() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class, Integer.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_owner() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(null, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_rawType() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class, String.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_typeArgumentsLength() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notParameterizedType() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_equal() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    GenericArrayType b = createGenericArrayType(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_notEqual_componentType() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    GenericArrayType b = createGenericArrayType(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_notGenericArrayType() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_equal() throws Exception {
    WildcardType a = createWildcardType(new Type[] {String.class}, new Type[] {});
    WildcardType b = createWildcardType(new Type[] {String.class}, new Type[] {});
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_notEqual_upperBounds() throws Exception {
    WildcardType a = createWildcardType(new Type[] {String.class}, new Type[] {});
    WildcardType b = createWildcardType(new Type[] {Integer.class}, new Type[] {});
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_notEqual_lowerBounds() throws Exception {
    WildcardType a = createWildcardType(new Type[] {String.class}, new Type[] {Integer.class});
    WildcardType b = createWildcardType(new Type[] {String.class}, new Type[] {});
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_notWildcardType() throws Exception {
    WildcardType a = createWildcardType(new Type[] {String.class}, new Type[] {});
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_equal() throws Exception {
    TypeVariable<?> a = createTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = createTypeVariable(DummyClass.class, "T");
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_notEqual_differentName() throws Exception {
    TypeVariable<?> a = createTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = createTypeVariable(DummyClass.class, "U");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_notEqual_differentDeclaringClass() throws Exception {
    TypeVariable<?> a = createTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = createTypeVariable(DummyClass2.class, "T");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_notTypeVariable() throws Exception {
    TypeVariable<?> a = createTypeVariable(DummyClass.class, "T");
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_unsupportedTypes() {
    Type a = new Type() {};
    Type b = new Type() {};
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper method to create ParameterizedType instances
  private static ParameterizedType createParameterizedType(Type ownerType, Type... typeArguments) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return typeArguments;
      }

      @Override
      public Type getRawType() {
        return String.class;
      }

      @Override
      public Type getOwnerType() {
        return ownerType;
      }
    };
  }

  // Helper method to create GenericArrayType instances
  private static GenericArrayType createGenericArrayType(Type componentType) {
    return () -> componentType;
  }

  // Helper method to create WildcardType instances
  private static WildcardType createWildcardType(Type[] upperBounds, Type[] lowerBounds) {
    return new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return upperBounds;
      }

      @Override
      public Type[] getLowerBounds() {
        return lowerBounds;
      }
    };
  }

  // Helper method to create TypeVariable instances using reflection
  private static TypeVariable<?> createTypeVariable(Class<?> declaringClass, String name) throws Exception {
    // Using a dummy generic class to get TypeVariable by name
    for (TypeVariable<?> tv : DummyGeneric.class.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        // We cannot create new TypeVariable instances easily, return existing matching name variable
        return tv;
      }
    }
    // fallback: create a proxy TypeVariable with correct name and declaring class
    return new TypeVariable<Type>() {
      @Override
      public Type[] getBounds() {
        return new Type[] {Object.class};
      }

      @Override
      public GenericDeclaration getGenericDeclaration() {
        return declaringClass;
      }

      @Override
      public String getName() {
        return name;
      }

      @Override
      public AnnotatedType[] getAnnotatedBounds() {
        return new AnnotatedType[0];
      }
    };
  }

  // Dummy classes for TypeVariable tests
  static class DummyGeneric<T, U> {}
  static class DummyClass {}
  static class DummyClass2 {}
}