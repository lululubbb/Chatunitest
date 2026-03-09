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

class JsonArray_652_4Test {

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
  void testGet_withValidIndex_returnsElement() throws Exception {
    // Using reflection to set private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(elementMock1);
    elements.add(elementMock2);
    elementsField.set(jsonArray, elements);

    JsonElement result0 = jsonArray.get(0);
    JsonElement result1 = jsonArray.get(1);

    assertSame(elementMock1, result0);
    assertSame(elementMock2, result1);
  }

  @Test
    @Timeout(8000)
  void testGet_withIndexOutOfBounds_throwsException() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(elementMock1);
    elementsField.set(jsonArray, elements);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
  }
}