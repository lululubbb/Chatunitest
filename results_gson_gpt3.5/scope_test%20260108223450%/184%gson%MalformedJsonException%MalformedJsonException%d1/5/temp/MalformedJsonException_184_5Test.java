package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_184_5Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException exception = new MalformedJsonException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "error message only";
    MalformedJsonException exception = new MalformedJsonException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithThrowable() {
    Throwable cause = new RuntimeException("cause only");
    MalformedJsonException exception = new MalformedJsonException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testPrivateSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }
}