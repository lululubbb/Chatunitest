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

public class JsonPrimitive_533_5Test {

  private JsonPrimitive jsonPrimitive;

  private void setValue(Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, value);
  }

  @BeforeEach
  public void setUp() throws Exception {
    // We create a JsonPrimitive instance with any constructor (e.g. Boolean)
    jsonPrimitive = new JsonPrimitive(true);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withStringValue() throws Exception {
    setValue("testString");
    String result = jsonPrimitive.getAsString();
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withNumberValue() throws Exception {
    // Use Integer number
    setValue(123);
    // Spy to override isNumber and getAsNumber to behave as expected
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(123).when(spyPrimitive).getAsNumber();
    // Replace jsonPrimitive with spyPrimitive for this test
    String result = spyPrimitive.getAsString();
    assertEquals("123", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withBooleanValue() throws Exception {
    setValue(Boolean.TRUE);
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(true).when(spyPrimitive).isBoolean();
    String result = spyPrimitive.getAsString();
    assertEquals("true", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_unexpectedType_throwsAssertionError() throws Exception {
    setValue(new Object());
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(false).when(spyPrimitive).isBoolean();
    AssertionError error = assertThrows(AssertionError.class, spyPrimitive::getAsString);
    assertTrue(error.getMessage().contains("Unexpected value type:"));
  }
}