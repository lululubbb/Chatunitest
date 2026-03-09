package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;

class JsonArray_644_4Test {

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
    // Prepare elements for otherArray
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    // Use reflection to access private 'elements' field of otherArray and add mocks
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> otherElements = (ArrayList<JsonElement>) elementsField.get(otherArray);
    otherElements.add(element1);
    otherElements.add(element2);
    // Confirm initial sizes
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonArrayElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(0, jsonArrayElements.size());
    assertEquals(2, otherElements.size());

    // Call addAll
    jsonArray.addAll(otherArray);

    // Validate that jsonArray's elements now contain all elements from otherArray
    assertEquals(2, jsonArrayElements.size());
    assertTrue(jsonArrayElements.contains(element1));
    assertTrue(jsonArrayElements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void addAll_withEmptyArray_shouldNotChangeOriginal() throws Exception {
    // Confirm both arrays are initially empty
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonArrayElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> otherElements = (ArrayList<JsonElement>) elementsField.get(otherArray);

    assertTrue(jsonArrayElements.isEmpty());
    assertTrue(otherElements.isEmpty());

    // Call addAll with empty otherArray
    jsonArray.addAll(otherArray);

    // Confirm jsonArray remains empty
    assertTrue(jsonArrayElements.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addAll_withSelf_shouldDuplicateElements() throws Exception {
    // Add element to jsonArray
    JsonElement element = mock(JsonElement.class);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> jsonArrayElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    jsonArrayElements.add(element);

    // Call addAll with itself
    jsonArray.addAll(jsonArray);

    // Elements should be duplicated
    assertEquals(2, jsonArrayElements.size());
    assertEquals(element, jsonArrayElements.get(0));
    assertEquals(element, jsonArrayElements.get(1));
  }
}