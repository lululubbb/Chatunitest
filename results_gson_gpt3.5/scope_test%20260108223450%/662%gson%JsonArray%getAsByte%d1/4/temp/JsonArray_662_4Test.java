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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_662_4Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsByte_returnsByteFromSingleElement() throws Exception {
    // Arrange
    byte expected = 42;
    // Use reflection to set private field 'elements' with a single mocked JsonElement
    java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
    elementsList.add(mockSingleElement);
    elementsField.set(jsonArray, elementsList);

    when(mockSingleElement.getAsByte()).thenReturn(expected);

    // Act
    byte actual = jsonArray.getAsByte();

    // Assert
    assertEquals(expected, actual);
    verify(mockSingleElement).getAsByte();
  }

  @Test
    @Timeout(8000)
  void getAsByte_emptyArray_throwsIllegalStateException() throws Exception {
    // Arrange
    // empty elements list
    java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
    elementsField.set(jsonArray, elementsList);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  void getAsByte_multipleElements_throwsIllegalStateException() throws Exception {
    // Arrange
    java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();

    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    elementsList.add(first);
    elementsList.add(second);
    elementsField.set(jsonArray, elementsList);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_privateMethod_invocation() throws Exception {
    // Arrange
    java.lang.reflect.Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.ArrayList<JsonElement> elementsList = new java.util.ArrayList<>();
    elementsList.add(mockSingleElement);
    elementsField.set(jsonArray, elementsList);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act
    JsonElement result = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    // Assert
    assertSame(mockSingleElement, result);
  }
}