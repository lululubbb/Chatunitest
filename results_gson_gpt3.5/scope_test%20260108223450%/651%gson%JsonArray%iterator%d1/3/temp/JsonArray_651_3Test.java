package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_651_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void iterator_emptyArray_hasNoElements() {
    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
    @Timeout(8000)
  void iterator_nonEmptyArray_iteratesAllElements() {
    JsonPrimitive element1 = new JsonPrimitive("first");
    JsonPrimitive element2 = new JsonPrimitive(123);
    JsonPrimitive element3 = new JsonPrimitive(true);
    jsonArray.add(element1);
    jsonArray.add(element2);
    jsonArray.add(element3);

    Iterator<JsonElement> iterator = jsonArray.iterator();

    assertTrue(iterator.hasNext());
    assertEquals(element1, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(element2, iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(element3, iterator.next());

    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
    @Timeout(8000)
  void iterator_modificationDuringIteration_reflectsChanges() {
    JsonPrimitive element1 = new JsonPrimitive("one");
    JsonPrimitive element2 = new JsonPrimitive("two");
    jsonArray.add(element1);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element1, iterator.next());

    // Add element after iterator creation
    jsonArray.add(element2);

    // New iterator should see the new element
    Iterator<JsonElement> newIterator = jsonArray.iterator();
    assertTrue(newIterator.hasNext());
    assertEquals(element1, newIterator.next());
    assertTrue(newIterator.hasNext());
    assertEquals(element2, newIterator.next());
    assertFalse(newIterator.hasNext());
  }
}