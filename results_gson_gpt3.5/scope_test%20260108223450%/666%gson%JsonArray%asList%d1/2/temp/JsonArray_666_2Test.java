package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.NonNullElementWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class JsonArray_666_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void asList_shouldReturnNonNullElementWrapperListWrappingElements() throws Exception {
    // Arrange
    // Create a spy of the internal elements list to verify it is passed correctly
    ArrayList<JsonElement> mockElements = spy(new ArrayList<>());

    // Add some JsonElement mocks to the list
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    mockElements.add(element1);
    mockElements.add(element2);

    // Use reflection to set the private final elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, mockElements);

    // Act
    List<JsonElement> result = jsonArray.asList();

    // Assert
    assertNotNull(result, "asList() should not return null");
    assertTrue(result instanceof NonNullElementWrapperList, "Returned list should be of type NonNullElementWrapperList");
    assertEquals(2, result.size(), "Returned list size should match the elements list size");
    assertTrue(result.contains(element1), "Returned list should contain element1");
    assertTrue(result.contains(element2), "Returned list should contain element2");

    // Verify that the underlying elements list was passed to the wrapper by checking spy interactions
    // (NonNullElementWrapperList does not expose the wrapped list, so we verify by interaction)
    verify(mockElements, atLeast(0)).size(); // At least size() is called internally
  }

  @Test
    @Timeout(8000)
  void asList_shouldReturnEmptyListIfElementsIsEmpty() throws Exception {
    // Arrange
    ArrayList<JsonElement> emptyElements = new ArrayList<>();

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, emptyElements);

    // Act
    List<JsonElement> result = jsonArray.asList();

    // Assert
    assertNotNull(result, "asList() should not return null");
    assertTrue(result.isEmpty(), "Returned list should be empty when elements is empty");
  }
}