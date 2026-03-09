package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_533_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No common setup needed as we create instances per test
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsString() throws Exception {
    jsonPrimitive = new JsonPrimitive("testString");
    String result = jsonPrimitive.getAsString();
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(123);
    String result = jsonPrimitive.getAsString();
    assertEquals("123", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsBoolean() throws Exception {
    jsonPrimitive = new JsonPrimitive(true);
    String result = jsonPrimitive.getAsString();
    assertEquals("true", result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsUnexpected_throwsAssertionError() throws Exception {
    jsonPrimitive = createJsonPrimitiveWithValue(new Object());

    AssertionError thrown = assertThrows(AssertionError.class, () -> jsonPrimitive.getAsString());
    assertTrue(thrown.getMessage().contains("Unexpected value type"));
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_and_getAsNumber_withLazilyParsedNumber() throws Exception {
    Object lazilyParsedNumber = new LazilyParsedNumber("456");
    jsonPrimitive = createJsonPrimitiveWithValue(lazilyParsedNumber);

    assertTrue(jsonPrimitive.isNumber());
    assertEquals("456", jsonPrimitive.getAsNumber().toString());
    assertEquals("456", jsonPrimitive.getAsString());
  }

  // Helper method to create JsonPrimitive with arbitrary value using reflection
  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use one of the existing public constructors to create an instance
    JsonPrimitive instance = new JsonPrimitive("temp");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);
    return instance;
  }
}