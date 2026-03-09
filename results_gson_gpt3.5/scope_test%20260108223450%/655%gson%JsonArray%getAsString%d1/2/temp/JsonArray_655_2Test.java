package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_655_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_singleElement() {
    JsonElement element = mock(JsonElement.class);
    when(element.getAsString()).thenReturn("mockedString");

    // Use reflection to add the mocked element to the private elements list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(element);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    String result = jsonArray.getAsString();
    assertEquals("mockedString", result);
    verify(element).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_noElements_throwsException() {
    // By default elements is empty, getAsSingleElement() will throw IllegalStateException when size != 1
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonElement element = mock(JsonElement.class);
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(element);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);
    JsonElement returned = (JsonElement) getAsSingleElement.invoke(jsonArray);
    assertSame(element, returned);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_empty_throwsException() throws NoSuchMethodException {
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);
    assertThrows(InvocationTargetException.class, () -> getAsSingleElement.invoke(jsonArray));
  }
}