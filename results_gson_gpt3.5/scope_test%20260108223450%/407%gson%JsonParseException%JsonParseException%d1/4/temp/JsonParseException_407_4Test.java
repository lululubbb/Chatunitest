package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonParseException_407_4Test {

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndCause() {
    Throwable cause = new IllegalArgumentException("cause message");
    JsonParseException exception = new JsonParseException("error message", cause);
    assertEquals("error message", exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_MessageOnly() {
    JsonParseException exception = new JsonParseException("error message");
    assertEquals("error message", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_CauseOnly() {
    Throwable cause = new NullPointerException("null pointer");
    JsonParseException exception = new JsonParseException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}