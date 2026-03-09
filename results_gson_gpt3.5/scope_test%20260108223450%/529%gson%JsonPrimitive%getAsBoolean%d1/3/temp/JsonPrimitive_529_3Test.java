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

import org.junit.jupiter.api.Test;

public class JsonPrimitive_529_3Test {

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
    JsonPrimitive jsonPrimitive = new JsonPrimitive("FaLse");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenValueIsStringNonBoolean() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("notABoolean");
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_reflectively_setValueBooleanTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("false");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.TRUE);
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_reflectively_setValueBooleanFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Boolean.FALSE);
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_privateMethod() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    Method isBooleanMethod = JsonPrimitive.class.getDeclaredMethod("isBoolean");
    isBooleanMethod.setAccessible(true);
    boolean result = (boolean) isBooleanMethod.invoke(jsonPrimitive);
    assertTrue(result);

    JsonPrimitive jsonPrimitive2 = new JsonPrimitive("string");
    boolean result2 = (boolean) isBooleanMethod.invoke(jsonPrimitive2);
    assertFalse(result2);
  }
}