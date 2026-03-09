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

public class JsonPrimitive_529_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setup() {
    // no-op
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanTrue_returnsBooleanValue() throws Exception {
    jsonPrimitive = new JsonPrimitive(true);

    // Using reflection to set private final field 'value' to Boolean.TRUE
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.TRUE);

    // Spy to mock isBoolean() to return true
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isBoolean();

    boolean result = spyPrimitive.getAsBoolean();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalseAndStringTrue_returnsTrue() throws Exception {
    jsonPrimitive = new JsonPrimitive("true");

    // Spy to mock isBoolean() to return false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // Spy getAsString() to return "true"
    doReturn("true").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalseAndStringFalse_returnsFalse() throws Exception {
    jsonPrimitive = new JsonPrimitive("false");

    // Spy to mock isBoolean() to return false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // Spy getAsString() to return "false"
    doReturn("false").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalseAndStringRandom_returnsFalse() throws Exception {
    jsonPrimitive = new JsonPrimitive("randomString");

    // Spy to mock isBoolean() to return false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // Spy getAsString() to return "randomString"
    doReturn("randomString").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();
    assertFalse(result);
  }

}