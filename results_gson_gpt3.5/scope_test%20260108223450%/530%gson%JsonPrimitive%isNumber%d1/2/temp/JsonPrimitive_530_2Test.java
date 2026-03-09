package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonPrimitive_530_2Test {

  @Test
    @Timeout(8000)
  public void testIsNumber_withNumberValue_shouldReturnTrue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumberValue_shouldReturnTrue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(new com.google.gson.internal.LazilyParsedNumber("456"));
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withBooleanValue_shouldReturnFalse() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withStringValue_shouldReturnFalse() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("string");
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withCharacterValue_shouldReturnFalse() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive('c');
    assertFalse(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withManuallySetValueToNumber_shouldReturnTrue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("dummy");
    // Use reflection to set private final field 'value' to a Number instance
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, 789);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withManuallySetValueToNonNumber_shouldReturnFalse() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    // Use reflection to set private final field 'value' to a non-Number instance
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, "not a number");
    assertFalse(primitive.isNumber());
  }
}