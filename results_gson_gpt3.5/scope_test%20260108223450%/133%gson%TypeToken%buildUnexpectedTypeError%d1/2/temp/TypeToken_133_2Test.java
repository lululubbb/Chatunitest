package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeToken_133_2Test {

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_singleExpectedType() throws Exception {
    Type token = String.class;
    Class<?>[] expected = {Integer.class};

    // Use reflection to access private static method
    var method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String expectedMessage =
        "Unexpected type. Expected one of: java.lang.Integer, but got: java.lang.Class, for type token: class java.lang.String.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_multipleExpectedTypes() throws Exception {
    Type token = Double.class;
    Class<?>[] expected = {Integer.class, String.class, Number.class};

    var method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String expectedMessage =
        "Unexpected type. Expected one of: java.lang.Integer, java.lang.String, java.lang.Number, but got: java.lang.Class, for type token: class java.lang.Double.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_noExpectedTypes() throws Exception {
    Type token = Boolean.class;
    Class<?>[] expected = new Class<?>[0];

    var method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String expectedMessage =
        "Unexpected type. Expected one of: but got: java.lang.Class, for type token: class java.lang.Boolean.";
    assertEquals(expectedMessage, error.getMessage());
  }

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_nullToken() throws Exception {
    Type token = null;
    Class<?>[] expected = {Integer.class};

    var method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(null, token, expected));
    // The cause should be NullPointerException
    assertEquals(NullPointerException.class, thrown.getCause().getClass());
  }
}