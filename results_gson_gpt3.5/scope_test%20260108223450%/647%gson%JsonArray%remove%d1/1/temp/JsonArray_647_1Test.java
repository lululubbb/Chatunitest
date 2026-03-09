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

public class JsonArray_647_1Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    element1 = mock(JsonElement.class);
    element2 = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testRemove_validIndex_removesAndReturnsElement() throws Exception {
    // Use reflection to get the private elements field and set it up
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsList.add(element2);
    elementsField.set(jsonArray, elementsList);

    // Remove element at index 0
    JsonElement removed = jsonArray.remove(0);

    assertSame(element1, removed);
    assertEquals(1, elementsList.size());
    assertSame(element2, elementsList.get(0));
  }

  @Test
    @Timeout(8000)
  public void testRemove_invalidIndex_throwsIndexOutOfBoundsException() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsField.set(jsonArray, elementsList);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(1));
  }

  @Test
    @Timeout(8000)
  public void testRemove_emptyList_throwsIndexOutOfBoundsException() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsField.set(jsonArray, elementsList);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(0));
  }
}