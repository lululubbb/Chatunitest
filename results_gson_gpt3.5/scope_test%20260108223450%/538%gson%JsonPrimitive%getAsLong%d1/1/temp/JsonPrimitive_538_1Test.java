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

public class JsonPrimitive_538_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberTrue_shouldReturnNumberLongValue() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive(123L));
    doReturn(true).when(spyPrimitive).isNumber();
    Number numberMock = mock(Number.class);
    when(spyPrimitive.getAsNumber()).thenReturn(numberMock);
    when(numberMock.longValue()).thenReturn(456L);

    long result = spyPrimitive.getAsLong();

    assertEquals(456L, result);
    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsNumber();
    verify(numberMock).longValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberFalse_shouldParseLongFromString() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("789"));
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("789").when(spyPrimitive).getAsString();

    long result = spyPrimitive.getAsLong();

    assertEquals(789L, result);
    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenIsNumberFalse_invalidLongString_shouldThrowNumberFormatException() {
    JsonPrimitive spyPrimitive = spy(new JsonPrimitive("notANumber"));
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("notANumber").when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, () -> {
      spyPrimitive.getAsLong();
    });

    verify(spyPrimitive).isNumber();
    verify(spyPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(123);
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);
    boolean result = (boolean) isIntegral.invoke(null, primitive);
    assertTrue(result);

    JsonPrimitive primitiveString = new JsonPrimitive("123");
    boolean resultString = (boolean) isIntegral.invoke(null, primitiveString);
    assertFalse(resultString);
  }
}