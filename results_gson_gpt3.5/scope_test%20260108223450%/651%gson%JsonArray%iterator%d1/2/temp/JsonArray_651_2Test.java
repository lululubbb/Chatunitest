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

class JsonArray_651_2Test {

  JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void iterator_emptyList_noElements() throws Exception {
    // Use reflection to set private final elements field to empty list
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
  void iterator_nonEmptyList_iteratesCorrectly() throws Exception {
    ArrayList<JsonElement> mockList = spy(new ArrayList<>());
    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);
    mockList.add(elem1);
    mockList.add(elem2);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, mockList);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertSame(elem1, iterator.next());
    assertTrue(iterator.hasNext());
    assertSame(elem2, iterator.next());
    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);

    verify(mockList, atLeastOnce()).iterator();
  }
}