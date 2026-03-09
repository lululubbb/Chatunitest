package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonPrimitive_528_4Test {

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanTrue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);
    assertTrue(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanFalse() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(false);
    assertTrue(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNonBooleanInteger() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    assertFalse(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNonBooleanString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("true");
    assertFalse(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNonBooleanCharacter() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive('a');
    assertFalse(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withPrivateValueBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("not boolean");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, Boolean.TRUE);
    assertTrue(primitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withPrivateValueNonBoolean() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, 1);
    assertFalse(primitive.isBoolean());
  }
}