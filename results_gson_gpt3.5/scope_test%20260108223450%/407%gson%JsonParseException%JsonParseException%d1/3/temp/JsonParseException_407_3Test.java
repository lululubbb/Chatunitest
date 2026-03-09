package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsonParseException_407_3Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException("error message", cause);
    assertEquals("error message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    JsonParseException exception = new JsonParseException("error message");
    assertEquals("error message", exception.getMessage());
    // Cause should be null
    assertEquals(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(cause);
    // Message should be cause.toString()
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}