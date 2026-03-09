package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Cart_60_2Test {

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_Null() throws Exception {
    Method method = Cart.class.getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);
    String result = (String) method.invoke(null, (Object) null);
    assertEquals("null", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_ClassType() throws Exception {
    Method method = Cart.class.getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);

    String resultString = (String) method.invoke(null, String.class);
    assertEquals("String", resultString);

    String resultInteger = (String) method.invoke(null, Integer.class);
    assertEquals("Integer", resultInteger);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_ParameterizedType() throws Exception {
    Method method = Cart.class.getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);

    // Create a ParameterizedType instance for List<String>
    ParameterizedType listStringType = new ParameterizedType() {
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

    String result = (String) method.invoke(null, listStringType);
    assertEquals("List<String>", result);

    // Create a ParameterizedType instance for List<?>
    ParameterizedType listWildcardType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {new WildcardType() {
          @Override
          public Type[] getUpperBounds() {
            return new Type[] {Object.class};
          }

          @Override
          public Type[] getLowerBounds() {
            return new Type[0];
          }
        }};
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

    String resultWildcard = (String) method.invoke(null, listWildcardType);
    assertEquals("List<?>", resultWildcard);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_WildcardType() throws Exception {
    Method method = Cart.class.getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);

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

    String result = (String) method.invoke(null, wildcardType);
    assertEquals("?", result);
  }

  @Test
    @Timeout(8000)
  public void testGetSimpleTypeName_OtherType() throws Exception {
    Method method = Cart.class.getDeclaredMethod("getSimpleTypeName", Type.class);
    method.setAccessible(true);

    Type otherType = new Type() {
      @Override
      public String toString() {
        return "CustomTypeToString";
      }
    };

    String result = (String) method.invoke(null, otherType);
    assertEquals("CustomTypeToString", result);
  }
}