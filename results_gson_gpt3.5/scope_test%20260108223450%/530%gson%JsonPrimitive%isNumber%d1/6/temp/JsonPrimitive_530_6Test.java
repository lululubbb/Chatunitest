package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonPrimitive_530_6Test {

  @Test
    @Timeout(8000)
  public void testIsNumber_withInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withDouble() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123.456);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is package-private, create via constructor with String
    JsonPrimitive primitive = new JsonPrimitive(new com.google.gson.internal.LazilyParsedNumber("789"));
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
    JsonPrimitive primitive = new JsonPrimitive("string");
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
  public void testIsNumber_withReflection_valueSetToNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("not a number");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, 42L);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_valueSetToNonNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(100);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, "not a number");
    assertFalse(primitive.isNumber());
  }
}