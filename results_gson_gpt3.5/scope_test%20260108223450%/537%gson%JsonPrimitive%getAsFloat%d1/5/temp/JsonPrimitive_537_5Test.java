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

public class JsonPrimitive_537_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    // No default constructor, will create instances via reflection or constructors as needed
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_NumberValue() throws Exception {
    // Create JsonPrimitive with a Number value
    jsonPrimitive = new JsonPrimitive(3.14);

    // getAsFloat should return the float value of the number
    float result = jsonPrimitive.getAsFloat();
    assertEquals(3.14f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_StringValue() throws Exception {
    // Create JsonPrimitive with a String value that is a valid float string
    jsonPrimitive = new JsonPrimitive("2.718");

    // getAsFloat should parse the string as float
    float result = jsonPrimitive.getAsFloat();
    assertEquals(2.718f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_StringValue_IntegerString() throws Exception {
    // Create JsonPrimitive with a String value that is an integer string
    jsonPrimitive = new JsonPrimitive("123");

    // getAsFloat should parse the string as float
    float result = jsonPrimitive.getAsFloat();
    assertEquals(123f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_StringValue_NegativeFloat() throws Exception {
    // Create JsonPrimitive with a negative float string
    jsonPrimitive = new JsonPrimitive("-45.67");

    float result = jsonPrimitive.getAsFloat();
    assertEquals(-45.67f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_NumberValue_Integer() throws Exception {
    // Create JsonPrimitive with an Integer value
    jsonPrimitive = new JsonPrimitive(42);

    float result = jsonPrimitive.getAsFloat();
    assertEquals(42f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_NumberValue_Long() throws Exception {
    // Create JsonPrimitive with a Long value
    jsonPrimitive = new JsonPrimitive(1234567890123L);

    float result = jsonPrimitive.getAsFloat();
    assertEquals(1234567890123f, result, 0.1f); // float precision loss allowed
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_NumberValue_LazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is internal, create via reflection
    Object lazilyParsedNumber = createLazilyParsedNumber("56.78");

    // Create JsonPrimitive with LazilyParsedNumber value by reflection
    jsonPrimitive = createJsonPrimitiveWithValue(lazilyParsedNumber);

    float result = jsonPrimitive.getAsFloat();
    assertEquals(56.78f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_StringValue_InvalidFloat_ThrowsNumberFormatException() throws Exception {
    jsonPrimitive = new JsonPrimitive("not-a-number");

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsFloat();
    });
  }

  // Helper to create LazilyParsedNumber instance via reflection
  private Object createLazilyParsedNumber(String value) throws Exception {
    Class<?> clazz = Class.forName("com.google.gson.internal.LazilyParsedNumber");
    return clazz.getConstructor(String.class).newInstance(value);
  }

  // Helper to create JsonPrimitive instance with a specific value via reflection
  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use constructor JsonPrimitive(Number) or JsonPrimitive(String) if possible
    // If value is Number or String, use constructor directly
    if (value instanceof Number) {
      return new JsonPrimitive((Number) value);
    }
    if (value instanceof String) {
      return new JsonPrimitive((String) value);
    }
    // Otherwise create empty JsonPrimitive and set private final field 'value' via reflection
    JsonPrimitive instance = (JsonPrimitive) 
        JsonPrimitive.class.getDeclaredConstructor(String.class).newInstance("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);
    return instance;
  }
}