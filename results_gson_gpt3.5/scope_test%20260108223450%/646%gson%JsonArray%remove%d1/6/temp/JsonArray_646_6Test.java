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

class JsonArray_646_6Test {

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
  void remove_existingElement_returnsTrue() throws Exception {
    // Use reflection to get the private 'elements' field and add elements manually
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element1);
    elements.add(element2);

    // Verify remove returns true when element is present
    boolean result = jsonArray.remove(element1);
    assertTrue(result);
    assertFalse(elements.contains(element1));
    assertEquals(1, elements.size());
  }

  @Test
    @Timeout(8000)
  void remove_nonExistingElement_returnsFalse() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element2);

    // Try to remove element1 which is not present
    boolean result = jsonArray.remove(element1);
    assertFalse(result);
    assertEquals(1, elements.size());
    assertTrue(elements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void remove_nullElement_returnsFalse() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element2);

    // Remove null element should return false
    boolean result = jsonArray.remove(null);
    assertFalse(result);
    assertEquals(1, elements.size());
    assertTrue(elements.contains(element2));
  }
}