package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsonParseException_406_1Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "error message";
    JsonParseException ex = new JsonParseException(message);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException(message, cause);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException(cause);
    assertNotNull(ex);
    assertEquals(cause, ex.getCause());
  }
}