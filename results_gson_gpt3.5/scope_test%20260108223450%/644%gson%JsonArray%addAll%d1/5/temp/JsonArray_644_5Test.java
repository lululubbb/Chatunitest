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

class JsonArray_644_5Test {

  private JsonArray jsonArray;
  private JsonArray otherArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    otherArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void addAll_emptyToEmpty_resultsInEmpty() throws Exception {
    jsonArray.addAll(otherArray);
    assertEquals(0, getElements(jsonArray).size());
  }

  @Test
    @Timeout(8000)
  void addAll_nonEmptyToEmpty_addsAllElements() throws Exception {
    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);
    addElement(otherArray, elem1);
    addElement(otherArray, elem2);

    jsonArray.addAll(otherArray);

    ArrayList<JsonElement> elements = getElements(jsonArray);
    assertEquals(2, elements.size());
    assertTrue(elements.contains(elem1));
    assertTrue(elements.contains(elem2));
  }

  @Test
    @Timeout(8000)
  void addAll_emptyToNonEmpty_appendsNoElements() throws Exception {
    JsonElement elem1 = mock(JsonElement.class);
    addElement(jsonArray, elem1);

    jsonArray.addAll(otherArray);

    ArrayList<JsonElement> elements = getElements(jsonArray);
    assertEquals(1, elements.size());
    assertTrue(elements.contains(elem1));
  }

  @Test
    @Timeout(8000)
  void addAll_nonEmptyToNonEmpty_appendsAllElements() throws Exception {
    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);
    JsonElement elem3 = mock(JsonElement.class);
    addElement(jsonArray, elem1);
    addElement(otherArray, elem2);
    addElement(otherArray, elem3);

    jsonArray.addAll(otherArray);

    ArrayList<JsonElement> elements = getElements(jsonArray);
    assertEquals(3, elements.size());
    assertTrue(elements.contains(elem1));
    assertTrue(elements.contains(elem2));
    assertTrue(elements.contains(elem3));
  }

  @Test
    @Timeout(8000)
  void addAll_self_addsElementsToEnd() throws Exception {
    JsonElement elem1 = mock(JsonElement.class);
    addElement(jsonArray, elem1);

    jsonArray.addAll(jsonArray);

    ArrayList<JsonElement> elements = getElements(jsonArray);
    assertEquals(2, elements.size());
    assertEquals(elem1, elements.get(0));
    assertEquals(elem1, elements.get(1));
  }

  // Helper method to access private 'elements' field via reflection
  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElements(JsonArray array) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(array);
  }

  // Helper method to add element to private 'elements' list via reflection
  private void addElement(JsonArray array, JsonElement element) throws Exception {
    getElements(array).add(element);
  }
}