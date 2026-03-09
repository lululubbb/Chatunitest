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

public class JsonArray_644_1Test {

  private JsonArray jsonArray;
  private JsonArray otherArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    otherArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void addAll_shouldAddAllElementsFromOtherArray() throws Exception {
    // Use reflection to access private 'elements' field of both jsonArray and otherArray
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    // Prepare elements for otherArray
    ArrayList<JsonElement> otherElements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    otherElements.add(element1);
    otherElements.add(element2);
    elementsField.set(otherArray, otherElements);

    // Prepare empty elements for jsonArray
    ArrayList<JsonElement> elements = new ArrayList<>();
    elementsField.set(jsonArray, elements);

    // Call addAll
    jsonArray.addAll(otherArray);

    // Verify that jsonArray.elements contains all elements from otherArray.elements
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> updatedElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(2, updatedElements.size());
    assertTrue(updatedElements.contains(element1));
    assertTrue(updatedElements.contains(element2));
  }

  @Test
    @Timeout(8000)
  public void addAll_shouldAddAllElementsWhenJsonArrayHasExistingElements() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> initialElements = new ArrayList<>();
    JsonElement existingElement = mock(JsonElement.class);
    initialElements.add(existingElement);
    elementsField.set(jsonArray, initialElements);

    ArrayList<JsonElement> otherElements = new ArrayList<>();
    JsonElement newElement = mock(JsonElement.class);
    otherElements.add(newElement);
    elementsField.set(otherArray, otherElements);

    jsonArray.addAll(otherArray);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> updatedElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(2, updatedElements.size());
    assertTrue(updatedElements.contains(existingElement));
    assertTrue(updatedElements.contains(newElement));
  }

  @Test
    @Timeout(8000)
  public void addAll_shouldDoNothingWhenOtherArrayIsEmpty() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> initialElements = new ArrayList<>();
    JsonElement existingElement = mock(JsonElement.class);
    initialElements.add(existingElement);
    elementsField.set(jsonArray, initialElements);

    // otherArray elements empty
    ArrayList<JsonElement> otherElements = new ArrayList<>();
    elementsField.set(otherArray, otherElements);

    jsonArray.addAll(otherArray);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> updatedElements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertEquals(1, updatedElements.size());
    assertTrue(updatedElements.contains(existingElement));
  }

  @Test
    @Timeout(8000)
  public void addAll_shouldThrowNullPointerExceptionWhenOtherArrayIsNull() {
    assertThrows(NullPointerException.class, () -> jsonArray.addAll(null));
  }
}