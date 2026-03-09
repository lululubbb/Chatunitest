package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonPrimitive_529_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsBooleanTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsBooleanFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(false);
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsStringTrueIgnoreCase() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("TrUe");
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsStringFalseIgnoreCase() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("fAlSe");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsStringRandom() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("randomString");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsNumberStringTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsNumberStringFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("false");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsNumber() throws Exception {
    // Create a JsonPrimitive with a number string "1" which should parse to false boolean
    JsonPrimitive jsonPrimitive = new JsonPrimitive("1");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_reflectIsBooleanTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");

    // Use reflection to set private field 'value' to a Boolean true
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.TRUE);

    // Use reflection to mock isBoolean() to return true
    JsonPrimitive spy = Mockito.spy(jsonPrimitive);
    doReturn(true).when(spy).isBoolean();

    assertTrue(spy.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_reflectIsBooleanFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("false");

    // Use reflection to set private field 'value' to a String "false"
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "false");

    // Use reflection to mock isBoolean() to return false
    JsonPrimitive spy = Mockito.spy(jsonPrimitive);
    doReturn(false).when(spy).isBoolean();

    assertFalse(spy.getAsBoolean());
  }
}