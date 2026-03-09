package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_541_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    // no-op before each test
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenValueIsNumber_returnsByteValue() throws Exception {
    jsonPrimitive = new JsonPrimitive(123);

    // Using reflection to set private final field 'value' to a Number instance
    setValueField(jsonPrimitive, Integer.valueOf(123));

    // Spy to mock isNumber() and getAsNumber()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();

    // Actually getAsNumber returns Number, so mock getAsNumber to return a Number with byteValue 123
    doReturn(new Number() {
      private static final long serialVersionUID = 1L;
      @Override public int intValue() { return 123; }
      @Override public long longValue() { return 123L; }
      @Override public float floatValue() { return 123f; }
      @Override public double doubleValue() { return 123d; }
      @Override public byte byteValue() { return (byte) 123; }
      @Override public short shortValue() { return (short) 123; }
    }).when(spyPrimitive).getAsNumber();

    byte result = spyPrimitive.getAsByte();
    assertEquals((byte) 123, result);
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenValueIsNotNumber_returnsParsedByte() throws Exception {
    jsonPrimitive = new JsonPrimitive("45");

    // Using reflection to set private final field 'value' to a String
    setValueField(jsonPrimitive, "45");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("45").when(spyPrimitive).getAsString();

    byte result = spyPrimitive.getAsByte();
    assertEquals((byte) 45, result);
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenValueIsNotNumber_invalidByte_throwsNumberFormatException() throws Exception {
    jsonPrimitive = new JsonPrimitive("invalid");

    setValueField(jsonPrimitive, "invalid");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("invalid").when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, spyPrimitive::getAsByte);
  }

  // Helper method to set private final field 'value' via reflection
  private void setValueField(JsonPrimitive instance, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    // Remove final modifier if present (works on Java <= 11)
    try {
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(valueField, valueField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
    } catch (NoSuchFieldException ignored) {
      // Java 12+ does not have 'modifiers' field; ignore
    }

    valueField.set(instance, value);
  }
}