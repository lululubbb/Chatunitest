package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeTokenBuildUnexpectedTypeErrorTest {

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_withSingleExpectedClass() throws Exception {
    Type token = String.class;
    Class<?>[] expected = {Integer.class};

    AssertionError error = invokeBuildUnexpectedTypeError(token, expected);

    String message = error.getMessage();
    assertTrue(message.contains("Unexpected type. Expected one of: "));
    assertTrue(message.contains(Integer.class.getName()));
    assertTrue(message.contains(token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_withMultipleExpectedClasses() throws Exception {
    Type token = Integer.class;
    Class<?>[] expected = {String.class, Double.class, Long.class};

    AssertionError error = invokeBuildUnexpectedTypeError(token, expected);

    String message = error.getMessage();
    for (Class<?> clazz : expected) {
      assertTrue(message.contains(clazz.getName()));
    }
    assertTrue(message.contains(token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void buildUnexpectedTypeError_withNoExpectedClasses() throws Exception {
    Type token = Double.class;
    Class<?>[] expected = new Class<?>[0];

    AssertionError error = invokeBuildUnexpectedTypeError(token, expected);

    String message = error.getMessage();
    assertTrue(message.contains("Unexpected type. Expected one of: "));
    assertTrue(message.contains("but got: " + token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  private static AssertionError invokeBuildUnexpectedTypeError(Type token, Class<?>... expected) throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);
    // Wrap expected in Object[] to avoid varargs ambiguity
    return (AssertionError) method.invoke(null, token, (Object) expected);
  }
}