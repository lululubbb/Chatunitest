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

public class JsonPrimitive_539_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no common setup needed
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenIsNumber_returnsShortValue() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive(123));
    doReturn(true).when(jsonPrimitive).isNumber();
    Number mockedNumber = mock(Number.class);
    doReturn(mockedNumber).when(jsonPrimitive).getAsNumber();
    when(mockedNumber.shortValue()).thenReturn((short) 123);

    // Act
    short result = jsonPrimitive.getAsShort();

    // Assert
    assertEquals(123, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsNumber();
    verify(mockedNumber).shortValue();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNotNumber_parsesShortFromString() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive("456"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("456").when(jsonPrimitive).getAsString();

    // Act
    short result = jsonPrimitive.getAsShort();

    // Assert
    assertEquals(456, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNotNumber_parsesNegativeShortFromString() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive("-32768"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("-32768").when(jsonPrimitive).getAsString();

    // Act
    short result = jsonPrimitive.getAsShort();

    // Assert
    assertEquals((short) -32768, result);
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNotNumber_stringNotParsable_throwsNumberFormatException() {
    // Arrange
    jsonPrimitive = spy(new JsonPrimitive("notANumber"));
    doReturn(false).when(jsonPrimitive).isNumber();
    doReturn("notANumber").when(jsonPrimitive).getAsString();

    // Act & Assert
    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsShort());
    verify(jsonPrimitive).isNumber();
    verify(jsonPrimitive).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(123);
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    // Act
    boolean result = (boolean) isIntegralMethod.invoke(null, jsonPrimitive);

    // Assert
    // For Number 123, isIntegral should be true
    assertTrue(result);
  }
}