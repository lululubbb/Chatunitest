package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_528_3Test {

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanTrue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanFalse() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.FALSE);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNumber() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withCharacter() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('a');
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withLazilyParsedNumber() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(new LazilyParsedNumber("123"));
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNullValue() throws Exception {
    // Create JsonPrimitive normally
    JsonPrimitive jsonPrimitive = new JsonPrimitive("dummy");
    // Use reflection to set private final field 'value' to null
    java.lang.reflect.Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    // Remove final modifier from the field
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(valueField, valueField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    valueField.set(jsonPrimitive, null);
    assertFalse(jsonPrimitive.isBoolean());
  }
}