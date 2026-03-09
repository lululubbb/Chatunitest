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
  public void testEquals_bothNull() {
    assertTrue($Gson$Types.equals(null, null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameObject() {
    Type type = String.class;
    assertTrue($Gson$Types.equals(type, type));
  }

  @Test
    @Timeout(8000)
  public void testEquals_classTypes_equal() {
    Type a = Integer.class;
    Type b = Integer.class;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_classTypes_notEqual() {
    Type a = Integer.class;
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_classAndNonClass() {
    Type a = Integer.class;
    Type b = mock(ParameterizedType.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_equal() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class, Integer.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_owner() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(null, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_rawType() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class, String.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_typeArguments() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    ParameterizedType b = createParameterizedType(String.class, Double.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_bNotParameterizedType() throws Exception {
    ParameterizedType a = createParameterizedType(String.class, Integer.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_equal() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    GenericArrayType b = createGenericArrayType(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_notEqual_componentType() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    GenericArrayType b = createGenericArrayType(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_bNotGenericArrayType() throws Exception {
    GenericArrayType a = createGenericArrayType(String.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_equal() throws Exception {
    WildcardType a = createWildcardType(new Type[] {Number.class}, new Type[] {});
    WildcardType b = createWildcardType(new Type[] {Number.class}, new Type[] {});
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_upperBounds() throws Exception {
    WildcardType a = createWildcardType(new Type[] {Number.class}, new Type[] {});
    WildcardType b = createWildcardType(new Type[] {Integer.class}, new Type[] {});
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_lowerBounds() throws Exception {
    WildcardType a = createWildcardType(new Type[] {Number.class}, new Type[] {Integer.class});
    WildcardType b = createWildcardType(new Type[] {Number.class}, new Type[] {Double.class});
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_bNotWildcardType() throws Exception {
    WildcardType a = createWildcardType(new Type[] {Number.class}, new Type[] {});
    Type b = Number.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_equal() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "T");
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentDeclaration() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(AnotherDummyClass.class, "T");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentName() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "U");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_bNotTypeVariable() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_otherTypes() {
    Type a = new Type() {};
    Type b = new Type() {};
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper to create ParameterizedType
  private static ParameterizedType createParameterizedType(final Type ownerType, final Type typeArg) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {typeArg};
      }

      @Override
      public Type getRawType() {
        return java.util.Map.class;
      }

      @Override
      public Type getOwnerType() {
        return ownerType;
      }
    };
  }

  // Helper to create GenericArrayType
  private static GenericArrayType createGenericArrayType(final Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }
    };
  }

  // Helper to create WildcardType
  private static WildcardType createWildcardType(final Type[] upperBounds, final Type[] lowerBounds) {
    return new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return upperBounds.length == 0 ? new Type[] {Object.class} : upperBounds;
      }

      @Override
      public Type[] getLowerBounds() {
        return lowerBounds;
      }
    };
  }

  // Helper to get TypeVariable by reflection
  private static TypeVariable<?> getTypeVariable(Class<?> clazz, String name) throws Exception {
    for (TypeVariable<?> tv : clazz.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        return tv;
      }
    }
    throw new NoSuchFieldException("TypeVariable " + name + " not found in " + clazz);
  }

  // Dummy classes with type parameters for TypeVariable tests
  private static class DummyClass<T, U> {}
  private static class AnotherDummyClass<T> {}
}