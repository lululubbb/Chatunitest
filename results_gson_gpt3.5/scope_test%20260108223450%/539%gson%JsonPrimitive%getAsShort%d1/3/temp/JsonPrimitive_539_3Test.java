package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonPrimitive_539_3Test {

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenIsNumberTrue_returnsNumberShortValue() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(Short.valueOf((short)123)));

    // Mock isNumber() to return true
    doReturn(true).when(jsonPrimitive).isNumber();

    // Mock getAsNumber() to return a Number with shortValue 123
    Number numberMock = mock(Number.class);
    when(numberMock.shortValue()).thenReturn((short)123);
    doReturn(numberMock).when(jsonPrimitive).getAsNumber();

    short result = jsonPrimitive.getAsShort();
    assertEquals((short)123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenIsNumberFalse_parsesShortFromString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("456"));

    // Mock isNumber() to return false
    doReturn(false).when(jsonPrimitive).isNumber();

    // Mock getAsString() to return "456"
    doReturn("456").when(jsonPrimitive).getAsString();

    short result = jsonPrimitive.getAsShort();
    assertEquals((short)456, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenIsNumberFalse_parsesNegativeShortFromString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("-789"));

    // Mock isNumber() to return false
    doReturn(false).when(jsonPrimitive).isNumber();

    // Mock getAsString() to return "-789"
    doReturn("-789").when(jsonPrimitive).getAsString();

    short result = jsonPrimitive.getAsShort();
    assertEquals((short)-789, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_usingReflection() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("321"));

    // Mock isNumber() to return false
    doReturn(false).when(jsonPrimitive).isNumber();

    // Mock getAsString() to return "321"
    doReturn("321").when(jsonPrimitive).getAsString();

    Method getAsShortMethod = JsonPrimitive.class.getDeclaredMethod("getAsShort");
    getAsShortMethod.setAccessible(true);
    short result = (short) getAsShortMethod.invoke(jsonPrimitive);
    assertEquals((short)321, result);
  }

}