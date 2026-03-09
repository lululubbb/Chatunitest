package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonSyntaxException;

class JsonSyntaxException_459_6Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    String message = "Test message";
    JsonSyntaxException ex = new JsonSyntaxException(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonSyntaxException ex = new JsonSyntaxException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("Cause");
    JsonSyntaxException ex = new JsonSyntaxException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}