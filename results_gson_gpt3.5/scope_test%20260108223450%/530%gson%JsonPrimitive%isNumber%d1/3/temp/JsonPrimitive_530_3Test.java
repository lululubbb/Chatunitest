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

public class JsonPrimitive_530_3Test {

  @Test
    @Timeout(8000)
  public void testIsNumber_withInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withDouble() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123.45);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumber() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123");
    JsonPrimitive primitive = new JsonPrimitive(lpn);
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
    valueField.set(primitive, 456);
    assertTrue(primitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_valueSetToNonNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, "string");
    assertFalse(primitive.isNumber());
  }
}