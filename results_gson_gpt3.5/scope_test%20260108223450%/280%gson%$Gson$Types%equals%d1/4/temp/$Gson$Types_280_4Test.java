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

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;

public class GsonTypesEqualsTest {

  @Test
    @Timeout(8000)
  public void testEquals_BothNull() {
    assertTrue($Gson$Types.equals(null, null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_SameObject() {
    Type t = String.class;
    assertTrue($Gson$Types.equals(t, t));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothClass_Equal() {
    Type a = String.class;
    Type b = String.class;
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_BothClass_NotEqual() {
    Type a = String.class;
    Type b = Integer.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_ParameterizedType_Equal() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(String.class, Integer.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_ParameterizedType_NotEqual_DifferentRawType() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(Integer.class, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_ParameterizedType_NotEqual_DifferentOwnerType() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(null, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_ParameterizedType_NotEqual_DifferentTypeArguments() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(String.class, String.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_ParameterizedType_B_NotParameterizedType() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_GenericArrayType_Equal() throws Exception {
    GenericArrayType a = arrayOfType(String.class);
    GenericArrayType b = arrayOfType(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_GenericArrayType_NotEqual_ComponentType() throws Exception {
    GenericArrayType a = arrayOfType(String.class);
    GenericArrayType b = arrayOfType(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_GenericArrayType_B_NotGenericArrayType() throws Exception {
    GenericArrayType a = arrayOfType(String.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_WildcardType_Equal() throws Exception {
    WildcardType a = subtypeOfType(String.class);
    WildcardType b = subtypeOfType(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_WildcardType_NotEqual_UpperBounds() throws Exception {
    WildcardType a = subtypeOfType(String.class);
    WildcardType b = subtypeOfType(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_WildcardType_NotEqual_LowerBounds() throws Exception {
    WildcardType a = supertypeOfType(String.class);
    WildcardType b = supertypeOfType(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_WildcardType_B_NotWildcardType() throws Exception {
    WildcardType a = subtypeOfType(String.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_TypeVariable_Equal() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "T");
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_TypeVariable_NotEqual_DifferentName() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "U");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_TypeVariable_NotEqual_DifferentGenericDeclaration() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(AnotherDummyClass.class, "T");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_TypeVariable_B_NotTypeVariable() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_UnsupportedTypes() {
    Type a = new Type() {};
    Type b = new Type() {};
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper to create ParameterizedType instances
  private ParameterizedType newParameterizedType(final Type owner, final Type raw, final Type... args) {
    final Type ownerType = owner;
    final Type rawType = raw;
    final Type[] typeArguments = args;
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return typeArguments;
      }

      @Override
      public Type getRawType() {
        return rawType;
      }

      @Override
      public Type getOwnerType() {
        return ownerType;
      }
    };
  }

  private ParameterizedType newParameterizedType(final Type raw, final Type... args) {
    return newParameterizedType(
      (raw instanceof Class<?> && ((Class<?>) raw).getEnclosingClass() != null)
          ? ((Class<?>) raw).getEnclosingClass()
          : null,
      raw,
      args);
  }

  // Helper to create GenericArrayType instances
  private GenericArrayType arrayOfType(final Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }
    };
  }

  // Helper to create WildcardType subtypeOf
  private WildcardType subtypeOfType(final Type bound) {
    return new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {bound};
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
  }

  // Helper to create WildcardType supertypeOf
  private WildcardType supertypeOfType(final Type bound) {
    return new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[] {bound};
      }
    };
  }

  // Helper to get TypeVariable by name from a class
  private TypeVariable<?> getTypeVariable(Class<?> clazz, String name) {
    for (TypeVariable<?> tv : clazz.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        return tv;
      }
    }
    throw new IllegalArgumentException("No type variable " + name + " in " + clazz);
  }

  // Dummy classes for TypeVariable testing
  static class DummyClass<T, U> {}
  static class AnotherDummyClass<T> {}
}