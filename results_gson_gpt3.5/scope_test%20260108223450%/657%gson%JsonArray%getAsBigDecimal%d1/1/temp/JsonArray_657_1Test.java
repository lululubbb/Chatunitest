package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_657_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_singleElementDelegates() throws Exception {
    // Create a mock JsonElement to be returned by getAsSingleElement()
    JsonElement mockElement = mock(JsonElement.class);
    BigDecimal expected = new BigDecimal("123.45");
    when(mockElement.getAsBigDecimal()).thenReturn(expected);

    // Use reflection to replace the private getAsSingleElement() method result by adding one element
    JsonArray array = new JsonArray();
    array.add(mockElement);

    BigDecimal actual = array.getAsBigDecimal();

    assertEquals(expected, actual);

    // Verify mockElement.getAsBigDecimal() was called
    verify(mockElement).getAsBigDecimal();
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_reflection() throws Exception {
    // Add one element to jsonArray
    JsonElement mockElement = mock(JsonElement.class);
    jsonArray.add(mockElement);

    // Access private getAsSingleElement() via reflection
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returned = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, returned);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_emptyArray_throws() {
    // If getAsSingleElement throws (likely IllegalStateException), getAsBigDecimal should propagate
    JsonArray emptyArray = new JsonArray();

    assertThrows(IllegalStateException.class, emptyArray::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_multipleElements_throws() {
    // Add multiple elements to cause getAsSingleElement to throw (if it does)
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigDecimal());
  }
}