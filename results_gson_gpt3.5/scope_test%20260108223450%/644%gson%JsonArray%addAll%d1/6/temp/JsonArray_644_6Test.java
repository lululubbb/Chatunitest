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

class JsonArray_644_6Test {

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
    // Arrange: create JsonElements to add
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    JsonElement element3 = mock(JsonElement.class);

    // Add elements to otherArray's private elements list via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> otherElements = (ArrayList<JsonElement>) elementsField.get(otherArray);
    otherElements.add(element1);
    otherElements.add(element2);
    otherElements.add(element3);

    // Make sure jsonArray's elements list is empty initially
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertTrue(jsonElements.isEmpty());

    // Act
    jsonArray.addAll(otherArray);

    // Assert: jsonArray.elements contains all elements from otherArray.elements in order
    assertEquals(3, jsonElements.size());
    assertSame(element1, jsonElements.get(0));
    assertSame(element2, jsonElements.get(1));
    assertSame(element3, jsonElements.get(2));
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAddEmptyWhenOtherArrayIsEmpty() throws Exception {
    // Arrange: otherArray is empty, jsonArray has some elements
    JsonElement element = mock(JsonElement.class);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    jsonElements.add(element);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> otherElements = (ArrayList<JsonElement>) elementsField.get(otherArray);
    assertTrue(otherElements.isEmpty());

    // Act
    jsonArray.addAll(otherArray);

    // Assert: jsonArray.elements remains unchanged
    assertEquals(1, jsonElements.size());
    assertSame(element, jsonElements.get(0));
  }

  @Test
    @Timeout(8000)
  void addAll_shouldAddAllFromItself() throws Exception {
    // Arrange: Add elements to jsonArray
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    jsonElements.add(element1);
    jsonElements.add(element2);

    // Act
    jsonArray.addAll(jsonArray);

    // Assert: elements list size doubled, elements repeated in order
    assertEquals(4, jsonElements.size());
    assertSame(element1, jsonElements.get(0));
    assertSame(element2, jsonElements.get(1));
    assertSame(element1, jsonElements.get(2));
    assertSame(element2, jsonElements.get(3));
  }

}