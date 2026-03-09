package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonPrimitive_538_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, will instantiate in each test
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withNumberValue() throws Exception {
    // Create a spy of JsonPrimitive using Mockito
    JsonPrimitive spy = Mockito.spy(new JsonPrimitive(0));

    // Use reflection to set private final field 'value' to a Number
    setValue(spy, 123L);

    // Mock isNumber() and getAsNumber()
    Mockito.when(spy.isNumber()).thenReturn(true);
    Mockito.when(spy.getAsNumber()).thenReturn(123L);

    assertEquals(123L, spy.getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withStringValue() throws Exception {
    // Create a spy of JsonPrimitive using Mockito
    JsonPrimitive spy = Mockito.spy(new JsonPrimitive("0"));

    // Use reflection to set private final field 'value' to a String
    setValue(spy, "456");

    // Mock isNumber() and getAsString()
    Mockito.when(spy.isNumber()).thenReturn(false);
    Mockito.when(spy.getAsString()).thenReturn("456");

    assertEquals(456L, spy.getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withInvalidString_throwsNumberFormatException() throws Exception {
    // Create a spy of JsonPrimitive using Mockito
    JsonPrimitive spy = Mockito.spy(new JsonPrimitive("invalid"));

    // Use reflection to set private final field 'value' to a String
    setValue(spy, "invalid");

    // Mock isNumber() and getAsString()
    Mockito.when(spy.isNumber()).thenReturn(false);
    Mockito.when(spy.getAsString()).thenReturn("invalid");

    assertThrows(NumberFormatException.class, spy::getAsLong);
  }

  private void setValue(JsonPrimitive instance, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);
  }
}