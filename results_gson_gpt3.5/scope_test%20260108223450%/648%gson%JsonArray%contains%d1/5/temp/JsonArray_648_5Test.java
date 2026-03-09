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

class JsonArray_648_5Test {

  private JsonArray jsonArray;
  private JsonElement elementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    elementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void contains_returnsTrue_whenElementPresent() throws Exception {
    // Use reflection to set elements field with a list containing elementMock
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(elementMock);
    elementsField.set(jsonArray, elementsList);

    assertTrue(jsonArray.contains(elementMock));
  }

  @Test
    @Timeout(8000)
  void contains_returnsFalse_whenElementNotPresent() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsField.set(jsonArray, elementsList);

    assertFalse(jsonArray.contains(elementMock));
  }

  @Test
    @Timeout(8000)
  void contains_returnsFalse_whenElementsIsEmpty() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> emptyList = new ArrayList<>();
    elementsField.set(jsonArray, emptyList);

    assertFalse(jsonArray.contains(elementMock));
  }

  @Test
    @Timeout(8000)
  void contains_returnsFalse_whenElementIsNull() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    // Instead of adding null, add JsonNull.INSTANCE to match Gson's null element representation
    elementsList.add(JsonNull.INSTANCE);
    elementsField.set(jsonArray, elementsList);

    assertFalse(jsonArray.contains(null));
  }

}