package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_538_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    // no-op, will initialize per test
  }

  @Test
    @Timeout(8000)
  void getAsLong_withNumber_returnsLongValue() throws Exception {
    Number number = mock(Number.class);
    when(number.longValue()).thenReturn(123L);

    // Use reflection to create a JsonPrimitive instance with the mocked Number
    jsonPrimitive = createJsonPrimitiveWithNumber(number);

    long result = jsonPrimitive.getAsLong();
    assertEquals(123L, result);
    verify(number).longValue();
  }

  @Test
    @Timeout(8000)
  void getAsLong_withNonNumber_parsesString() throws Exception {
    // Use reflection to create a JsonPrimitive instance with the string "456"
    jsonPrimitive = createJsonPrimitiveWithString("456");

    long result = jsonPrimitive.getAsLong();
    assertEquals(456L, result);
  }

  @Test
    @Timeout(8000)
  void getAsLong_withNonNumber_invalidString_throwsNumberFormatException() throws Exception {
    // Use reflection to create a JsonPrimitive instance with the string "abc"
    jsonPrimitive = createJsonPrimitiveWithString("abc");

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsLong();
    });
  }

  @Test
    @Timeout(8000)
  void privateIsIntegral_reflection() throws Exception {
    // Create a JsonPrimitive wrapping a Long number to test isIntegral method
    jsonPrimitive = new JsonPrimitive(42L);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean result = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertTrue(result);

    // Also test with a JsonPrimitive wrapping a non-integral Number (e.g. Double)
    JsonPrimitive doublePrimitive = new JsonPrimitive(3.14);
    boolean doubleResult = (boolean) isIntegralMethod.invoke(null, doublePrimitive);
    assertFalse(doubleResult);
  }

  // Helper methods to create JsonPrimitive instances without subclassing final class

  private JsonPrimitive createJsonPrimitiveWithNumber(Number number) throws Exception {
    // Create JsonPrimitive using constructor with Number parameter
    return new JsonPrimitive(number);
  }

  private JsonPrimitive createJsonPrimitiveWithString(String string) throws Exception {
    // Create JsonPrimitive using constructor with String parameter
    return new JsonPrimitive(string);
  }
}