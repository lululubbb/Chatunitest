package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class JsonArray_644_2Test {

  private JsonArray jsonArray;
  private JsonArray otherArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    otherArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAddAllElementsFromOtherArray() throws Exception {
    // Prepare elements to add
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    // Use reflection to add elements to otherArray's private 'elements' list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> otherElements = (ArrayList<JsonElement>) elementsField.get(otherArray);
    otherElements.add(element1);
    otherElements.add(element2);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> initialElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertTrue(initialElements.isEmpty());

    // Call focal method
    jsonArray.addAll(otherArray);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> resultElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    // Verify all elements were added
    assertEquals(2, resultElements.size());
    assertTrue(resultElements.contains(element1));
    assertTrue(resultElements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAddEmpty_whenOtherArrayIsEmpty() throws Exception {
    // otherArray is empty by default
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> initialElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    initialElements.add(mock(JsonElement.class));

    int initialSize = initialElements.size();

    // Call focal method
    jsonArray.addAll(otherArray);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> resultElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    // Size should remain unchanged
    assertEquals(initialSize, resultElements.size());
  }

  @Test
    @Timeout(8000)
  void addAll_shouldThrowNullPointerException_whenOtherArrayIsNull() {
    assertThrows(NullPointerException.class, () -> jsonArray.addAll(null));
  }
}