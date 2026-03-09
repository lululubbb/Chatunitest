package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonSyntaxException_459_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "Syntax error";
    JsonSyntaxException ex = new JsonSyntaxException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndCause() {
    String message = "Syntax error";
    Throwable cause = new RuntimeException("Cause exception");
    JsonSyntaxException ex = new JsonSyntaxException(message, cause);
    assertEquals(message, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withCause() {
    Throwable cause = new RuntimeException("Cause exception");
    JsonSyntaxException ex = new JsonSyntaxException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerializationUID() throws Exception {
    // Access private static final field serialVersionUID via reflection
    java.lang.reflect.Field field = JsonSyntaxException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }
}