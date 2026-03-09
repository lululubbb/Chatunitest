package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonIOException_61_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "Test message";
    JsonIOException ex = new JsonIOException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonIOException ex = new JsonIOException(message, cause);
    assertEquals(message, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withCause() {
    Throwable cause = new RuntimeException("Cause");
    JsonIOException ex = new JsonIOException(cause);
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUID() throws Exception {
    // Using reflection to check the private static final field serialVersionUID
    java.lang.reflect.Field field = JsonIOException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }
}