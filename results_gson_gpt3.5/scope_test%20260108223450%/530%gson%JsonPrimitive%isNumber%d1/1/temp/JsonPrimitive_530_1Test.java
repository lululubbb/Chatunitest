package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_530_1Test {

  private JsonPrimitive jsonPrimitiveNumber;
  private JsonPrimitive jsonPrimitiveString;
  private JsonPrimitive jsonPrimitiveBoolean;
  private JsonPrimitive jsonPrimitiveCharacter;
  private JsonPrimitive jsonPrimitiveLazilyParsedNumber;

  @BeforeEach
  public void setUp() throws Exception {
    // Using reflection to set private final field 'value' via constructors is complicated,
    // but JsonPrimitive has public constructors for Number, String, Boolean, Character.
    jsonPrimitiveNumber = new JsonPrimitive(123);
    jsonPrimitiveString = new JsonPrimitive("test");
    jsonPrimitiveBoolean = new JsonPrimitive(true);
    jsonPrimitiveCharacter = new JsonPrimitive('c');
    jsonPrimitiveLazilyParsedNumber = new JsonPrimitive(new com.google.gson.internal.LazilyParsedNumber("456"));
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withInteger() {
    assertTrue(jsonPrimitiveNumber.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withString() {
    assertFalse(jsonPrimitiveString.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withBoolean() {
    assertFalse(jsonPrimitiveBoolean.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withCharacter() {
    assertFalse(jsonPrimitiveCharacter.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withLazilyParsedNumber() {
    assertTrue(jsonPrimitiveLazilyParsedNumber.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_setValueToNumberSubclass() throws Exception {
    JsonPrimitive jp = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jp, 3.14f); // Float is a Number subclass
    assertTrue(jp.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_withReflection_setValueToNonNumber() throws Exception {
    JsonPrimitive jp = new JsonPrimitive(0);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jp, new Object());
    assertFalse(jp.isNumber());
  }
}