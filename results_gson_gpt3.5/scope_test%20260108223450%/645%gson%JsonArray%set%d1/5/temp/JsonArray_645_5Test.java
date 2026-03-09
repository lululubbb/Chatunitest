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

class JsonArray_645_5Test {

  private JsonArray jsonArray;
  private JsonElement elementMock1;
  private JsonElement elementMock2;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();

    elementMock1 = mock(JsonElement.class);
    elementMock2 = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testSetReplacesElementAtIndex() throws Exception {
    // Use reflection to set private final ArrayList<JsonElement> elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(elementMock1);
    elements.add(elementMock2);
    elementsField.set(jsonArray, elements);

    // Replace element at index 0 with elementMock2
    JsonElement replaced = jsonArray.set(0, elementMock2);
    assertSame(elementMock1, replaced, "Returned element should be the old element at index");
    assertEquals(2, elements.size(), "Size should remain unchanged");
    assertSame(elementMock2, elements.get(0), "Element at index 0 should be replaced with new element");
  }

  @Test
    @Timeout(8000)
  void testSetWithNullElementReplacesWithJsonNullInstance() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(elementMock1);
    elementsField.set(jsonArray, elements);

    JsonElement replaced = jsonArray.set(0, null);
    assertSame(elementMock1, replaced, "Returned element should be the old element at index");
    assertEquals(1, elements.size(), "Size should remain unchanged");
    assertSame(JsonNull.INSTANCE, elements.get(0), "Null element should be replaced with JsonNull.INSTANCE");
  }

  @Test
    @Timeout(8000)
  void testSetThrowsIndexOutOfBoundsExceptionWhenIndexInvalid() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(elementMock1);
    elementsField.set(jsonArray, elements);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(1, elementMock2));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(-1, elementMock2));
  }
}