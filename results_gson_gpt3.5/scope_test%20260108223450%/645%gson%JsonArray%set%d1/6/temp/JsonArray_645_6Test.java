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

public class JsonArray_645_6Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;
  private JsonElement elementNull;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    element1 = mock(JsonElement.class);
    element2 = mock(JsonElement.class);
    elementNull = null;

    // Use reflection to initialize the private final elements field with an ArrayList containing element1 and element2
    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> list = new ArrayList<>();
      list.add(element1);
      list.add(element2);
      elementsField.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testSet_ReplacesElementAtIndex_ReturnsOldElement() {
    JsonElement oldElement = jsonArray.set(0, element2);
    assertSame(element1, oldElement, "set should return the old element at index");
    // Verify the element at index 0 is replaced with element2
    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      assertSame(element2, elements.get(0), "Element at index 0 should be replaced with element2");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection access failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testSet_NullElement_ReplacesWithJsonNullInstance() {
    JsonElement oldElement = jsonArray.set(1, null);
    assertSame(element2, oldElement, "set should return the old element at index");
    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      assertSame(JsonNull.INSTANCE, elements.get(1), "Null element should be replaced with JsonNull.INSTANCE");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection access failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testSet_IndexOutOfBounds_ThrowsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(2, element1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(-1, element1));
  }
}