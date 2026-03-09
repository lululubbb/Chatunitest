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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_654_4Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsNumber_delegatesToSingleElement() throws Exception {
    // Use reflection to set private final field 'elements' with a list containing singleElementMock
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    // Mock getAsNumber of the single element
    Number expectedNumber = 42;
    when(singleElementMock.getAsNumber()).thenReturn(expectedNumber);

    // Invoke getAsNumber on jsonArray
    Number actualNumber = jsonArray.getAsNumber();

    // Verify delegation and returned value
    verify(singleElementMock).getAsNumber();
    assertEquals(expectedNumber, actualNumber);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_returnsFirstElement() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Object returned = getAsSingleElement.invoke(jsonArray);
    assertSame(singleElementMock, returned);
  }

  @Test
    @Timeout(8000)
  void testGetAsNumber_emptyArray_throwsIllegalStateException() throws Exception {
    // elements is empty by default (new JsonArray())
    // getAsSingleElement will throw IllegalStateException because no elements

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsNumber();
    });
  }

  @Test
    @Timeout(8000)
  void testGetAsNumber_singleElementReturnsNullNumber() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    when(singleElementMock.getAsNumber()).thenReturn(null);

    Number result = jsonArray.getAsNumber();
    assertNull(result);
    verify(singleElementMock).getAsNumber();
  }
}