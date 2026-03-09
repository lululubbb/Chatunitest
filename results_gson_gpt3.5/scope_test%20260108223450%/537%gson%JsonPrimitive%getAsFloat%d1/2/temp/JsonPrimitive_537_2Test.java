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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonPrimitive_537_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenIsNumberTrue_returnsNumberFloatValue() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(42));

    // Force isNumber to return true
    doReturn(true).when(jsonPrimitive).isNumber();

    // Mock getAsNumber to return a Number with floatValue 3.14f
    Number mockNumber = mock(Number.class);
    when(mockNumber.floatValue()).thenReturn(3.14f);
    doReturn(mockNumber).when(jsonPrimitive).getAsNumber();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(3.14f, result, 0.00001f);

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenIsNumberFalse_parsesString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("2.718"));

    // Force isNumber to return false
    doReturn(false).when(jsonPrimitive).isNumber();

    // Mock getAsString to return "2.718"
    doReturn("2.718").when(jsonPrimitive).getAsString();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(2.718f, result, 0.00001f);

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenIsNumberFalse_parsesStringNegative() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("-123.456"));

    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("-123.456").when(jsonPrimitive).getAsString();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(-123.456f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenIsNumberTrue_integralNumber() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(100));

    doReturn(true).when(jsonPrimitive).isNumber();

    Number mockNumber = mock(Number.class);
    when(mockNumber.floatValue()).thenReturn(100.0f);
    doReturn(mockNumber).when(jsonPrimitive).getAsNumber();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(100.0f, result, 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withLazilyParsedNumber() throws Exception {
    LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber("123.321");
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(lazilyParsedNumber));

    doReturn(true).when(jsonPrimitive).isNumber();
    doReturn(lazilyParsedNumber).when(jsonPrimitive).getAsNumber();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(123.321f, result, 0.00001f);
  }
}