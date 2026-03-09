package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_651_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void iterator_emptyElements_returnsEmptyIterator() throws Exception {
    // Use reflection to clear elements list to ensure empty
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
    @Timeout(8000)
  void iterator_nonEmptyElements_returnsIteratorWithAllElements() throws Exception {
    // Prepare elements list with mocks
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();

    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    elements.add(mockElement1);
    elements.add(mockElement2);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals(mockElement1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(mockElement2, iterator.next());
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }
}