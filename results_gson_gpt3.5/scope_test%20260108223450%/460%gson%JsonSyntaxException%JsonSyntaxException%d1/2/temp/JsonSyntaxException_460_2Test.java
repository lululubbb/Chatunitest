package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class JsonSyntaxException_460_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_StringThrowable() {
    String message = "Error message";
    Throwable cause = new RuntimeException("Cause");
    JsonSyntaxException ex = new JsonSyntaxException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_String() {
    String message = "Only message";
    JsonSyntaxException ex = new JsonSyntaxException(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Throwable() {
    Throwable cause = new RuntimeException("Cause only");
    JsonSyntaxException ex = new JsonSyntaxException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testPrivateSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = JsonSyntaxException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }
}