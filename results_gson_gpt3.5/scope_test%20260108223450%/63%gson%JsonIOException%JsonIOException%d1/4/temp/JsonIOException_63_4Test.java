package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonIOException_63_4Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new Throwable("cause message");
    JsonIOException exception = new JsonIOException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    JsonIOException exception = new JsonIOException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "error message";
    Throwable cause = new Throwable("cause message");
    JsonIOException exception = new JsonIOException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullCause() {
    JsonIOException exception = new JsonIOException((Throwable) null);
    assertEquals(null, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullMessage() {
    JsonIOException exception = new JsonIOException((String) null);
    assertEquals(null, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullMessageAndCause() {
    JsonIOException exception = new JsonIOException(null, null);
    assertEquals(null, exception.getMessage());
    assertEquals(null, exception.getCause());
  }
}