package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class Cart_60_4Test {

  private Method getSimpleTypeNameMethod() throws Exception {
    Method method = Class.forName("com.google.gson.examples.android.model.Cart")
        .getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_null() throws Exception {
    Method method = getSimpleTypeNameMethod();
    String result = (String) method.invoke(null, new Object[] {null});
    assertEquals("null", result);
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_class() throws Exception {
    Method method = getSimpleTypeNameMethod();
    String result = (String) method.invoke(null, String.class);
    assertEquals("String", result);
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_parameterizedType() throws Exception {
    Method method = getSimpleTypeNameMethod();

    // Create a ParameterizedType instance for List<String>
    ParameterizedType pType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return List.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    String result = (String) method.invoke(null, pType);
    assertEquals("List<String>", result);
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_parameterizedType_multipleArgs() throws Exception {
    Method method = getSimpleTypeNameMethod();

    // ParameterizedType for Map<String, Integer>
    ParameterizedType pType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }

      @Override
      public Type getRawType() {
        return Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    String result = (String) method.invoke(null, pType);
    assertEquals("Map<String,Integer>", result);
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_wildcardType() throws Exception {
    Method method = getSimpleTypeNameMethod();

    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[0];
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };

    String result = (String) method.invoke(null, wildcardType);
    assertEquals("?", result);
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_otherType() throws Exception {
    Method method = getSimpleTypeNameMethod();

    // Create anonymous Type that is neither Class, ParameterizedType, nor WildcardType
    Type otherType = new Type() {
      @Override
      public String toString() {
        return "OtherTypeToString";
      }
    };

    String result = (String) method.invoke(null, otherType);
    assertEquals("OtherTypeToString", result);
  }
}