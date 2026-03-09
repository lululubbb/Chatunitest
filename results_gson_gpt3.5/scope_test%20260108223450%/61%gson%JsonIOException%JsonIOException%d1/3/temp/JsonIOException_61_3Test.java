package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonIOException_61_3Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "test message";
    JsonIOException ex = new JsonIOException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndCause() {
    String message = "test message";
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(message, cause);
    assertEquals(message, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(cause);
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerializationId() throws Exception {
    // Use reflection to access private static final field serialVersionUID
    var field = JsonIOException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }
}