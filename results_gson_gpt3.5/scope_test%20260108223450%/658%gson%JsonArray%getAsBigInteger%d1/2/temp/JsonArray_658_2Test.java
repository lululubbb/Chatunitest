package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JsonArray_658_2Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_returnsValueFromSingleElement() {
    // Use reflection to set private final elements list with one mocked element
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var elements = new java.util.ArrayList<JsonElement>();
      elements.add(singleElementMock);
      elementsField.set(jsonArray, elements);
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    BigInteger expected = BigInteger.valueOf(123456789L);
    when(singleElementMock.getAsBigInteger()).thenReturn(expected);

    BigInteger actual = jsonArray.getAsBigInteger();

    assertEquals(expected, actual);
    verify(singleElementMock).getAsBigInteger();
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_noElements_throwsException() throws Exception {
    // Set empty elements list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, new java.util.ArrayList<JsonElement>());
    } catch (Exception e) {
      fail("Failed to set elements field via reflection: " + e.getMessage());
    }

    // Access private getAsSingleElement method to check behavior
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Expect getAsBigInteger to throw because getAsSingleElement will throw
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigInteger());
  }
}