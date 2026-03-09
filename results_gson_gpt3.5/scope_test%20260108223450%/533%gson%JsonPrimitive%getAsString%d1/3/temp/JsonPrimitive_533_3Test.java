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

public class JsonPrimitive_533_3Test {

  private JsonPrimitive jsonPrimitive;

  private void setValue(Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, value);
  }

  @BeforeEach
  public void setUp() throws Exception {
    // Instantiate with a dummy value so constructor sets final field.
    jsonPrimitive = new JsonPrimitive("dummy");
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withStringValue() throws Exception {
    setValue("testString");
    assertEquals("testString", jsonPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withNumberValue() throws Exception {
    Number number = 12345;
    setValue(number);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(number).when(spyPrimitive).getAsNumber();

    assertEquals("12345", spyPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_withBooleanValue() throws Exception {
    Boolean boolValue = Boolean.TRUE;
    setValue(boolValue);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(true).when(spyPrimitive).isBoolean();

    assertEquals("true", spyPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_unexpectedValueType_throwsAssertionError() throws Exception {
    Object unknownValue = new Object();
    setValue(unknownValue);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(false).when(spyPrimitive).isBoolean();

    AssertionError error = assertThrows(AssertionError.class, spyPrimitive::getAsString);
    assertTrue(error.getMessage().contains("Unexpected value type:"));
    assertTrue(error.getMessage().contains(unknownValue.getClass().toString()));
  }
}