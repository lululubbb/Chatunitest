package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_644_3Test {

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
    // Prepare elements to add in otherArray
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    // Use reflection to add elements to otherArray's private elements list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> otherElements = new ArrayList<>();
    otherElements.add(element1);
    otherElements.add(element2);
    elementsField.set(otherArray, otherElements);

    // Initially jsonArray's elements list is empty
    ArrayList<JsonElement> jsonElements = new ArrayList<>();
    elementsField.set(jsonArray, jsonElements);

    // Call addAll
    jsonArray.addAll(otherArray);

    // Verify jsonArray's elements now contain all elements from otherArray
    ArrayList<JsonElement> resultElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(2, resultElements.size());
    assertTrue(resultElements.contains(element1));
    assertTrue(resultElements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAddEmptyWhenOtherArrayIsEmpty() throws Exception {
    // otherArray elements empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> emptyOtherElements = new ArrayList<>();
    elementsField.set(otherArray, emptyOtherElements);

    ArrayList<JsonElement> jsonElements = new ArrayList<>();
    elementsField.set(jsonArray, jsonElements);

    jsonArray.addAll(otherArray);

    ArrayList<JsonElement> resultElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertTrue(resultElements.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAppendToExistingElements() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    JsonElement existing = mock(JsonElement.class);
    ArrayList<JsonElement> jsonElements = new ArrayList<>();
    jsonElements.add(existing);
    elementsField.set(jsonArray, jsonElements);

    JsonElement newElement = mock(JsonElement.class);
    ArrayList<JsonElement> otherElements = new ArrayList<>();
    otherElements.add(newElement);
    elementsField.set(otherArray, otherElements);

    jsonArray.addAll(otherArray);

    ArrayList<JsonElement> resultElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(2, resultElements.size());
    assertTrue(resultElements.contains(existing));
    assertTrue(resultElements.contains(newElement));
  }
}