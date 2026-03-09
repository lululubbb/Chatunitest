package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonParseException_406_3Test {

  @Test
    @Timeout(8000)
  void testConstructor_Message() {
    String message = "Test message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_MessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_Cause() {
    Throwable cause = new RuntimeException("Cause");
    JsonParseException exception = new JsonParseException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}