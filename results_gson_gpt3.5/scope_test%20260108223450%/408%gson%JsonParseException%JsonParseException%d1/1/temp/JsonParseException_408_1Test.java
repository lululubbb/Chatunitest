package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class JsonParseException_408_1Test {

  @Test
    @Timeout(8000)
  void testConstructor_withCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String msg = "error message";
    JsonParseException exception = new JsonParseException(msg);
    assertEquals(msg, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String msg = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(msg, cause);
    assertEquals(msg, exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}