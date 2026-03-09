package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MalformedJsonException_185_4Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertNotNull(ex);
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}