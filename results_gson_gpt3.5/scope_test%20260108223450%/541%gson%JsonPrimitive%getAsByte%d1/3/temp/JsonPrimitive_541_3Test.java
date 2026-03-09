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

public class JsonPrimitive_541_3Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    // no-op, instances created per test
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenNumber_returnsByteValue() {
    Number number = mock(Number.class);
    when(number.byteValue()).thenReturn((byte) 42);

    JsonPrimitive primitive = spy(new JsonPrimitive(0));
    doReturn(true).when(primitive).isNumber();
    doReturn(number).when(primitive).getAsNumber();

    byte result = primitive.getAsByte();

    assertEquals(42, result);
    verify(primitive).isNumber();
    verify(primitive).getAsNumber();
    verify(number).byteValue();
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenNotNumber_parsesByteFromString() {
    JsonPrimitive primitive = spy(new JsonPrimitive("123"));
    doReturn(false).when(primitive).isNumber();
    doReturn("123").when(primitive).getAsString();

    byte result = primitive.getAsByte();

    assertEquals(123, result);
    verify(primitive).isNumber();
    verify(primitive).getAsString();
  }

  @Test
    @Timeout(8000)
  void getAsByte_whenNotNumber_invalidByte_throwsNumberFormatException() {
    JsonPrimitive primitive = spy(new JsonPrimitive("notANumber"));
    doReturn(false).when(primitive).isNumber();
    doReturn("notANumber").when(primitive).getAsString();

    assertThrows(NumberFormatException.class, primitive::getAsByte);

    verify(primitive).isNumber();
    verify(primitive).getAsString();
  }

  @Test
    @Timeout(8000)
  void testPrivateIsIntegralUsingReflection() throws Exception {
    JsonPrimitive integralPrimitive = new JsonPrimitive(10);
    JsonPrimitive decimalPrimitive = new JsonPrimitive(10.5);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean decimalResult = (boolean) isIntegralMethod.invoke(null, decimalPrimitive);

    assertTrue(integralResult);
    assertFalse(decimalResult);
  }
}