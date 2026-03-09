package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonPrimitive_530_5Test {

  @Test
    @Timeout(8000)
  public void testIsNumber_withInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(42);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withDouble() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(3.14);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumber() throws Exception {
    LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber("12345");
    JsonPrimitive primitive = new JsonPrimitive(lazilyParsedNumber);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("not a number");
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withCharacter() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive('c');
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withCustomObject() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("dummy");
    // Use reflection to set private field 'value' to a custom Object that is not a Number
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, new Object());
    assertFalse(primitive.isNumber());
  }
}