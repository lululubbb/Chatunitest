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

class JsonArray_660_1Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsLong_singleElement_returnsLong() {
    // Add mocked element to the internal elements list via reflection
    addElementToJsonArray(jsonArray, mockElement);

    when(mockElement.getAsLong()).thenReturn(123L);

    long result = jsonArray.getAsLong();

    assertEquals(123L, result);
    verify(mockElement).getAsLong();
  }

  @Test
    @Timeout(8000)
  void getAsLong_emptyArray_throwsIllegalStateException() {
    // jsonArray is empty by default
    // getAsSingleElement is private, so we invoke getAsLong and expect failure

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_access_returnsFirstElement() throws Exception {
    addElementToJsonArray(jsonArray, mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_access_empty_throwsIllegalStateException() throws Exception {
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  private void addElementToJsonArray(JsonArray array, JsonElement element) {
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(array);
      elements.add(element);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}