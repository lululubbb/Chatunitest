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

public class JsonPrimitive_539_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, initialize in each test
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenValueIsNumber_shouldReturnShortValue() throws Exception {
    // Create JsonPrimitive with a Number value
    jsonPrimitive = new JsonPrimitive(12345);

    short result = jsonPrimitive.getAsShort();

    assertEquals((short) 12345, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenValueIsLazilyParsedNumber_shouldReturnShortValue() throws Exception {
    // LazilyParsedNumber is internal, test with a String number
    jsonPrimitive = new JsonPrimitive("12345");

    // Using reflection to set private field 'value' to LazilyParsedNumber
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, new com.google.gson.internal.LazilyParsedNumber("12345"));

    short result = jsonPrimitive.getAsShort();

    assertEquals((short) 12345, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenValueIsString_shouldParseShort() {
    jsonPrimitive = new JsonPrimitive("32767");

    short result = jsonPrimitive.getAsShort();

    assertEquals(Short.MAX_VALUE, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenValueIsStringNegative_shouldParseShort() {
    jsonPrimitive = new JsonPrimitive("-32768");

    short result = jsonPrimitive.getAsShort();

    assertEquals(Short.MIN_VALUE, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenValueIsNonNumberString_shouldThrowNumberFormatException() {
    jsonPrimitive = new JsonPrimitive("notANumber");

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsShort();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_coverageIsNumberFalseBranch() throws Exception {
    // Spy on JsonPrimitive to mock isNumber() and getAsString()
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("123"));

    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("123").when(spyPrimitive).getAsString();

    short result = spyPrimitive.getAsShort();

    assertEquals((short) 123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_coverageIsNumberTrueBranch() throws Exception {
    // Spy on JsonPrimitive to mock isNumber() and getAsNumber()
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive(456));

    doReturn(true).when(spyPrimitive).isNumber();
    doReturn((short) 456).when(spyPrimitive).getAsNumber();

    short result = spyPrimitive.getAsShort();

    assertEquals((short) 456, result);
  }
}