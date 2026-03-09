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

class JsonArray_645_2Test {

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
  void set_ReplacesElementAtIndex_ReturnsPreviousElement() throws Exception {
    // Use reflection to set the private elements list with 2 elements
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    elements.add(element2);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);

    // Replace element at index 1 with element1
    JsonElement returned = jsonArray.set(1, element1);

    // The returned element should be the previous element at index 1 (element2)
    assertSame(element2, returned);

    // The element at index 1 should now be element1
    assertSame(element1, elements.get(1));
  }

  @Test
    @Timeout(8000)
  void set_NullElement_ReplacesWithJsonNullInstance() throws Exception {
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);

    JsonElement returned = jsonArray.set(0, null);

    // Returned element is the previous element (element1)
    assertSame(element1, returned);

    // The element at index 0 should be JsonNull.INSTANCE
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void set_EmptyElementsList_IndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(0, element1));
  }

  @Test
    @Timeout(8000)
  void set_IndexOutOfBounds_NegativeIndex() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(-1, element1));
  }

  @Test
    @Timeout(8000)
  void set_IndexOutOfBounds_IndexTooLarge() throws Exception {
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(2, element2));
  }
}