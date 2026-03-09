package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class Cart_60_6Test {

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_null() {
    assertEquals("null", Cart.getSimpleTypeName(null));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_class() {
    assertEquals("String", Cart.getSimpleTypeName(String.class));
    assertEquals("Integer", Cart.getSimpleTypeName(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_parameterizedType_singleArg() throws NoSuchFieldException, SecurityException {
    // Use reflection to get a ParameterizedType instance for List<String>
    Field field = Sample.class.getDeclaredField("stringList");
    Type type = field.getGenericType();
    String expected = "List<String>";
    assertEquals(expected, Cart.getSimpleTypeName(type));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_parameterizedType_multipleArgs() throws NoSuchFieldException, SecurityException {
    // Use reflection to get a ParameterizedType instance for Map<String,Integer>
    Field field = Sample.class.getDeclaredField("stringIntegerMap");
    Type type = field.getGenericType();
    String expected = "Map<String,Integer>";
    assertEquals(expected, Cart.getSimpleTypeName(type));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_wildcardType() throws NoSuchFieldException, SecurityException {
    // Use reflection to get a WildcardType instance for List<?>
    Field field = Sample.class.getDeclaredField("wildcardList");
    Type type = field.getGenericType();
    // The List<?> is a ParameterizedType, its argument is a WildcardType
    ParameterizedType pType = (ParameterizedType) type;
    Type[] args = pType.getActualTypeArguments();
    assertEquals(1, args.length);
    Type wildcardType = args[0];
    assertEquals("?", Cart.getSimpleTypeName(wildcardType));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_otherTypeToString() {
    // Create a custom Type implementation that is none of the above
    Type customType = new Type() {
      @Override
      public String toString() {
        return "customTypeToString";
      }
    };
    assertEquals("customTypeToString", Cart.getSimpleTypeName(customType));
  }

  // Helper class to hold fields with generic types for reflection
  private static class Sample {
    List<String> stringList;
    Map<String, Integer> stringIntegerMap;
    List<?> wildcardList;
  }
}