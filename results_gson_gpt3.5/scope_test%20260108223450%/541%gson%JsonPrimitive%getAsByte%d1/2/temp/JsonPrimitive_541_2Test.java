package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonPrimitive_541_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNumber_returnsByteValue() throws Exception {
    Number numberMock = mock(Number.class);
    when(numberMock.byteValue()).thenReturn((byte) 42);

    // Use reflection to create a JsonPrimitive instance with a Number value
    JsonPrimitive primitive = new JsonPrimitive((Number) 0);
    // Use reflection to set the private final field 'value' to numberMock
    var valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, numberMock);

    // Use reflection to override isNumber and getAsNumber by proxies is complicated,
    // instead, rely on the value field and the actual implementation of isNumber/getAsNumber
    // Since the value is a Number, isNumber() should return true and getAsNumber() should return value

    byte result = primitive.getAsByte();
    assertEquals((byte) 42, result);
    verify(numberMock).byteValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_parsesByteFromString() throws Exception {
    // Create JsonPrimitive with String "123"
    JsonPrimitive primitive = new JsonPrimitive("123");

    byte result = primitive.getAsByte();
    assertEquals((byte) 123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_invalidByte_throwsNumberFormatException() {
    JsonPrimitive primitive = new JsonPrimitive("not_a_byte");

    assertThrows(NumberFormatException.class, primitive::getAsByte);
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    // Create JsonPrimitive instances with different values to test isIntegral
    JsonPrimitive integralPrimitive = new JsonPrimitive(10);
    JsonPrimitive nonIntegralPrimitive = new JsonPrimitive(10.5);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    Boolean integralResult = (Boolean) isIntegralMethod.invoke(null, integralPrimitive);
    Boolean nonIntegralResult = (Boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
  }
}