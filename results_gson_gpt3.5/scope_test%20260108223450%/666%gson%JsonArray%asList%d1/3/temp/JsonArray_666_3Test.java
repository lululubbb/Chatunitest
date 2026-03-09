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
import org.mockito.MockedConstruction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class JsonArray_666_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void asList_returnsNonNullElementWrapperListWrappingElements() throws Exception {
    // Prepare a real ArrayList<JsonElement> with some mocked JsonElement objects
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    elementsList.add(mockElement1);
    elementsList.add(mockElement2);

    // Use reflection to set the private final field 'elements' in jsonArray
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elementsList);

    // Call the focal method
    List<JsonElement> result = jsonArray.asList();

    // Assert the returned list is a NonNullElementWrapperList and wraps the same elements list
    assertNotNull(result);
    assertTrue(result instanceof NonNullElementWrapperList);

    // Because NonNullElementWrapperList is a wrapper, its underlying list should equal elementsList
    // We can check that the contents are the same
    assertEquals(elementsList.size(), result.size());
    assertTrue(result.containsAll(elementsList));
  }

  @Test
    @Timeout(8000)
  void asList_withEmptyElements_returnsEmptyNonNullElementWrapperList() throws Exception {
    // Set private final field 'elements' to an empty ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<>());

    List<JsonElement> result = jsonArray.asList();

    assertNotNull(result);
    assertTrue(result instanceof NonNullElementWrapperList);
    assertTrue(result.isEmpty());
  }
}