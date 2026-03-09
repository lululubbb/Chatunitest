package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JsonParseException_407_6Test {

  @Test
    @Timeout(8000)
  void testConstructor_MessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException("message", cause);
    assertEquals("message", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_MessageOnly() {
    JsonParseException ex = new JsonParseException("message");
    assertEquals("message", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_CauseOnly() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}