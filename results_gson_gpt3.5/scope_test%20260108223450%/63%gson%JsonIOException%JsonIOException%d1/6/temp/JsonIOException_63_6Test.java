package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonIOException_63_6Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withCause() {
    Throwable cause = new RuntimeException("root cause");
    JsonIOException exception = new JsonIOException(cause);
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    JsonIOException exception = new JsonIOException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("root cause");
    JsonIOException exception = new JsonIOException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}