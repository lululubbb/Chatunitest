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

public class JsonPrimitive_537_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No common setup needed
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithNumberValue() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(3.14f);

    // Act
    float result = jsonPrimitive.getAsFloat();

    // Assert
    assertEquals(3.14f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithIntegerNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(10);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(10.0f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is package-private, so create via reflection
    Object lazilyParsedNumber = new LazilyParsedNumber("2.71");
    jsonPrimitive = new JsonPrimitive((Number) lazilyParsedNumber);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(2.71f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithStringValue_ParsableFloat() throws Exception {
    jsonPrimitive = new JsonPrimitive("1.618");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(1.618f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithStringValue_NonParsableFloat() throws Exception {
    jsonPrimitive = new JsonPrimitive("NaN");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(Float.NaN, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithStringValue_PositiveInfinity() throws Exception {
    jsonPrimitive = new JsonPrimitive("Infinity");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(Float.POSITIVE_INFINITY, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_WithStringValue_NegativeInfinity() throws Exception {
    jsonPrimitive = new JsonPrimitive("-Infinity");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(Float.NEGATIVE_INFINITY, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_PrivateValueField_Number() throws Exception {
    jsonPrimitive = new JsonPrimitive(5);

    // Use reflection to set private field 'value' to a Double
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, Double.valueOf(7.5));

    float result = jsonPrimitive.getAsFloat();

    assertEquals(7.5f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_PrivateValueField_String() throws Exception {
    jsonPrimitive = new JsonPrimitive("3.33");

    // Use reflection to set private field 'value' to a String
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "4.44");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(4.44f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_PrivateMethod_isNumber_True() throws Exception {
    jsonPrimitive = new JsonPrimitive(12);

    Method isNumberMethod = JsonPrimitive.class.getDeclaredMethod("isNumber");
    isNumberMethod.setAccessible(true);
    boolean isNumber = (boolean) isNumberMethod.invoke(jsonPrimitive);
    assertTrue(isNumber);

    float result = jsonPrimitive.getAsFloat();
    assertEquals(12f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_PrivateMethod_isNumber_False() throws Exception {
    jsonPrimitive = new JsonPrimitive("7.77");

    Method isNumberMethod = JsonPrimitive.class.getDeclaredMethod("isNumber");
    isNumberMethod.setAccessible(true);
    boolean isNumber = (boolean) isNumberMethod.invoke(jsonPrimitive);
    assertFalse(isNumber);

    float result = jsonPrimitive.getAsFloat();
    assertEquals(7.77f, result, 0.0001f);
  }
}