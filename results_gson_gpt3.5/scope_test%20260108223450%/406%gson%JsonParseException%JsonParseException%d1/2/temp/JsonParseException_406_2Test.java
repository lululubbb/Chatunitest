package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class JsonParseException_406_2Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "Error message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
    assertEquals(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    String message = "Error message";
    Throwable cause = new RuntimeException("Cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("Cause");
    JsonParseException exception = new JsonParseException(cause);
    assertNotNull(exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}