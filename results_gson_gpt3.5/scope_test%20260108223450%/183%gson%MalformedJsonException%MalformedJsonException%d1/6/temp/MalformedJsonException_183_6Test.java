package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class MalformedJsonException_183_6Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "Test message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndThrowable() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithThrowable() {
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorUsingReflection() throws Exception {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    MalformedJsonException ex = constructor.newInstance("Reflected message");
    assertEquals("Reflected message", ex.getMessage());
  }
}