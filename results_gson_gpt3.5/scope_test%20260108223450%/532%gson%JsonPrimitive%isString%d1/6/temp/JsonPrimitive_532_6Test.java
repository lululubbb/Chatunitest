package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class JsonPrimitive_532_6Test {

  @Test
    @Timeout(8000)
  public void testIsString_withStringValue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Integer() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Boolean() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Character() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('c');
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_LazilyParsedNumber() throws Exception {
    // Using reflection to create JsonPrimitive with LazilyParsedNumber since no direct constructor
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, new com.google.gson.internal.LazilyParsedNumber("123"));
    assertFalse(jsonPrimitive.isString());
  }
}