package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JsonSyntaxException_459_4Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "Test message";
    JsonSyntaxException exception = new JsonSyntaxException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonSyntaxException exception = new JsonSyntaxException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("Cause");
    JsonSyntaxException exception = new JsonSyntaxException(cause);
    assertNotNull(exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}