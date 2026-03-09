package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonIOException_61_5Test {

  @Test
    @Timeout(8000)
  public void testConstructor_Message() {
    String message = "Test message";
    JsonIOException exception = new JsonIOException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndCause() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    JsonIOException exception = new JsonIOException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Cause() {
    Throwable cause = new RuntimeException("Cause");
    JsonIOException exception = new JsonIOException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_NullMessage() {
    JsonIOException exception = new JsonIOException((String) null);
    assertEquals(null, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_NullCause() {
    JsonIOException exception = new JsonIOException((Throwable) null);
    assertEquals(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_NullMessageAndNullCause() {
    JsonIOException exception = new JsonIOException(null, null);
    assertEquals(null, exception.getMessage());
    assertEquals(null, exception.getCause());
  }
}