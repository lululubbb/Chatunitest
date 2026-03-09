package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class JsonParseException_406_6Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "error message";
    JsonParseException exception = new JsonParseException(message);
    assertEquals(message, exception.getMessage());
    assertEquals(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndCause() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonParseException exception = new JsonParseException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertSame(cause, exception.getCause());
  }
}