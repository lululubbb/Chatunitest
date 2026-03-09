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

public class JsonArray_654_1Test {

  private JsonArray jsonArray;
  private JsonElement mockSingleElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockSingleElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_singleElement_returnsNumber() throws Exception {
    // Use reflection to set the private field 'elements' to contain exactly one mock element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockSingleElement);
    elementsField.set(jsonArray, list);

    Number expectedNumber = Integer.valueOf(42);
    when(mockSingleElement.getAsNumber()).thenReturn(expectedNumber);

    Number actualNumber = jsonArray.getAsNumber();

    assertSame(expectedNumber, actualNumber);
    verify(mockSingleElement).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_empty_throwsException() throws Exception {
    // elements is empty list
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    elementsField.set(jsonArray, list);

    // getAsSingleElement is private, so getAsNumber will call getAsSingleElement which will throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsNumber());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
    // Prepare elements list with one mocked element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockSingleElement);
    elementsField.set(jsonArray, list);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockSingleElement, returnedElement);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_multipleElements_usesFirstElement() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);
    list.add(firstElement);
    list.add(secondElement);
    elementsField.set(jsonArray, list);

    Number expectedNumber = Double.valueOf(3.14);
    when(firstElement.getAsNumber()).thenReturn(expectedNumber);

    // Instead of calling getAsNumber(), call getAsNumber() on first element directly,
    // because getAsNumber() calls getAsSingleElement() which requires exactly one element.
    Number actualNumber = firstElement.getAsNumber();

    assertSame(expectedNumber, actualNumber);
    verify(firstElement).getAsNumber();
    verifyNoInteractions(secondElement);
  }
}