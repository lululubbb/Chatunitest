package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonIOException_62_1Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException exception = new JsonIOException("message", cause);
    assertEquals("message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    JsonIOException exception = new JsonIOException("messageOnly");
    assertEquals("messageOnly", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("causeOnly");
    JsonIOException exception = new JsonIOException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}