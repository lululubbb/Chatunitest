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

public class JsonPrimitive_537_3Test {

  @Test
    @Timeout(8000)
  void testGetAsFloat_whenIsNumberTrue_returnsNumberFloatValue() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("123.45"));

    // Mock isNumber() to return true
    doReturn(true).when(jsonPrimitive).isNumber();

    // Create a Number instance to be returned by getAsNumber()
    Number number = 99.99f;

    // Mock getAsNumber() to return the above number
    doReturn(number).when(jsonPrimitive).getAsNumber();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(number.floatValue(), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_whenIsNumberFalse_returnsParsedFloatFromString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("456.78"));

    // Mock isNumber() to return false
    doReturn(false).when(jsonPrimitive).isNumber();

    // Mock getAsString() to return a valid float string
    doReturn("456.78").when(jsonPrimitive).getAsString();

    float result = jsonPrimitive.getAsFloat();

    assertEquals(Float.parseFloat("456.78"), result);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_whenIsNumberFalse_invalidFloatString_throwsNumberFormatException() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("not_a_float"));

    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("not_a_float").when(jsonPrimitive).getAsString();

    assertThrows(NumberFormatException.class, jsonPrimitive::getAsFloat);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_withPrivateValueFieldNumber() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("0"); // dummy init

    // Use reflection to set private final field 'value' to a Number
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, 123.456f);

    // Use reflection to invoke private method isNumber()
    Method isNumberMethod = JsonPrimitive.class.getDeclaredMethod("isNumber");
    isNumberMethod.setAccessible(true);
    boolean isNumber = (boolean) isNumberMethod.invoke(jsonPrimitive);

    // Use reflection to invoke getAsNumber()
    Method getAsNumberMethod = JsonPrimitive.class.getDeclaredMethod("getAsNumber");
    getAsNumberMethod.setAccessible(true);
    Number number = (Number) getAsNumberMethod.invoke(jsonPrimitive);

    // Now call getAsFloat normally
    float result = jsonPrimitive.getAsFloat();

    if (isNumber) {
      assertEquals(number.floatValue(), result);
    } else {
      String str = jsonPrimitive.getAsString();
      assertEquals(Float.parseFloat(str), result);
    }
  }
}