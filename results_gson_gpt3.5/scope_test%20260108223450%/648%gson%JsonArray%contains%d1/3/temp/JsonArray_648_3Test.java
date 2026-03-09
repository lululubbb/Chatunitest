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

class JsonArray_648_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnFalse_whenElementIsNull() throws Exception {
    // elements list is empty by default
    assertFalse(jsonArray.contains(null));
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnFalse_whenElementNotPresent() throws Exception {
    JsonElement element = mock(JsonElement.class);
    // Inject elements list with one element different from the one to check
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(mock(JsonElement.class));
    setElementsField(jsonArray, elements);

    assertFalse(jsonArray.contains(element));
  }

  @Test
    @Timeout(8000)
  void contains_shouldReturnTrue_whenElementPresent() throws Exception {
    JsonElement element = mock(JsonElement.class);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element);
    setElementsField(jsonArray, elements);

    assertTrue(jsonArray.contains(element));
  }

  private void setElementsField(JsonArray jsonArray, ArrayList<JsonElement> elements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);
  }
}