package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsonParseException_408_2Test {

  @Test
    @Timeout(8000)
  void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String message = "error message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}