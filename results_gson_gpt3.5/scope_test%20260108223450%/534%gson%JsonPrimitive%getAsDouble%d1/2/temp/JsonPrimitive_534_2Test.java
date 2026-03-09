package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonPrimitive_534_2Test {

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withNumberValue() {
    JsonPrimitive primitive = new JsonPrimitive(123.456);

    double result = primitive.getAsDouble();

    assertEquals(123.456, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withStringValueValidDouble() {
    JsonPrimitive primitive = new JsonPrimitive("789.012");

    double result = primitive.getAsDouble();

    assertEquals(789.012, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withStringValueInvalidDouble_throwsNumberFormatException() {
    JsonPrimitive primitive = new JsonPrimitive("not-a-number");

    assertThrows(NumberFormatException.class, primitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withBooleanValue_throwsNumberFormatException() {
    JsonPrimitive primitive = new JsonPrimitive(true);

    assertThrows(NumberFormatException.class, primitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withCharacterValue_throwsNumberFormatException() {
    JsonPrimitive primitive = new JsonPrimitive('a');

    assertThrows(NumberFormatException.class, primitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_reflectiveInvocation() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("3.14");

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsDouble");
    method.setAccessible(true);
    double result = (double) method.invoke(primitive);

    assertEquals(3.14, result, 0.000001);
  }
}