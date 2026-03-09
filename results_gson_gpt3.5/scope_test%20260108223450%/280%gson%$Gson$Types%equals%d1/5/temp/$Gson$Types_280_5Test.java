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
  void testEquals_bothNull() {
    assertTrue($Gson$Types.equals(null, null));
  }

  @Test
    @Timeout(8000)
  void testEquals_sameTypeInstance() {
    Type type = String.class;
    assertTrue($Gson$Types.equals(type, type));
  }

  @Test
    @Timeout(8000)
  void testEquals_classEquals() {
    Type a = String.class;
    Type b = String.class;
    Type c = Integer.class;
    assertTrue($Gson$Types.equals(a, b));
    assertFalse($Gson$Types.equals(a, c));
    assertFalse($Gson$Types.equals(a, null));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_equal() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(String.class, Integer.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_owner() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedTypeWithOwner(String.class, String.class, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_rawType() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(Integer.class, Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_notEqual_typeArguments() throws Exception {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    ParameterizedType b = newParameterizedType(String.class, String.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_parameterizedType_vs_nonParameterizedType() {
    ParameterizedType a = newParameterizedType(String.class, Integer.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_equal() throws Exception {
    GenericArrayType a = arrayOf(String.class);
    GenericArrayType b = arrayOf(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_notEqual() throws Exception {
    GenericArrayType a = arrayOf(String.class);
    GenericArrayType b = arrayOf(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_genericArrayType_vs_nonGenericArrayType() throws Exception {
    GenericArrayType a = arrayOf(String.class);
    Type b = String[].class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_equal() throws Exception {
    WildcardType a = subtypeOf(String.class);
    WildcardType b = subtypeOf(String.class);
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_notEqual_upperBounds() throws Exception {
    WildcardType a = subtypeOf(String.class);
    WildcardType b = subtypeOf(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_notEqual_lowerBounds() throws Exception {
    WildcardType a = supertypeOf(String.class);
    WildcardType b = supertypeOf(Integer.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_wildcardType_vs_nonWildcardType() throws Exception {
    WildcardType a = subtypeOf(String.class);
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_equal() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "T");
    assertTrue($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_notEqual_differentName() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(DummyClass.class, "U");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_notEqual_differentDeclaration() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    TypeVariable<?> b = getTypeVariable(AnotherDummyClass.class, "T");
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_typeVariable_vs_nonTypeVariable() throws Exception {
    TypeVariable<?> a = getTypeVariable(DummyClass.class, "T");
    Type b = String.class;
    assertFalse($Gson$Types.equals(a, b));
  }

  @Test
    @Timeout(8000)
  void testEquals_unknownType() {
    Type a = new Type() {};
    Type b = new Type() {};
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper methods

  // Create ParameterizedType instance for testing without owner
  private ParameterizedType newParameterizedType(Type raw, Type... args) {
    return $Gson$Types.newParameterizedTypeWithOwner(null, raw, args);
  }

  // Create ParameterizedType instance for testing with owner
  private ParameterizedType newParameterizedTypeWithOwner(Type owner, Type raw, Type... args) {
    return $Gson$Types.newParameterizedTypeWithOwner(owner, raw, args);
  }

  // Create GenericArrayType instance for testing
  private GenericArrayType arrayOf(Type componentType) {
    return $Gson$Types.arrayOf(componentType);
  }

  // Create WildcardType subtypeOf for testing
  private WildcardType subtypeOf(Type bound) {
    return $Gson$Types.subtypeOf(bound);
  }

  // Create WildcardType supertypeOf for testing
  private WildcardType supertypeOf(Type bound) {
    return $Gson$Types.supertypeOf(bound);
  }

  // Get TypeVariable by reflection from a generic class
  private TypeVariable<?> getTypeVariable(Class<?> clazz, String name) {
    for (TypeVariable<?> tv : clazz.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        return tv;
      }
    }
    fail("TypeVariable " + name + " not found in " + clazz);
    return null;
  }

  // Dummy generic classes for TypeVariable tests
  static class DummyClass<T, U> {}
  static class AnotherDummyClass<T> {}
}