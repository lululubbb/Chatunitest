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

public class JsonPrimitive_540_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setup() {
    // no-op, will create instances in each test
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_whenIsNumber_returnsNumberIntValue() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive(123));

    Number mockNumber = mock(Number.class);
    when(mockNumber.intValue()).thenReturn(456);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(mockNumber).when(spyPrimitive).getAsNumber();

    int result = spyPrimitive.getAsInt();

    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_whenNotNumber_parsesString() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("789"));

    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("789").when(spyPrimitive).getAsString();

    int result = spyPrimitive.getAsInt();

    assertEquals(789, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_whenNotNumber_invalidNumberString_throwsNumberFormatException() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("notANumber"));

    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("notANumber").when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, spyPrimitive::getAsInt);
  }

  @Test
    @Timeout(8000)
  void testPrivateIsIntegralMethod() throws Exception {
    // Use reflection to invoke private static boolean isIntegral(JsonPrimitive primitive)
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive integralPrimitive = new JsonPrimitive(123);
    JsonPrimitive nonIntegralPrimitive = new JsonPrimitive(123.45);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean nonIntegralResult = (boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
  }
}