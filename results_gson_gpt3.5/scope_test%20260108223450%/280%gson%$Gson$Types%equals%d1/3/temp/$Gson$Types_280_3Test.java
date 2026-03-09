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

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;

public class GsonTypesEqualsTest {

  @Test
    @Timeout(8000)
  public void testEquals_sameReference() {
    Type t = String.class;
    assertTrue($Gson$Types.equals(t, t));
    assertTrue($Gson$Types.equals(null, null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_classInstances() {
    Type a = String.class;
    Type b = String.class;
    Type c = Integer.class;
    assertTrue($Gson$Types.equals(a, b));
    assertFalse($Gson$Types.equals(a, c));
    assertFalse($Gson$Types.equals(a, null));
    assertFalse($Gson$Types.equals(null, c));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_equal() throws Exception {
    ParameterizedType pt1 = parameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = parameterizedType(String.class, null, Integer.class);
    assertTrue($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_owner() throws Exception {
    ParameterizedType pt1 = parameterizedType(String.class, String.class, Integer.class);
    ParameterizedType pt2 = parameterizedType(String.class, null, Integer.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_rawType() throws Exception {
    ParameterizedType pt1 = parameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = parameterizedType(Integer.class, null, Integer.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_typeArguments() throws Exception {
    ParameterizedType pt1 = parameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = parameterizedType(String.class, null, String.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_vs_nonParameterizedType() throws Exception {
    ParameterizedType pt = parameterizedType(String.class, null, Integer.class);
    assertFalse($Gson$Types.equals(pt, String.class));
    assertFalse($Gson$Types.equals(String.class, pt));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_equal() throws Exception {
    GenericArrayType ga1 = genericArrayType(String.class);
    GenericArrayType ga2 = genericArrayType(String.class);
    assertTrue($Gson$Types.equals(ga1, ga2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_notEqual_component() throws Exception {
    GenericArrayType ga1 = genericArrayType(String.class);
    GenericArrayType ga2 = genericArrayType(Integer.class);
    assertFalse($Gson$Types.equals(ga1, ga2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_vs_nonGenericArrayType() throws Exception {
    GenericArrayType ga = genericArrayType(String.class);
    assertFalse($Gson$Types.equals(ga, String.class));
    assertFalse($Gson$Types.equals(String.class, ga));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_equal() throws Exception {
    WildcardType w1 = wildcardType(new Type[]{Object.class}, new Type[]{});
    WildcardType w2 = wildcardType(new Type[]{Object.class}, new Type[]{});
    assertTrue($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_bounds() throws Exception {
    WildcardType w1 = wildcardType(new Type[]{Object.class}, new Type[]{});
    WildcardType w2 = wildcardType(new Type[]{String.class}, new Type[]{});
    assertFalse($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_lowerBounds() throws Exception {
    WildcardType w1 = wildcardType(new Type[]{Object.class}, new Type[]{String.class});
    WildcardType w2 = wildcardType(new Type[]{Object.class}, new Type[]{Integer.class});
    assertFalse($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_vs_nonWildcardType() throws Exception {
    WildcardType w = wildcardType(new Type[]{Object.class}, new Type[]{});
    assertFalse($Gson$Types.equals(w, String.class));
    assertFalse($Gson$Types.equals(String.class, w));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_equal() throws Exception {
    TypeVariable<?> v1 = getTypeVariableFromClass(ExampleClass.class, "T");
    TypeVariable<?> v2 = getTypeVariableFromClass(ExampleClass.class, "T");
    assertTrue($Gson$Types.equals(v1, v2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentName() throws Exception {
    TypeVariable<?> v1 = getTypeVariableFromClass(ExampleClass.class, "T");
    TypeVariable<?> v2 = getTypeVariableFromClass(AnotherExampleClass.class, "U");
    assertFalse($Gson$Types.equals(v1, v2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentDeclaration() throws Exception {
    TypeVariable<?> v1 = getTypeVariableFromClass(ExampleClass.class, "T");
    TypeVariable<?> v2 = getTypeVariableFromClass(DifferentDeclarationClass.class, "T");
    assertFalse($Gson$Types.equals(v1, v2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_vs_nonTypeVariable() throws Exception {
    TypeVariable<?> v = getTypeVariableFromClass(ExampleClass.class, "T");
    assertFalse($Gson$Types.equals(v, String.class));
    assertFalse($Gson$Types.equals(String.class, v));
  }

  @Test
    @Timeout(8000)
  public void testEquals_unsupportedTypes() {
    Type a = new Type() {};
    Type b = new Type() {};
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper methods to create ParameterizedType, GenericArrayType, WildcardType, and get TypeVariable

  private ParameterizedType parameterizedType(final Type rawType, final Type ownerType, final Type... typeArguments) {
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

      @Override
      public boolean equals(Object o) {
        return $Gson$Types.equals(this, o instanceof ParameterizedType ? (ParameterizedType) o : null);
      }

      @Override
      public int hashCode() {
        return Arrays.hashCode(typeArguments) ^ rawType.hashCode() ^ (ownerType == null ? 0 : ownerType.hashCode());
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>) rawType).getName());
        if (typeArguments.length > 0) {
          sb.append("<");
          for (int i = 0; i < typeArguments.length; i++) {
            if (i != 0) sb.append(", ");
            sb.append(typeArguments[i].getTypeName());
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  private GenericArrayType genericArrayType(final Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }

      @Override
      public boolean equals(Object o) {
        return $Gson$Types.equals(this, o instanceof GenericArrayType ? (GenericArrayType) o : null);
      }

      @Override
      public int hashCode() {
        return componentType.hashCode();
      }

      @Override
      public String toString() {
        return componentType.getTypeName() + "[]";
      }
    };
  }

  private WildcardType wildcardType(final Type[] upperBounds, final Type[] lowerBounds) {
    return new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return upperBounds;
      }

      @Override
      public Type[] getLowerBounds() {
        return lowerBounds;
      }

      @Override
      public boolean equals(Object o) {
        return $Gson$Types.equals(this, o instanceof WildcardType ? (WildcardType) o : null);
      }

      @Override
      public int hashCode() {
        return Arrays.hashCode(upperBounds) ^ Arrays.hashCode(lowerBounds);
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder("?");
        if (lowerBounds.length > 0) {
          sb.append(" super ").append(lowerBounds[0].getTypeName());
        } else if (upperBounds.length > 0 && !upperBounds[0].equals(Object.class)) {
          sb.append(" extends ").append(upperBounds[0].getTypeName());
        }
        return sb.toString();
      }
    };
  }

  private TypeVariable<?> getTypeVariableFromClass(Class<?> clazz, String name) throws Exception {
    for (TypeVariable<?> tv : clazz.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        return tv;
      }
    }
    throw new NoSuchElementException("No type variable " + name + " in " + clazz);
  }

  // Helper generic classes for TypeVariable tests
  private static class ExampleClass<T> {}
  private static class AnotherExampleClass<U> {}
  private static class DifferentDeclarationClass<T> {}

}