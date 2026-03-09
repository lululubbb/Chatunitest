package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Cart_60_1Test {

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_NullType() {
    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(null);
    assertEquals("null", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_ClassType() {
    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(String.class);
    assertEquals("String", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_ParameterizedType() throws Exception {
    // Create a ParameterizedType representing List<String>
    ParameterizedType pType = new ParameterizedType() {
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

    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(pType);
    assertEquals("List<String>", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_ParameterizedType_MultipleArguments() throws Exception {
    // Create a ParameterizedType representing Comparable<List<String>>
    ParameterizedType inner = new ParameterizedType() {
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

    ParameterizedType outer = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { inner };
      }

      @Override
      public Type getRawType() {
        return Comparable.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(outer);
    assertEquals("Comparable<List<String>>", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_WildcardType() {
    WildcardType wildcard = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[0];
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };

    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(wildcard);
    assertEquals("?", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_OtherType() {
    Type anonType = new Type() {
      @Override
      public String toString() {
        return "customTypeToString";
      }
    };
    String result = com.google.gson.examples.android.model.Cart.getSimpleTypeName(anonType);
    assertEquals("customTypeToString", result);
  }
}