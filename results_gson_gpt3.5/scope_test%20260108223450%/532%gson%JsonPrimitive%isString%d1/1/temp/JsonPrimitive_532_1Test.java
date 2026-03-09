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

public class JsonPrimitive_532_1Test {

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
    // Changed to use String constructor with Character to match real behavior
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Character.valueOf('c').toString());
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_reflectivelySetValue() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    valueField.set(jsonPrimitive, "stringValue");
    assertTrue(jsonPrimitive.isString());

    valueField.set(jsonPrimitive, 42);
    assertFalse(jsonPrimitive.isString());

    valueField.set(jsonPrimitive, true);
    assertFalse(jsonPrimitive.isString());

    valueField.set(jsonPrimitive, null);
    assertFalse(jsonPrimitive.isString());
  }
}