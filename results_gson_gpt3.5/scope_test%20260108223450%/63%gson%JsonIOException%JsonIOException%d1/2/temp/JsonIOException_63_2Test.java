package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonIOException_63_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowableCause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(cause);
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    JsonIOException ex = new JsonIOException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}