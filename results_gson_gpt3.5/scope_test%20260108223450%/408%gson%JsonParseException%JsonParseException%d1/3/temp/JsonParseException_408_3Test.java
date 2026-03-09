package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonParseException_408_3Test {

  @Test
    @Timeout(8000)
  void testConstructor_withCause() {
    Throwable cause = new RuntimeException("root cause");
    JsonParseException ex = new JsonParseException(cause);
    assertNotNull(ex);
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String message = "error message";
    JsonParseException ex = new JsonParseException(message);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("root cause");
    JsonParseException ex = new JsonParseException(message, cause);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}