package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class JsonParseException_408_4Test {

  @Test
    @Timeout(8000)
  void testConstructorWithThrowable() {
    Throwable cause = new Throwable("cause");
    JsonParseException exception = new JsonParseException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "error message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new Throwable("cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}