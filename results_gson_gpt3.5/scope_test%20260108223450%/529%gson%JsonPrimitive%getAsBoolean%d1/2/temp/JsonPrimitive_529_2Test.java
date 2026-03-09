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

public class JsonPrimitive_529_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No default constructor, will create instances via reflection or constructors if available
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanTrue_valueTrue() throws Exception {
    // Create JsonPrimitive with Boolean true
    jsonPrimitive = new JsonPrimitive(true);

    // getAsBoolean should return true
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanTrue_valueFalse() throws Exception {
    // Create JsonPrimitive with Boolean false
    jsonPrimitive = new JsonPrimitive(false);

    // getAsBoolean should return false
    assertFalse(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalse_parseTrueString() throws Exception {
    // Create JsonPrimitive with String "true"
    jsonPrimitive = new JsonPrimitive("true");

    // Spy on jsonPrimitive to mock isBoolean() to false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // getAsBoolean should parse string and return true
    assertTrue(spyPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalse_parseFalseString() throws Exception {
    // Create JsonPrimitive with String "false"
    jsonPrimitive = new JsonPrimitive("false");

    // Spy on jsonPrimitive to mock isBoolean() to false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // getAsBoolean should parse string and return false
    assertFalse(spyPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_whenIsBooleanFalse_parseRandomString() throws Exception {
    // Create JsonPrimitive with String "randomString"
    jsonPrimitive = new JsonPrimitive("randomString");

    // Spy on jsonPrimitive to mock isBoolean() to false
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isBoolean();

    // getAsBoolean should parse string and return false for non-true string
    assertFalse(spyPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_privateFieldTrue() throws Exception {
    // Create JsonPrimitive with Boolean true
    jsonPrimitive = new JsonPrimitive(true);

    // isBoolean() should return true
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_privateFieldNotBoolean() throws Exception {
    // Create JsonPrimitive with String
    jsonPrimitive = new JsonPrimitive("string");

    // isBoolean() should return false
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testPrivateFieldValueSetCorrectly() throws Exception {
    // Create JsonPrimitive with Integer 123
    jsonPrimitive = new JsonPrimitive(123);

    // Access private field value via reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals(123, value);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethodIsIntegral() throws Exception {
    // Prepare JsonPrimitive with integral Number
    JsonPrimitive integralPrimitive = new JsonPrimitive(123);

    // Prepare JsonPrimitive with non-integral Number (Double)
    JsonPrimitive nonIntegralPrimitive = new JsonPrimitive(123.456);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean nonIntegralResult = (boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
  }
}