package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonPrimitive_531_4Test {

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withNumberValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(42);

    Number number = primitive.getAsNumber();

    assertNotNull(number);
    assertEquals(42, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withStringValue() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("123.456");

    Number number = primitive.getAsNumber();

    assertNotNull(number);
    assertTrue(number instanceof LazilyParsedNumber);
    assertEquals("123.456", number.toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withUnsupportedValue_throws() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("dummy");
    // Use reflection to set private final field 'value' to an unsupported type (e.g. Boolean)
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, new Object());

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, primitive::getAsNumber);
    assertEquals("Primitive is neither a number nor a string", exception.getMessage());
  }
}