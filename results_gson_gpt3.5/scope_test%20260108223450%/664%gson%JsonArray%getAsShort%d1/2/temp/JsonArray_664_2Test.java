package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonArray_664_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_singleElement() {
    // Create a mock JsonElement
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsShort()).thenReturn((short) 123);

    // Use reflection to add the mock element to the private elements list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mockElement);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    // Call getAsShort and verify it returns the mocked value
    short result = jsonArray.getAsShort();
    assertEquals(123, result);
    verify(mockElement, times(1)).getAsShort();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_emptyArray_throwsIllegalStateException() {
    // jsonArray is empty by default
    // getAsSingleElement throws IllegalStateException if size != 1
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Prepare a mock element
    JsonElement mockElement = mock(JsonElement.class);
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mockElement);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);
    JsonElement result = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, result);
  }
}