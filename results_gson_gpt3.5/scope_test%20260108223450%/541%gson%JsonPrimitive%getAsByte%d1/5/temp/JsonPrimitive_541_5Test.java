package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_541_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    jsonPrimitive = new JsonPrimitive("0"); // initial dummy value
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNumber_returnsNumberByteValue() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    Number number = (byte) 123;
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(number).when(spyPrimitive).getAsNumber();

    // Act
    byte result = spyPrimitive.getAsByte();

    // Assert
    assertEquals((byte) 123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_parsesString() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    String stringValue = "42";
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(stringValue).when(spyPrimitive).getAsString();

    // Act
    byte result = spyPrimitive.getAsByte();

    // Assert
    assertEquals((byte) 42, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_parsesNegativeByte() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    String stringValue = "-128";
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(stringValue).when(spyPrimitive).getAsString();

    // Act
    byte result = spyPrimitive.getAsByte();

    // Assert
    assertEquals((byte) -128, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_parsesByteMaxValue() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    String stringValue = "127";
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(stringValue).when(spyPrimitive).getAsString();

    // Act
    byte result = spyPrimitive.getAsByte();

    // Assert
    assertEquals(Byte.MAX_VALUE, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_whenNotNumber_invalidString_throwsNumberFormatException() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    String stringValue = "not_a_byte";
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(stringValue).when(spyPrimitive).getAsString();

    // Act & Assert
    assertThrows(NumberFormatException.class, spyPrimitive::getAsByte);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_reflectionInvocation() throws Exception {
    // Arrange
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn((byte) 10).when(spyPrimitive).getAsNumber();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsByte");
    method.setAccessible(true);

    // Act
    byte result = (byte) method.invoke(spyPrimitive);

    // Assert
    assertEquals((byte) 10, result);
  }

}