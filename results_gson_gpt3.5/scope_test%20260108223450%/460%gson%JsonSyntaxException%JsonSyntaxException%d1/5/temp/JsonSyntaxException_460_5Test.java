package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonSyntaxException;

class JsonSyntaxException_460_5Test {

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException ex = new JsonSyntaxException("error message", cause);
    assertEquals("error message", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() {
    JsonSyntaxException ex = new JsonSyntaxException("error message");
    assertEquals("error message", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCause() {
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException ex = new JsonSyntaxException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}