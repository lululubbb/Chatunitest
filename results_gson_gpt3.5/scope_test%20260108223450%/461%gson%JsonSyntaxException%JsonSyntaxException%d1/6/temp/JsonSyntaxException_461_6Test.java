package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonSyntaxException_461_6Test {

  @Test
    @Timeout(8000)
  void testConstructor_withThrowableCause() {
    Throwable cause = new RuntimeException("cause message");
    JsonSyntaxException exception = new JsonSyntaxException(cause);
    assertEquals(cause, exception.getCause());
    assertEquals(cause.toString(), exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String message = "syntax error";
    JsonSyntaxException exception = new JsonSyntaxException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String message = "syntax error";
    Throwable cause = new RuntimeException("cause message");
    JsonSyntaxException exception = new JsonSyntaxException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}