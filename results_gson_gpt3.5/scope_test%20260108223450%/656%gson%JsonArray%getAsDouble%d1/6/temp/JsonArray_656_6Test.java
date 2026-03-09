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

public class JsonArray_656_6Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withSingleElement_returnsDouble() {
    // Use reflection to set elements list with one mocked JsonElement
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(mockSingleElement);
      elementsField.set(jsonArray, elements);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    when(mockSingleElement.getAsDouble()).thenReturn(42.42);

    double result = jsonArray.getAsDouble();

    assertEquals(42.42, result, 0.000001);
    verify(mockSingleElement).getAsDouble();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withEmptyElements_throwsIllegalStateException() {
    // elements list is empty by default, but ensure via reflection
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, new ArrayList<JsonElement>());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    // getAsDouble calls getAsSingleElement which throws IllegalStateException on empty list
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsDouble());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withMultipleElements_throwsIllegalStateException() {
    // Because getAsSingleElement expects exactly one element, multiple elements cause IllegalStateException
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);

      JsonElement firstElement = mock(JsonElement.class);
      JsonElement secondElement = mock(JsonElement.class);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(firstElement);
      elements.add(secondElement);
      elementsField.set(jsonArray, elements);

      assertThrows(IllegalStateException.class, () -> jsonArray.getAsDouble());

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_privateMethod_returnsCorrectElement() throws Exception {
    // Set elements list with one element and invoke private getAsSingleElement()
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> elements = new ArrayList<>();
      elements.add(mockSingleElement);
      elementsField.set(jsonArray, elements);

      Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElementMethod.setAccessible(true);

      JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

      assertSame(mockSingleElement, returnedElement);

    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      fail("Reflection invocation failed: " + e.getMessage());
    }
  }
}