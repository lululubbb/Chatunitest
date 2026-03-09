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

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class TypeToken_133_3Test {

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withSingleExpectedClass() throws Exception {
    Type token = String.class;
    Class<?>[] expected = new Class<?>[] { Integer.class };

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String message = error.getMessage();
    assertEquals(true, message.contains("Unexpected type. Expected one of: "));
    assertEquals(true, message.contains(Integer.class.getName()));
    assertEquals(true, message.contains(token.getClass().getName()));
    assertEquals(true, message.contains(token.toString()));
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withMultipleExpectedClasses() throws Exception {
    Type token = Double.class;
    Class<?>[] expected = new Class<?>[] { Integer.class, String.class };

    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    AssertionError error = (AssertionError) method.invoke(null, token, expected);

    String message = error.getMessage();
    assertEquals(true, message.contains("Unexpected type. Expected one of: "));
    for (Class<?> clazz : expected) {
      assertEquals(true, message.contains(clazz.getName()));
    }
    assertEquals(true, message.contains(token.getClass().getName()));
    assertEquals(true, message.contains(token.toString()));
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
    assertEquals("Unexpected type. Expected one of: but got: "
        + token.getClass().getName() + ", for type token: " + token.toString() + '.', message);
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError_withNullToken_throwsException() throws Exception {
    Class<?>[] expected = new Class<?>[] { Integer.class };
    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    // token is null, so token.getClass() will throw NullPointerException during method execution
    assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      method.invoke(null, (Type) null, expected);
    });
  }
}