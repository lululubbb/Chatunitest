package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonPrimitive_532_3Test {

  @Test
    @Timeout(8000)
  void testIsString_withStringValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  void testIsString_withNonStringValue_Integer() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  void testIsString_withNonStringValue_Boolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  void testIsString_withNonStringValue_Character() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('c');
    // Use reflection to access the private field 'value'
    var field = JsonPrimitive.class.getDeclaredField("value");
    field.setAccessible(true);
    Object value = field.get(jsonPrimitive);
    // Assert that the internal value is a String, so isString() returns true
    assertTrue(value instanceof String);
    assertTrue(jsonPrimitive.isString());
  }
}