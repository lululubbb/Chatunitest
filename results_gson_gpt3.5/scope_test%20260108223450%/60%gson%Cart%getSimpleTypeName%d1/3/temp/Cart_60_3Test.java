package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class Cart_60_3Test {

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_null() {
    assertEquals("null", Cart.getSimpleTypeName(null));
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_class() {
    assertEquals("String", Cart.getSimpleTypeName(String.class));
    assertEquals("Integer", Cart.getSimpleTypeName(Integer.class));
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_parameterizedType_singleArg() throws Exception {
    // Create a ParameterizedType representing List<String>
    ParameterizedType listStringType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
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
    String result = Cart.getSimpleTypeName(listStringType);
    assertEquals("List<String>", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_parameterizedType_multipleArgs() throws Exception {
    // Create a ParameterizedType representing Map<String, Integer>
    ParameterizedType mapType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class, Integer.class };
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
    String result = Cart.getSimpleTypeName(mapType);
    assertEquals("Map<String,Integer>", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_wildcardType() throws Exception {
    // Create a WildcardType representing ?
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] { Object.class };
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    String result = Cart.getSimpleTypeName(wildcardType);
    assertEquals("?", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_otherTypeToString() throws Exception {
    // Create a custom Type that is not Class, ParameterizedType, or WildcardType
    Type customType = new Type() {
      @Override
      public String toString() {
        return "customTypeToString";
      }
    };
    String result = Cart.getSimpleTypeName(customType);
    assertEquals("customTypeToString", result);
  }
}