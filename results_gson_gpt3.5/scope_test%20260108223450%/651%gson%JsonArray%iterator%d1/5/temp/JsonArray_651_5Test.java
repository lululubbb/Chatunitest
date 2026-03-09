package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_651_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void iterator_emptyArray_noElements() {
    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
    @Timeout(8000)
  void iterator_singleElement_returnsElement() {
    JsonPrimitive element = new JsonPrimitive("test");
    jsonArray.add(element);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
    @Timeout(8000)
  void iterator_multipleElements_returnsAllElementsInOrder() {
    JsonPrimitive element1 = new JsonPrimitive("one");
    JsonPrimitive element2 = new JsonPrimitive("two");
    JsonPrimitive element3 = new JsonPrimitive("three");
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
  }
}