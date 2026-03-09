package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class JsonIOException_61_6Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "Test message";
    JsonIOException exception = new JsonIOException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonIOException exception = new JsonIOException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithCause() {
    Throwable cause = new RuntimeException("Cause");
    JsonIOException exception = new JsonIOException(cause);
    assertSame(cause, exception.getCause());
  }
}