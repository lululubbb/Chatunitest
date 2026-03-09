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

class JsonArray_668_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testHashCode_emptyElements() throws Exception {
    // elements is empty ArrayList by default
    int expectedHashCode = getElementsField(jsonArray).hashCode();
    assertEquals(expectedHashCode, jsonArray.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_nonEmptyElements() throws Exception {
    ArrayList<JsonElement> realElements = new ArrayList<>();
    setElementsField(jsonArray, realElements);

    // Because elements is final and hashCode() is final, Mockito cannot mock it.
    // So we add a real element and check hashCode changes accordingly.
    int initialHashCode = jsonArray.hashCode();

    JsonElement element = mock(JsonElement.class);
    realElements.add(element);

    int newHashCode = jsonArray.hashCode();

    assertNotEquals(initialHashCode, newHashCode);
  }

  // Helper to get private 'elements' field via reflection
  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray jsonArray) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(jsonArray);
  }

  // Helper to set private 'elements' field via reflection
  private void setElementsField(JsonArray jsonArray, ArrayList<JsonElement> elements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);
  }
}