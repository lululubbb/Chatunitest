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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypeToken_133_1Test {

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withSingleExpectedClass() throws Exception {
    Type token = String.class;
    Class<?>[] expected = new Class<?>[]{Integer.class};

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String message = error.getMessage();
    assertTrue(message.contains("Unexpected type. Expected one of: "));
    assertTrue(message.contains(Integer.class.getName()));
    assertTrue(message.contains(token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withMultipleExpectedClasses() throws Exception {
    Type token = Double.class;
    Class<?>[] expected = new Class<?>[]{Integer.class, String.class, Number.class};

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String message = error.getMessage();
    assertTrue(message.contains("Unexpected type. Expected one of: "));
    Arrays.stream(expected).forEach(clazz -> assertTrue(message.contains(clazz.getName())));
    assertTrue(message.contains(token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withEmptyExpectedClasses() throws Exception {
    Type token = Float.class;
    Class<?>[] expected = new Class<?>[0];

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String message = error.getMessage();
    assertTrue(message.contains("Unexpected type. Expected one of: "));
    // No expected class names should be present except the fixed string
    assertTrue(message.contains("but got: "));
    assertTrue(message.contains(token.getClass().getName()));
    assertTrue(message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withNullToken_throwsNullPointerException() throws Exception {
    Class<?>[] expected = new Class<?>[]{Integer.class};

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    // Passing null token will cause NullPointerException on token.getClass()
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, (Object) null, expected);
    });
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withNullExpectedArray() throws Exception {
    Type token = String.class;

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    // Passing null as expected varargs should cause NullPointerException in the for loop
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, token, (Object) null);
    });
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }
}