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

class JsonArray_638_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyElements_returnsNewEmptyJsonArray() throws Exception {
    // elements is empty initially
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    assertTrue(elements.isEmpty());

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);

    // The new JsonArray should also have empty elements list
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> copyElements = (ArrayList<JsonElement>) elementsField.get(copy);
    assertTrue(copyElements.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_nonEmptyElements_returnsDeepCopiedJsonArray() throws Exception {
    // Prepare mock JsonElements
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);

    when(element1.deepCopy()).thenReturn(element1Copy);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Use reflection to set elements list with mocks
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsList.add(element2);
    elementsField.set(jsonArray, elementsList);

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> copyElements = (ArrayList<JsonElement>) elementsField.get(copy);

    assertEquals(2, copyElements.size());
    assertSame(element1Copy, copyElements.get(0));
    assertSame(element2Copy, copyElements.get(1));

    // Verify that deepCopy was called on each element
    verify(element1).deepCopy();
    verify(element2).deepCopy();
  }
}