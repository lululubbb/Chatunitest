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

class JsonArray_652_6Test {

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
  void testGetReturnsCorrectElement() throws Exception {
    // Use reflection to set private final ArrayList<JsonElement> elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsList.add(element2);
    elementsField.set(jsonArray, elementsList);

    // Test get(0)
    JsonElement result0 = jsonArray.get(0);
    assertSame(element1, result0);

    // Test get(1)
    JsonElement result1 = jsonArray.get(1);
    assertSame(element2, result1);
  }

  @Test
    @Timeout(8000)
  void testGetThrowsIndexOutOfBoundsException() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsField.set(jsonArray, elementsList);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(0));
  }
}