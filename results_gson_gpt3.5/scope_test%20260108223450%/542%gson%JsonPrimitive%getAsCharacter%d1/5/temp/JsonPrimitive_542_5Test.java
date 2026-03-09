package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonPrimitive_542_5Test {

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_nonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("hello");

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);
    char result = (char) method.invoke(jsonPrimitive);

    assertEquals('h', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyString_throwsException() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("");

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    Throwable thrown = assertThrows(
      Throwable.class,
      () -> method.invoke(jsonPrimitive),
      "Expected getAsCharacter() to throw, but it didn't"
    );

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
    assertEquals("String value is empty", cause.getMessage());
  }
}