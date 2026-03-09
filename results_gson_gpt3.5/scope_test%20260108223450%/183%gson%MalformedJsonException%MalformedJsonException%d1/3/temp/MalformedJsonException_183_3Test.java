package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_183_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_Message() {
    String message = "Test message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Message_Throwable() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Throwable() {
    Throwable cause = new RuntimeException("Cause only");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructorsViaReflection() throws Exception {
    Constructor<MalformedJsonException> ctor = MalformedJsonException.class.getDeclaredConstructor(String.class);
    ctor.setAccessible(true);
    MalformedJsonException ex = ctor.newInstance("reflection message");
    assertEquals("reflection message", ex.getMessage());
  }
}