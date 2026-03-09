package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;

class JsonSyntaxException_461_2Test {

  @Test
    @Timeout(8000)
  void testConstructor_withCause() {
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException ex = new JsonSyntaxException(cause);
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() {
    String msg = "message";
    JsonSyntaxException ex = new JsonSyntaxException(msg);
    assertEquals(msg, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndCause() {
    String msg = "message";
    Throwable cause = new RuntimeException("cause");
    JsonSyntaxException ex = new JsonSyntaxException(msg, cause);
    assertEquals(msg, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_nullCause() {
    // The constructor does not throw NullPointerException, so just test that cause is null
    JsonSyntaxException ex = new JsonSyntaxException((Throwable) null);
    assertEquals(null, ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_nullMessage() {
    JsonSyntaxException ex = new JsonSyntaxException((String) null);
    assertEquals(null, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_nullMessageAndCause() {
    JsonSyntaxException ex = new JsonSyntaxException(null, null);
    assertEquals(null, ex.getMessage());
    assertEquals(null, ex.getCause());
  }
}