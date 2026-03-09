package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsonParseException_406_5Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "error message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}