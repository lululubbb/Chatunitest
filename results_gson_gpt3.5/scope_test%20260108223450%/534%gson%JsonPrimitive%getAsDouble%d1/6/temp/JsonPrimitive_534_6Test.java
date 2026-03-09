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

public class JsonPrimitive_534_6Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no common setup needed for these tests
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_Number() {
    Number number = 42.5;
    jsonPrimitive = spy(new JsonPrimitive(number));
    doReturn(true).when(jsonPrimitive).isNumber();
    doReturn(number).when(jsonPrimitive).getAsNumber();

    double result = jsonPrimitive.getAsDouble();

    assertEquals(42.5, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_NotNumber_ValidDoubleString() {
    String doubleString = "123.456";
    jsonPrimitive = spy(new JsonPrimitive(doubleString));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn(doubleString).when(jsonPrimitive).getAsString();

    double result = jsonPrimitive.getAsDouble();

    assertEquals(123.456, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_NotNumber_InvalidDoubleString_ThrowsNumberFormatException() {
    String invalidDoubleString = "not_a_double";
    jsonPrimitive = spy(new JsonPrimitive(invalidDoubleString));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn(invalidDoubleString).when(jsonPrimitive).getAsString();

    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsDouble());

    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_UsingReflection() throws Exception {
    jsonPrimitive = spy(new JsonPrimitive("3.14"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("3.14").when(jsonPrimitive).getAsString();

    Method getAsDoubleMethod = JsonPrimitive.class.getDeclaredMethod("getAsDouble");
    getAsDoubleMethod.setAccessible(true);

    double result = (double) getAsDoubleMethod.invoke(jsonPrimitive);

    assertEquals(3.14, result);
  }
}