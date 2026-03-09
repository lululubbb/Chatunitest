package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JsonSyntaxException_461_1Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithThrowable() {
    Throwable cause = new Throwable("cause");
    JsonSyntaxException exception = new JsonSyntaxException(cause);
    assertEquals(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "error message";
    JsonSyntaxException exception = new JsonSyntaxException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new Throwable("cause");
    JsonSyntaxException exception = new JsonSyntaxException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}