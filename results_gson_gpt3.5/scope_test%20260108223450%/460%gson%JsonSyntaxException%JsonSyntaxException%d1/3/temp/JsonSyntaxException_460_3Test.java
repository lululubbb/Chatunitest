package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JsonSyntaxException_460_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_StringAndThrowable() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException exception = new JsonSyntaxException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_String() {
    String message = "error message";
    JsonSyntaxException exception = new JsonSyntaxException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Throwable() {
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException exception = new JsonSyntaxException(cause);
    assertEquals(cause.toString(), exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}