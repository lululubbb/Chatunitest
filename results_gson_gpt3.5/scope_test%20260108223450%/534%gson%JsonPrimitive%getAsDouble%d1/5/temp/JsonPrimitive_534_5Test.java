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

public class JsonPrimitive_534_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, individual tests will initialize jsonPrimitive accordingly
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberTrue_returnsNumberDoubleValue() {
    jsonPrimitive = spy(new JsonPrimitive(42));

    doReturn(true).when(jsonPrimitive).isNumber();
    Number mockNumber = mock(Number.class);
    doReturn(mockNumber).when(jsonPrimitive).getAsNumber();
    when(mockNumber.doubleValue()).thenReturn(123.456);

    double result = jsonPrimitive.getAsDouble();

    assertEquals(123.456, result, 0.0);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
    verify(mockNumber).doubleValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_parsesStringToDouble() {
    jsonPrimitive = spy(new JsonPrimitive("789.012"));

    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("789.012").when(jsonPrimitive).getAsString();

    double result = jsonPrimitive.getAsDouble();

    assertEquals(789.012, result, 0.0);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_whenIsNumberFalse_invalidDouble_throwsNumberFormatException() {
    jsonPrimitive = spy(new JsonPrimitive("notANumber"));

    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("notANumber").when(jsonPrimitive).getAsString();

    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsDouble());

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_usingReflection_onPrivateValueNumber() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(3.14);

    // Access private field 'value' via reflection and verify getAsDouble uses it correctly
    Method getAsDoubleMethod = JsonPrimitive.class.getDeclaredMethod("getAsDouble");
    getAsDoubleMethod.setAccessible(true);

    double result = (double) getAsDoubleMethod.invoke(primitive);

    assertEquals(3.14, result, 0.0);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_usingReflection_onPrivateValueString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("2.71828");

    Method getAsDoubleMethod = JsonPrimitive.class.getDeclaredMethod("getAsDouble");
    getAsDoubleMethod.setAccessible(true);

    double result = (double) getAsDoubleMethod.invoke(primitive);

    assertEquals(2.71828, result, 0.0);
  }
}