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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonArray_662_1Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_withSingleElement_returnsByte() {
    // Arrange
    byte expected = 42;
    when(mockSingleElement.getAsByte()).thenReturn(expected);
    setElementsField(jsonArray, mockSingleElement);

    // Act
    byte actual = jsonArray.getAsByte();

    // Assert
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_withEmptyElements_throwsIllegalStateException() {
    // Arrange
    setElementsField(jsonArray); // empty list

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_withMultipleElements_throwsIllegalStateException() {
    // Arrange
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);
    when(firstElement.getAsByte()).thenReturn((byte) 10);
    when(secondElement.getAsByte()).thenReturn((byte) 20);
    setElementsField(jsonArray, firstElement, secondElement);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
  }

  // Helper method to set private final ArrayList<JsonElement> elements via reflection
  @SuppressWarnings("unchecked")
  private void setElementsField(JsonArray jsonArray, JsonElement... elements) {
    try {
      java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      java.util.ArrayList<JsonElement> list = new java.util.ArrayList<>();
      for (JsonElement e : elements) {
        list.add(e);
      }
      elementsField.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflectiveInvocation_returnsCorrectElement() throws Exception {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    setElementsField(jsonArray, element);

    // Use reflection to invoke private getAsSingleElement
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act
    JsonElement returned = (JsonElement) method.invoke(jsonArray);

    // Assert
    assertSame(element, returned);
  }

}