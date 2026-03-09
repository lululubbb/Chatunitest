package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class JsonPrimitive_538_4Test {

  @Test
    @Timeout(8000)
  public void testGetAsLong_NumberValue() {
    Number numberMock = mock(Number.class);
    when(numberMock.longValue()).thenReturn(123L);

    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class);
    when(jsonPrimitive.isNumber()).thenReturn(true);
    when(jsonPrimitive.getAsNumber()).thenReturn(numberMock);

    // Call real method getAsLong
    when(jsonPrimitive.getAsLong()).thenCallRealMethod();

    long result = jsonPrimitive.getAsLong();
    assertEquals(123L, result);

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
    verify(numberMock).longValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_StringValue() {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class);
    when(jsonPrimitive.isNumber()).thenReturn(false);
    when(jsonPrimitive.getAsString()).thenReturn("456");

    // Call real method getAsLong
    when(jsonPrimitive.getAsLong()).thenCallRealMethod();

    long result = jsonPrimitive.getAsLong();
    assertEquals(456L, result);

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_StringValue_InvalidNumberFormat() {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class);
    when(jsonPrimitive.isNumber()).thenReturn(false);
    when(jsonPrimitive.getAsString()).thenReturn("notANumber");

    // Call real method getAsLong
    when(jsonPrimitive.getAsLong()).thenCallRealMethod();

    assertThrows(NumberFormatException.class, jsonPrimitive::getAsLong);

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);

    Method method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(null, jsonPrimitive);
    assertTrue(result);

    JsonPrimitive jsonPrimitiveDouble = new JsonPrimitive(123.45);
    boolean result2 = (boolean) method.invoke(null, jsonPrimitiveDouble);
    assertFalse(result2);
  }
}