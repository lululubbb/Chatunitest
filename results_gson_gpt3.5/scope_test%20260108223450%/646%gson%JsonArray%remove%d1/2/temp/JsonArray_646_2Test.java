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

class JsonArray_646_2Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    element1 = mock(JsonElement.class);
    element2 = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testRemove_ElementPresent() throws Exception {
    // Use reflection to get the private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    elements.add(element1);
    elements.add(element2);

    // Confirm element1 is present
    assertTrue(elements.contains(element1));

    // Call remove method
    boolean removed = jsonArray.remove(element1);

    // Assert remove returns true
    assertTrue(removed);

    // Assert element1 is removed
    assertFalse(elements.contains(element1));

    // Assert element2 still present
    assertTrue(elements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void testRemove_ElementNotPresent() throws Exception {
    // Use reflection to get the private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    elements.add(element2);

    // element1 is not present
    assertFalse(elements.contains(element1));

    // Call remove method
    boolean removed = jsonArray.remove(element1);

    // Assert remove returns false
    assertFalse(removed);

    // Assert element2 still present
    assertTrue(elements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void testRemove_NullElement() throws Exception {
    // Use reflection to get the private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    elements.add(element1);

    // Call remove with null (should return false)
    boolean removed = jsonArray.remove(null);

    // Assert remove returns false
    assertFalse(removed);

    // Assert existing element still present
    assertTrue(elements.contains(element1));
  }
}