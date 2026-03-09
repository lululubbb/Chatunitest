package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Method;

public class JsonPrimitive_538_3Test {

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberTrue_returnsNumberLongValue() {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive(123L));
    doReturn(true).when(jsonPrimitive).isNumber();
    Number mockNumber = mock(Number.class);
    doReturn(mockNumber).when(jsonPrimitive).getAsNumber();
    when(mockNumber.longValue()).thenReturn(456L);

    long result = jsonPrimitive.getAsLong();

    assertEquals(456L, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
    verify(mockNumber).longValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberFalse_returnsParsedLongFromString() {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive("789"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("789").when(jsonPrimitive).getAsString();

    long result = jsonPrimitive.getAsLong();

    assertEquals(789L, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberFalse_invalidString_throwsNumberFormatException() {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive("notANumber"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("notANumber").when(jsonPrimitive).getAsString();

    Executable executable = () -> jsonPrimitive.getAsLong();

    assertThrows(NumberFormatException.class, executable);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    JsonPrimitive integralPrimitive = new JsonPrimitive(123);
    JsonPrimitive decimalPrimitive = new JsonPrimitive(123.45);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean decimalResult = (boolean) isIntegralMethod.invoke(null, decimalPrimitive);

    assertTrue(integralResult);
    assertFalse(decimalResult);
  }
}