package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonIOException_63_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException exception = new JsonIOException(cause);
    assertNotNull(exception);
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    JsonIOException exception = new JsonIOException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonIOException exception = new JsonIOException(message, cause);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}