package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class JsonParseException_407_5Test {

  @Test
    @Timeout(8000)
  void testConstructor_MessageCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException("error message", cause);
    assertEquals("error message", ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_MessageOnly() {
    JsonParseException ex = new JsonParseException("error message");
    assertEquals("error message", ex.getMessage());
    // cause should be null
    assertSame(null, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_CauseOnly() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException ex = new JsonParseException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertSame(cause, ex.getCause());
  }
}