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

public class JsonPrimitive_540_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No common setup needed for now
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenIsNumberTrue_returnsNumberIntValue() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive(123));
    doReturn(true).when(jsonPrimitive).isNumber();
    Number numberMock = mock(Number.class);
    when(jsonPrimitive.getAsNumber()).thenReturn(numberMock);
    when(numberMock.intValue()).thenReturn(456);

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(456, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
    verify(numberMock).intValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenIsNumberFalse_parsesStringToInt() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive("789"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("789").when(jsonPrimitive).getAsString();

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(789, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenIsNumberFalse_invalidString_throwsNumberFormatException() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive("notANumber"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("notANumber").when(jsonPrimitive).getAsString();

    // Act & Assert
    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsInt());
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod_withIntegralAndNonIntegralValues() throws Exception {
    // Arrange
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive integralPrimitive = new JsonPrimitive(123);
    JsonPrimitive longPrimitive = new JsonPrimitive(123L);
    JsonPrimitive shortPrimitive = new JsonPrimitive((short) 1);
    JsonPrimitive bytePrimitive = new JsonPrimitive((byte) 2);
    JsonPrimitive bigIntegerPrimitive = new JsonPrimitive(java.math.BigInteger.ONE);
    JsonPrimitive doublePrimitive = new JsonPrimitive(3.14);
    JsonPrimitive floatPrimitive = new JsonPrimitive(2.71f);
    JsonPrimitive bigDecimalPrimitive = new JsonPrimitive(java.math.BigDecimal.ONE);

    // Act & Assert
    assertTrue((Boolean) isIntegralMethod.invoke(null, integralPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, longPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, shortPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, bytePrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, bigIntegerPrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, doublePrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, floatPrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, bigDecimalPrimitive));
  }
}