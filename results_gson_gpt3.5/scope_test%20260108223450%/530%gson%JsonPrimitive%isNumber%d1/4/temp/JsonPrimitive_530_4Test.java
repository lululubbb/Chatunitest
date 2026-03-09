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

public class JsonPrimitive_530_4Test {

  @Test
    @Timeout(8000)
  public void testIsNumber_withInteger() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertTrue(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withDouble() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123.45);
    assertTrue(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumber() throws Exception {
    LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber("123");
    JsonPrimitive jsonPrimitive = new JsonPrimitive(lazilyParsedNumber);
    assertTrue(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withBoolean() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertFalse(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertFalse(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withCharacter() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('c');
    assertFalse(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_setNumberValue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("initial");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, 42);
    assertTrue(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_setNonNumberValue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(1);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "not a number");
    assertFalse(jsonPrimitive.isNumber());
  }
}