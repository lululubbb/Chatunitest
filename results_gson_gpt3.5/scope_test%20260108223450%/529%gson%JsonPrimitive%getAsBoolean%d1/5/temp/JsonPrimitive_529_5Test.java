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

public class JsonPrimitive_529_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, instances created per test
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_WhenIsBooleanTrue() throws Exception {
    // Create instance with Boolean true
    jsonPrimitive = new JsonPrimitive(true);

    // Use reflection to set private final field 'value' to Boolean.TRUE to simulate isBoolean() true
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
  public void testGetAsBoolean_WhenIsBooleanFalse_AndStringTrue() throws Exception {
    jsonPrimitive = new JsonPrimitive("true");

    // Spy to mock isBoolean() to return false and getAsString() to return "true"
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();
    doReturn("true").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_WhenIsBooleanFalse_AndStringFalse() throws Exception {
    jsonPrimitive = new JsonPrimitive("false");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();
    doReturn("false").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_WhenIsBooleanFalse_AndStringRandom() throws Exception {
    jsonPrimitive = new JsonPrimitive("random");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();
    doReturn("random").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_WhenIsBooleanFalse_AndStringNull() throws Exception {
    jsonPrimitive = new JsonPrimitive("null");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();
    doReturn("null").when(spyPrimitive).getAsString();

    boolean result = spyPrimitive.getAsBoolean();

    assertFalse(result);
  }
}