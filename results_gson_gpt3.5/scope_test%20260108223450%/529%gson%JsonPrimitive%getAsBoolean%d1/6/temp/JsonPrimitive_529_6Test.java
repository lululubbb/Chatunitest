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

public class JsonPrimitive_529_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setup() {
    // no-op, instances created in each test
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_whenValueIsBooleanTrue() throws Exception {
    jsonPrimitive = new JsonPrimitive(true);

    // Using reflection to set private final field 'value' to Boolean.TRUE
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.TRUE);

    // Spy to override isBoolean to true
    JsonPrimitive spy = spy(jsonPrimitive);
    doReturn(true).when(spy).isBoolean();

    boolean result = spy.getAsBoolean();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_whenValueIsBooleanFalse() throws Exception {
    jsonPrimitive = new JsonPrimitive(false);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.FALSE);

    JsonPrimitive spy = spy(jsonPrimitive);
    doReturn(true).when(spy).isBoolean();

    boolean result = spy.getAsBoolean();
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_whenValueIsStringTrueIgnoreCase() throws Exception {
    jsonPrimitive = new JsonPrimitive("TrUe");

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "TrUe");

    JsonPrimitive spy = spy(jsonPrimitive);
    doReturn(false).when(spy).isBoolean();
    doReturn("TrUe").when(spy).getAsString();

    boolean result = spy.getAsBoolean();
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_whenValueIsStringFalseIgnoreCase() throws Exception {
    jsonPrimitive = new JsonPrimitive("FaLsE");

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "FaLsE");

    JsonPrimitive spy = spy(jsonPrimitive);
    doReturn(false).when(spy).isBoolean();
    doReturn("FaLsE").when(spy).getAsString();

    boolean result = spy.getAsBoolean();
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_whenValueIsStringNonBoolean() throws Exception {
    jsonPrimitive = new JsonPrimitive("notabool");

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "notabool");

    JsonPrimitive spy = spy(jsonPrimitive);
    doReturn(false).when(spy).isBoolean();
    doReturn("notabool").when(spy).getAsString();

    boolean result = spy.getAsBoolean();
    assertFalse(result);
  }
}