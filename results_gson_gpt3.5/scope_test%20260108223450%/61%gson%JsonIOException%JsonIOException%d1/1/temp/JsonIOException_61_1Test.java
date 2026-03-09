package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonIOException_61_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_Message() {
    String message = "error message";
    JsonIOException ex = new JsonIOException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(message, cause);
    assertEquals(message, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Cause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerializationUID() throws Exception {
    // Using reflection to check private static final long serialVersionUID = 1L;
    java.lang.reflect.Field field = JsonIOException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }
}