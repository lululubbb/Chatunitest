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

class JsonArray_660_5Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsLong_shouldReturnLongFromSingleElement() {
    // Use reflection to set private field 'elements' with singleElementMock
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(singleElementMock);
      elementsField.set(jsonArray, elements);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    when(singleElementMock.getAsLong()).thenReturn(123456789L);

    long result = jsonArray.getAsLong();

    assertEquals(123456789L, result);
    verify(singleElementMock).getAsLong();
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_access_shouldReturnSingleElement() throws Exception {
    // Prepare elements list with one JsonElement mock
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(singleElementMock);
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);

    // Access private method getAsSingleElement via reflection
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Object returned = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(singleElementMock, returned);
  }

  @Test
    @Timeout(8000)
  void getAsLong_emptyArray_shouldThrowIllegalStateException() {
    // elements is empty by default, no need to set

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
  }

  @Test
    @Timeout(8000)
  void getAsLong_multipleElements_shouldThrowIllegalStateException() {
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);

    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(firstElement);
      elements.add(secondElement);
      elementsField.set(jsonArray, elements);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });

    verifyNoInteractions(firstElement, secondElement);
  }
}