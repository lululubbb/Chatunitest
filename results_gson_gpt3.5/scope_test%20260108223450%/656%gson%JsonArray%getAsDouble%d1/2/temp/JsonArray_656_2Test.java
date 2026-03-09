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

public class JsonArray_656_2Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_singleElement_returnsDouble() {
    // Use reflection to set private elements list with one mocked element
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(mockSingleElement);
      elementsField.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    when(mockSingleElement.getAsDouble()).thenReturn(123.456);

    double result = jsonArray.getAsDouble();

    assertEquals(123.456, result, 0.000001);
    verify(mockSingleElement).getAsDouble();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_emptyArray_throwsException() {
    // Ensure elements list is empty
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      elementsField.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    // getAsSingleElement() will throw IllegalStateException because no elements
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsDouble();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_access() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Prepare elements list with one mock element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockSingleElement);
    elementsField.set(jsonArray, list);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Object result = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockSingleElement, result);
  }
}