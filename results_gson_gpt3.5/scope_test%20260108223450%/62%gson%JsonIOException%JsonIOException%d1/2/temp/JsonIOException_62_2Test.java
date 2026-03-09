package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonIOException_62_2Test {

  @Test
    @Timeout(8000)
  void testConstructor_MessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException exception = new JsonIOException("message", cause);
    assertEquals("message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_MessageOnly() {
    JsonIOException exception = new JsonIOException("message");
    assertEquals("message", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_CauseOnly() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException exception = new JsonIOException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}