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

public class JsonPrimitive_534_4Test {

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberTrue_returnsNumberDoubleValue() throws Exception {
    JsonPrimitive primitive = spy(new JsonPrimitive(42));

    doReturn(true).when(primitive).isNumber();
    Number mockedNumber = mock(Number.class);
    doReturn(mockedNumber).when(primitive).getAsNumber();
    when(mockedNumber.doubleValue()).thenReturn(3.14);

    double result = primitive.getAsDouble();

    assertEquals(3.14, result);
    verify(primitive).isNumber();
    verify(primitive).getAsNumber();
    verify(mockedNumber).doubleValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_parsesString() throws Exception {
    JsonPrimitive primitive = spy(new JsonPrimitive("2.718"));

    doReturn(false).when(primitive).isNumber();
    doReturn("2.718").when(primitive).getAsString();

    double result = primitive.getAsDouble();

    assertEquals(2.718, result);
    verify(primitive).isNumber();
    verify(primitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_invalidString_throwsNumberFormatException() throws Exception {
    JsonPrimitive primitive = spy(new JsonPrimitive("notANumber"));

    doReturn(false).when(primitive).isNumber();
    doReturn("notANumber").when(primitive).getAsString();

    Executable executable = primitive::getAsDouble;

    NumberFormatException thrown = assertThrows(NumberFormatException.class, executable);
    assertTrue(thrown.getMessage().contains("notANumber"));
    verify(primitive).isNumber();
    verify(primitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_reflectionInvocation() throws Exception {
    JsonPrimitive primitive = spy(new JsonPrimitive(123));

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsDouble");
    method.setAccessible(true);

    doReturn(true).when(primitive).isNumber();
    Number mockedNumber = mock(Number.class);
    doReturn(mockedNumber).when(primitive).getAsNumber();
    when(mockedNumber.doubleValue()).thenReturn(9.99);

    double result = (double) method.invoke(primitive);

    assertEquals(9.99, result);
    verify(primitive).isNumber();
    verify(primitive).getAsNumber();
    verify(mockedNumber).doubleValue();
  }
}