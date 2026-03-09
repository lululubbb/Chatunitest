package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_534_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, each test will instantiate its own JsonPrimitive as needed
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberTrue_returnsNumberDoubleValue() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive(42));

    doReturn(true).when(spyPrimitive).isNumber();
    Number mockNumber = mock(Number.class);
    when(spyPrimitive.getAsNumber()).thenReturn(mockNumber);
    when(mockNumber.doubleValue()).thenReturn(123.456);

    double result = spyPrimitive.getAsDouble();

    assertEquals(123.456, result, 0.0);
    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsNumber();
    verify(mockNumber).doubleValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_parsesString() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("3.14159"));

    doReturn(false).when(spyPrimitive).isNumber();
    when(spyPrimitive.getAsString()).thenReturn("3.14159");

    double result = spyPrimitive.getAsDouble();

    assertEquals(3.14159, result, 0.0);
    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_parsesNegativeNumberString() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("-987.654"));

    doReturn(false).when(spyPrimitive).isNumber();
    when(spyPrimitive.getAsString()).thenReturn("-987.654");

    double result = spyPrimitive.getAsDouble();

    assertEquals(-987.654, result, 0.0);
    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean result = (boolean) isIntegralMethod.invoke(null, primitive);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod_withNonIntegral() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(12.34);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean result = (boolean) isIntegralMethod.invoke(null, primitive);

    assertFalse(result);
  }
}