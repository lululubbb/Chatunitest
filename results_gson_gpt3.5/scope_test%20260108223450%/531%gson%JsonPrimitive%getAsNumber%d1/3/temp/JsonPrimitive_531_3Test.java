package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonPrimitive_531_3Test {

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withNumberValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(42);

    Number result = primitive.getAsNumber();

    assertEquals(42, result.intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withStringValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("123.45");

    Number result = primitive.getAsNumber();

    assertTrue(result instanceof LazilyParsedNumber);
    assertEquals("123.45", result.toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withUnsupportedValue_throws() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(true);

    // Use reflection to set private field 'value' to an unsupported type (e.g. Object)
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, new Object());

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, primitive::getAsNumber);
    assertEquals("Primitive is neither a number nor a string", exception.getMessage());
  }
}