package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_659_3Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_withSingleElement_returnsFloat() {
    // Use reflection to set the private field 'elements' with a list containing the mock
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(singleElementMock);
      elementsField.set(jsonArray, elements);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    when(singleElementMock.getAsFloat()).thenReturn(3.14f);

    float result = jsonArray.getAsFloat();

    assertEquals(3.14f, result, 0.0001f);
    verify(singleElementMock).getAsFloat();
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_emptyArray_throwsIllegalStateException() {
    // elements is empty by default, but ensure it explicitly
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, new ArrayList<JsonElement>());
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    assertThrows(IllegalStateException.class, () -> jsonArray.getAsFloat());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_returnsCorrectElement() throws Exception {
    // Prepare elements list with exactly one element
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(singleElementMock);
      elementsField.set(jsonArray, elements);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
    Object result = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(singleElementMock, result);
  }
}