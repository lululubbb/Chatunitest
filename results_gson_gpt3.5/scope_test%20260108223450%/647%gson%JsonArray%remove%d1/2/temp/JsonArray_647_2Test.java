package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_647_2Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> elements;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to access private final elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    // Initialize a spy ArrayList to track calls and control behavior
    elements = spy(new ArrayList<>());
    elementsField.set(jsonArray, elements);
  }

  @Test
    @Timeout(8000)
  void testRemove_validIndex_returnsElementAndRemoves() {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    elements.add(element1);
    elements.add(element2);

    // Remove element at index 0
    JsonElement removed = jsonArray.remove(0);

    assertSame(element1, removed);
    assertEquals(1, elements.size());
    assertFalse(elements.contains(element1));
    assertTrue(elements.contains(element2));
  }

  @Test
    @Timeout(8000)
  void testRemove_invalidIndex_throwsIndexOutOfBoundsException() {
    elements.add(mock(JsonElement.class));

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(5));
  }

  @Test
    @Timeout(8000)
  void testRemove_emptyList_throwsIndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(0));
  }
}