package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;

class JsonSyntaxException_461_4Test {

  @Test
    @Timeout(8000)
  void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("root cause");
    JsonSyntaxException exception = new JsonSyntaxException(cause);
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String message = "syntax error";
    JsonSyntaxException exception = new JsonSyntaxException(message);
    assertEquals(message, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String message = "syntax error";
    Throwable cause = new RuntimeException("root cause");
    JsonSyntaxException exception = new JsonSyntaxException(message, cause);
    assertEquals(message, exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
    @Timeout(8000)
  void testInheritance() {
    JsonSyntaxException exception = new JsonSyntaxException("msg");
    assertThrows(JsonParseException.class, () -> {
      throw exception;
    });
  }
}