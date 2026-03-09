package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_657_5Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_returnsValueFromSingleElement() throws Exception {
    BigDecimal expected = new BigDecimal("123.45");

    // Use reflection to set private field 'elements' with a single mocked element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockSingleElement);
    elementsField.set(jsonArray, list);

    // Mock getAsBigDecimal on the single element
    when(mockSingleElement.getAsBigDecimal()).thenReturn(expected);

    // Invoke getAsBigDecimal and verify result
    BigDecimal actual = jsonArray.getAsBigDecimal();
    assertEquals(expected, actual);

    // Verify getAsBigDecimal was called on the single element
    verify(mockSingleElement).getAsBigDecimal();
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_emptyArray_throwsIllegalStateException() throws Exception {
    // elements is empty by default, so getAsSingleElement() should throw IllegalStateException

    // We expect an IllegalStateException because getAsSingleElement checks size == 1
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_reflection_access() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Prepare elements list with mocked element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockSingleElement);
    elementsField.set(jsonArray, list);

    // Access private getAsSingleElement method via reflection
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Invoke method and verify it returns the mocked element
    Object result = getAsSingleElementMethod.invoke(jsonArray);
    assertSame(mockSingleElement, result);
  }
}