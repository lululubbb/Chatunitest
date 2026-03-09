package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_651_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void iterator_emptyElements_returnsEmptyIterator() throws Exception {
    // Access private field elements via reflection and set empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
    @Timeout(8000)
  void iterator_nonEmptyElements_returnsIteratorWithElements() throws Exception {
    // Prepare elements list with mock JsonElements
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    JsonElement elem1 = new JsonPrimitive("elem1");
    JsonElement elem2 = new JsonPrimitive(2);

    elementsList.add(elem1);
    elementsList.add(elem2);

    // Set private field elements via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elementsList);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals(elem1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(elem2, iterator.next());
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }
}