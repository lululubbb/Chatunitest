package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_540_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no common setup required
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenNumber_returnsIntValue() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(42);
    // spy to mock isNumber and getAsNumber
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(123).when(spyPrimitive).getAsNumber();

    // Act
    int result = spyPrimitive.getAsInt();

    // Assert
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenNotNumber_parsesString() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive("456");
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("456").when(spyPrimitive).getAsString();

    // Act
    int result = spyPrimitive.getAsInt();

    // Assert
    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_whenNotNumber_invalidString_throwsNumberFormatException() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive("notANumber");
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("notANumber").when(spyPrimitive).getAsString();

    // Act & Assert
    assertThrows(NumberFormatException.class, spyPrimitive::getAsInt);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_reflectionInvoke() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(10);
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(77).when(spyPrimitive).getAsNumber();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsInt");
    method.setAccessible(true);

    // Act
    int result = (int) method.invoke(spyPrimitive);

    // Assert
    assertEquals(77, result);
  }
}