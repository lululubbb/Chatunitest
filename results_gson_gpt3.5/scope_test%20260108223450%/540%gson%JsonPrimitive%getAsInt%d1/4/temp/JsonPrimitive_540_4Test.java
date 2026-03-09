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

public class JsonPrimitive_540_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setup() {
    // no-op, will initialize in each test
  }

  @Test
    @Timeout(8000)
  void getAsInt_withNumber_returnsIntValue() {
    Number number = 42;
    jsonPrimitive = new JsonPrimitive(number);
    // Spy to override isNumber() and getAsNumber()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(number).when(spyPrimitive).getAsNumber();

    int result = spyPrimitive.getAsInt();

    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_withStringNumber_returnsParsedInt() {
    String strNumber = "123";
    jsonPrimitive = new JsonPrimitive(strNumber);
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(strNumber).when(spyPrimitive).getAsString();

    int result = spyPrimitive.getAsInt();

    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_withStringNegativeNumber_returnsParsedInt() {
    String strNumber = "-456";
    jsonPrimitive = new JsonPrimitive(strNumber);
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(strNumber).when(spyPrimitive).getAsString();

    int result = spyPrimitive.getAsInt();

    assertEquals(-456, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_withStringNonNumeric_throwsNumberFormatException() {
    String nonNumeric = "notANumber";
    jsonPrimitive = new JsonPrimitive(nonNumeric);
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(nonNumeric).when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, spyPrimitive::getAsInt);
  }

  @Test
    @Timeout(8000)
  void getAsInt_privateIsIntegral_invocation() throws Exception {
    jsonPrimitive = new JsonPrimitive(10);
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean result = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_privateIsIntegral_withNonIntegral() throws Exception {
    JsonPrimitive nonIntegral = new JsonPrimitive(3.14d);
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    boolean result = (boolean) isIntegralMethod.invoke(null, nonIntegral);
    assertFalse(result);
  }
}