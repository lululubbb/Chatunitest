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

public class Cart_60_5Test {

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_null() {
    assertEquals("null", Cart.getSimpleTypeName(null));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_Class() {
    assertEquals("String", Cart.getSimpleTypeName(String.class));
    assertEquals("Integer", Cart.getSimpleTypeName(Integer.class));
    assertEquals("Cart", Cart.getSimpleTypeName(Cart.class));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_ParameterizedType() throws NoSuchFieldException, SecurityException {
    // Using field with parameterized type: lineItems -> List<LineItem>
    Field lineItemsField = Cart.class.getDeclaredField("lineItems");
    Type type = lineItemsField.getGenericType();
    String expected = "List<LineItem>";
    assertEquals(expected, Cart.getSimpleTypeName(type));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_ParameterizedType_MultiArg() {
    // Create a ParameterizedType instance for Map<String, Integer> via reflection proxy
    ParameterizedType mapType = new ParameterizedType() {
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
    String expected = "Map<String,Integer>";
    assertEquals(expected, Cart.getSimpleTypeName(mapType));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_WildcardType() {
    // Create a WildcardType instance with no bounds
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Object.class};
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    assertEquals("?", Cart.getSimpleTypeName(wildcardType));
  }

  @Test
    @Timeout(8000)
  void testGetSimpleTypeName_OtherType() {
    // Create a custom Type implementation returning toString
    Type customType = new Type() {
      @Override
      public String toString() {
        return "customTypeToString";
      }
    };
    assertEquals("customTypeToString", Cart.getSimpleTypeName(customType));
  }
}