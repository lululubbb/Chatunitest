package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_663_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_withSingleElement_returnsCharacter() {
    // Arrange
    JsonElement elementMock = mock(JsonElement.class);
    when(elementMock.getAsCharacter()).thenReturn('x');
    jsonArray.add(elementMock);

    // Act
    char result = jsonArray.getAsCharacter();

    // Assert
    assertEquals('x', result);
    verify(elementMock).getAsCharacter();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_withEmptyArray_throwsException() throws Exception {
    // Arrange
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // Throw the underlying cause to be caught by assertThrows
        throw e.getCause();
      }
    });
    String message = exception.getMessage();
    assertTrue(message.contains("Array must have size 1"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_withMultipleElements_throwsException() throws Exception {
    // Arrange
    JsonElement elementMock1 = mock(JsonElement.class);
    JsonElement elementMock2 = mock(JsonElement.class);
    jsonArray.add(elementMock1);
    jsonArray.add(elementMock2);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // Throw the underlying cause to be caught by assertThrows
        throw e.getCause();
      }
    });
    String message = exception.getMessage();
    assertTrue(message.contains("Array must have size 1"));
  }
}