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
  public void testEquals_classType() {
    Type a = String.class;
    Type b = String.class;
    Type c = Integer.class;
    assertTrue($Gson$Types.equals(a, b));
    assertFalse($Gson$Types.equals(a, c));
    assertFalse($Gson$Types.equals(a, null));
    assertFalse($Gson$Types.equals(null, b));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_equal() throws Exception {
    ParameterizedType pt1 = createParameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = createParameterizedType(String.class, null, Integer.class);
    assertTrue($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_owner() throws Exception {
    ParameterizedType pt1 = createParameterizedType(String.class, String.class, Integer.class);
    ParameterizedType pt2 = createParameterizedType(String.class, null, Integer.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_rawType() throws Exception {
    ParameterizedType pt1 = createParameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = createParameterizedType(Integer.class, null, Integer.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_notEqual_typeArgs() throws Exception {
    ParameterizedType pt1 = createParameterizedType(String.class, null, Integer.class);
    ParameterizedType pt2 = createParameterizedType(String.class, null, String.class);
    assertFalse($Gson$Types.equals(pt1, pt2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_parameterizedType_vs_nonParameterizedType() {
    ParameterizedType pt = mock(ParameterizedType.class);
    when(pt.getOwnerType()).thenReturn(null);
    when(pt.getRawType()).thenReturn(String.class);
    when(pt.getActualTypeArguments()).thenReturn(new Type[] {Integer.class});
    Type nonPT = String.class;
    assertFalse($Gson$Types.equals(pt, nonPT));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_equal() throws Exception {
    GenericArrayType gat1 = createGenericArrayType(String.class);
    GenericArrayType gat2 = createGenericArrayType(String.class);
    assertTrue($Gson$Types.equals(gat1, gat2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_notEqual() throws Exception {
    GenericArrayType gat1 = createGenericArrayType(String.class);
    GenericArrayType gat2 = createGenericArrayType(Integer.class);
    assertFalse($Gson$Types.equals(gat1, gat2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_genericArrayType_vs_nonGenericArrayType() throws Exception {
    GenericArrayType gat = createGenericArrayType(String.class);
    Type nonGat = String.class;
    assertFalse($Gson$Types.equals(gat, nonGat));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_equal() throws Exception {
    WildcardType w1 = createWildcardType(new Type[] {Number.class}, new Type[] {});
    WildcardType w2 = createWildcardType(new Type[] {Number.class}, new Type[] {});
    assertTrue($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_upperBounds() throws Exception {
    WildcardType w1 = createWildcardType(new Type[] {Number.class}, new Type[] {});
    WildcardType w2 = createWildcardType(new Type[] {Integer.class}, new Type[] {});
    assertFalse($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_notEqual_lowerBounds() throws Exception {
    WildcardType w1 = createWildcardType(new Type[] {Number.class}, new Type[] {Integer.class});
    WildcardType w2 = createWildcardType(new Type[] {Number.class}, new Type[] {});
    assertFalse($Gson$Types.equals(w1, w2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_wildcardType_vs_nonWildcardType() throws Exception {
    WildcardType w = createWildcardType(new Type[] {Number.class}, new Type[] {});
    Type nonW = String.class;
    assertFalse($Gson$Types.equals(w, nonW));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_equal() throws Exception {
    TypeVariable<Class<TestClass>> varA = TestClass.class.getTypeParameters()[0];
    TypeVariable<Class<TestClass>> varB = TestClass.class.getTypeParameters()[0];
    assertTrue($Gson$Types.equals(varA, varB));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentDeclaration() throws Exception {
    TypeVariable<Class<TestClass>> varA = TestClass.class.getTypeParameters()[0];
    TypeVariable<Class<OtherClass>> varB = OtherClass.class.getTypeParameters()[0];
    assertFalse($Gson$Types.equals(varA, varB));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_notEqual_differentName() throws Exception {
    TypeVariable<Class<TestClass>> varA = TestClass.class.getTypeParameters()[0];
    TypeVariable<Class<TestClass2>> varB = TestClass2.class.getTypeParameters()[0];
    assertFalse($Gson$Types.equals(varA, varB));
  }

  @Test
    @Timeout(8000)
  public void testEquals_typeVariable_vs_nonTypeVariable() throws Exception {
    TypeVariable<Class<TestClass>> varA = TestClass.class.getTypeParameters()[0];
    Type nonVar = String.class;
    assertFalse($Gson$Types.equals(varA, nonVar));
  }

  @Test
    @Timeout(8000)
  public void testEquals_unsupportedTypes() {
    Type a = mock(Type.class);
    Type b = mock(Type.class);
    assertFalse($Gson$Types.equals(a, b));
  }

  // Helper methods to create ParameterizedType, GenericArrayType, WildcardType

  private ParameterizedType createParameterizedType(final Type rawType, final Type ownerType, final Type... typeArguments) {
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
        return $Gson$Types.equals(this, o);
      }

      @Override
      public int hashCode() {
        return Arrays.hashCode(typeArguments) ^ rawType.hashCode() ^ (ownerType == null ? 0 : ownerType.hashCode());
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        if (ownerType != null) {
          sb.append(ownerType.getTypeName()).append("$");
          if (ownerType instanceof ParameterizedType) {
            sb.append(rawType.getTypeName().replace(((ParameterizedType) ownerType).getRawType().getTypeName() + "$", ""));
          } else {
            sb.append(rawType.getTypeName());
          }
        } else {
          sb.append(rawType.getTypeName());
        }
        if (typeArguments != null && typeArguments.length > 0) {
          sb.append("<");
          boolean first = true;
          for (Type t : typeArguments) {
            if (!first) sb.append(", ");
            sb.append(t.getTypeName());
            first = false;
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  private GenericArrayType createGenericArrayType(final Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }

      @Override
      public boolean equals(Object o) {
        return $Gson$Types.equals(this, o);
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

  private WildcardType createWildcardType(final Type[] upperBounds, final Type[] lowerBounds) {
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
        return $Gson$Types.equals(this, o);
      }

      @Override
      public int hashCode() {
        return Arrays.hashCode(upperBounds) ^ Arrays.hashCode(lowerBounds);
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder("?");
        if (lowerBounds.length > 0) {
          sb.append(" super ");
          boolean first = true;
          for (Type t : lowerBounds) {
            if (!first) sb.append(" & ");
            sb.append(t.getTypeName());
            first = false;
          }
        } else if (!Arrays.equals(upperBounds, new Type[] {Object.class})) {
          sb.append(" extends ");
          boolean first = true;
          for (Type t : upperBounds) {
            if (!first) sb.append(" & ");
            sb.append(t.getTypeName());
            first = false;
          }
        }
        return sb.toString();
      }
    };
  }

  // Test classes for TypeVariable tests
  static class TestClass<T> {}
  static class OtherClass<U> {}
  static class TestClass2<S> {}

}