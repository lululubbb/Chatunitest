package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_541_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, instances created in each test
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNumber_returnsByteValue() throws Exception {
    // Create a Number mock that returns a specific byte value
    Number number = mock(Number.class);
    when(number.byteValue()).thenReturn((byte) 42);

    // Create JsonPrimitive with Number
    jsonPrimitive = new JsonPrimitive(number);

    // Use reflection to set private final field 'value' to the mock number
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, number);

    // Spy on jsonPrimitive to mock isNumber() and getAsNumber()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(number).when(spyPrimitive).getAsNumber();

    byte result = spyPrimitive.getAsByte();

    assertEquals((byte) 42, result);

    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_parsesString() throws Exception {
    // Create JsonPrimitive with String
    jsonPrimitive = new JsonPrimitive("123");

    // Spy on jsonPrimitive to mock isNumber() and getAsString()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("123").when(spyPrimitive).getAsString();

    byte result = spyPrimitive.getAsByte();

    assertEquals((byte) 123, result);

    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_invalidString_throwsNumberFormatException() throws Exception {
    jsonPrimitive = new JsonPrimitive("abc");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("abc").when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, spyPrimitive::getAsByte);

    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }
}