package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;

class JsonArray_657_2Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_withSingleElement() throws Exception {
    // Use reflection to set the private field 'elements' with a list containing the mocked element
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var elementsList = new java.util.ArrayList<JsonElement>();
    elementsList.add(singleElementMock);
    elementsField.set(jsonArray, elementsList);

    // Mock getAsBigDecimal() on the single element to return a specific BigDecimal
    BigDecimal expected = new BigDecimal("123.45");
    when(singleElementMock.getAsBigDecimal()).thenReturn(expected);

    // Invoke getAsBigDecimal and verify the result
    BigDecimal actual = jsonArray.getAsBigDecimal();
    assertEquals(expected, actual);

    // Verify getAsBigDecimal was called exactly once on the single element
    verify(singleElementMock, times(1)).getAsBigDecimal();
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_emptyArray_throwsException() throws Exception {
    // Set empty elements list via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new java.util.ArrayList<JsonElement>());

    // The getAsSingleElement() method is private, so getAsBigDecimal() should throw an exception
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_multipleElements_usesFirst() throws Exception {
    // Prepare multiple mocked elements
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var elementsList = new java.util.ArrayList<JsonElement>();
    elementsList.add(firstElement);
    elementsField.set(jsonArray, elementsList);

    BigDecimal expected = new BigDecimal("999.99");
    when(firstElement.getAsBigDecimal()).thenReturn(expected);

    BigDecimal actual = jsonArray.getAsBigDecimal();
    assertEquals(expected, actual);

    verify(firstElement, times(1)).getAsBigDecimal();
    verifyNoInteractions(secondElement);
  }
}