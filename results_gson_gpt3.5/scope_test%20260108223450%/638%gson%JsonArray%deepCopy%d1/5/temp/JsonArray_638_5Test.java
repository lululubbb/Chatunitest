package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_638_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyElements_returnsNewEmptyJsonArray() throws Exception {
    // Ensure elements is empty
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);
    // The elements list inside copy should be empty
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> copyElements = (ArrayList<JsonElement>) elementsField.get(copy);
    assertTrue(copyElements.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_nonEmptyElements_returnsDeepCopiedJsonArray() throws Exception {
    // Prepare mock JsonElement with deepCopy
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Add elements to jsonArray
    jsonArray.add(element1);
    jsonArray.add(element2);

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);

    // Retrieve elements field from copy
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> copyElements = (ArrayList<JsonElement>) elementsField.get(copy);

    assertEquals(2, copyElements.size());
    assertSame(element1Copy, copyElements.get(0));
    assertSame(element2Copy, copyElements.get(1));

    // Verify deepCopy called on each element
    verify(element1, times(1)).deepCopy();
    verify(element2, times(1)).deepCopy();
  }
}