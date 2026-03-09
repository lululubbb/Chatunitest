package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonPrimitive_533_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsString_withStringValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");
    String result = jsonPrimitive.getAsString();
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withNumberValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    String result = jsonPrimitive.getAsString();
    assertEquals("123", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    String result = jsonPrimitive.getAsString();
    assertEquals("true", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_unexpectedValue_throwsAssertionError() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    // Use reflection to set private field 'value' to an unexpected type (e.g. Object)
    var valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, new Object());

    // Stub isNumber() and isBoolean() to return false
    Mockito.when(jsonPrimitive.isNumber()).thenReturn(false);
    Mockito.when(jsonPrimitive.isBoolean()).thenReturn(false);

    AssertionError thrown = assertThrows(AssertionError.class, jsonPrimitive::getAsString);
    assertTrue(thrown.getMessage().contains("Unexpected value type"));
    assertTrue(thrown.getMessage().contains("java.lang.Object"));
  }
}