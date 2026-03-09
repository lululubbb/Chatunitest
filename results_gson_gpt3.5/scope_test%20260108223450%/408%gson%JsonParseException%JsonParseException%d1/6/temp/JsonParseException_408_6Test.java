package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class JsonParseException_408_6Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithThrowable() {
    Throwable cause = new Throwable("cause message");
    JsonParseException exception = new JsonParseException(cause);
    assertNotNull(exception);
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithString() {
    String message = "error message";
    JsonParseException exception = new JsonParseException(message);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithStringAndThrowable() {
    String message = "error message";
    Throwable cause = new Throwable("cause message");
    JsonParseException exception = new JsonParseException(message, cause);
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}