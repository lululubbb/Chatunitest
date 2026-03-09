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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_663_2Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsCharacter_returnsCharacterFromSingleElement() {
    // Use reflection to set the private 'elements' field with a list containing the mock
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(singleElementMock);
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    when(singleElementMock.getAsCharacter()).thenReturn('x');

    char result = jsonArray.getAsCharacter();

    verify(singleElementMock).getAsCharacter();
    assertEquals('x', result);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_returnsOnlyElement() throws Exception {
    // Prepare JsonArray with exactly one element
    var element = mock(JsonElement.class);
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(element);
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    // Access private method getAsSingleElement
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returned = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(element, returned);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_throwsWhenEmpty() throws Exception {
    // elements empty list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> getAsSingleElementMethod.invoke(jsonArray));
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_throwsWhenMultipleElements() throws Exception {
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(mock(JsonElement.class));
      list.add(mock(JsonElement.class));
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> getAsSingleElementMethod.invoke(jsonArray));
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }
}