package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class TypeTokenBuildUnexpectedTypeErrorTest {

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_singleExpectedClass() throws Exception {
    Type token = String.class;

    Class<?>[] expected = new Class<?>[] { Integer.class };

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    // invoke returns Object, but method returns AssertionError, so cast
    AssertionError error = (AssertionError) method.invoke(null, token, (Object) expected);

    String expectedMessage = "Unexpected type. Expected one of: java.lang.Integer, but got: java.lang.Class, for type token: class java.lang.String.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_multipleExpectedClasses() throws Exception {
    Type token = Double.class;

    Class<?>[] expected = new Class<?>[] { Integer.class, String.class };

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, (Object) expected);

    String expectedMessage = "Unexpected type. Expected one of: java.lang.Integer, java.lang.String, but got: java.lang.Class, for type token: class java.lang.Double.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_noExpectedClasses() throws Exception {
    Type token = Float.class;

    Class<?>[] expected = new Class<?>[] {};

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, (Object) expected);

    String expectedMessage = "Unexpected type. Expected one of: but got: java.lang.Class, for type token: class java.lang.Float.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withParameterizedType() throws Exception {
    Type token = new ParameterizedType() {
      @Override
      public java.lang.reflect.Type[] getActualTypeArguments() {
        return new java.lang.reflect.Type[] { String.class };
      }

      @Override
      public java.lang.reflect.Type getRawType() {
        return java.util.Map.class;
      }

      @Override
      public java.lang.reflect.Type getOwnerType() {
        return null;
      }

      @Override
      public String toString() {
        return "java.util.Map<java.lang.String>";
      }
    };

    Class<?>[] expected = new Class<?>[] { java.util.Map.class };

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, (Object) expected);

    String expectedMessage = "Unexpected type. Expected one of: java.util.Map, but got: " + token.getClass().getName() + ", for type token: java.util.Map<java.lang.String>.";
    assertEquals(expectedMessage, error.getMessage());
  }
}