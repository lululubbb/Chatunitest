package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_653_5Test {

  private JsonArray jsonArray;
  private Method getAsSingleElementMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonArray = new JsonArray();
    getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeOne_ReturnsElement() throws InvocationTargetException, IllegalAccessException {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    // Use reflection to access private field 'elements'
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(mockElement);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to access elements field via reflection");
    }

    // Act
    JsonElement result = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    // Assert
    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeZero_ThrowsIllegalStateException() {
    // Arrange
    // elements list is empty by default

    // Act & Assert
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // InvocationTargetException wraps the actual exception
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeMoreThanOne_ThrowsIllegalStateException() {
    // Arrange
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(mockElement1);
      elements.add(mockElement2);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to access elements field via reflection");
    }

    // Act & Assert
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 2", cause.getMessage());
  }
}