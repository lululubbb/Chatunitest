package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonArray_660_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_singleElement() {
    // Prepare a JsonElement mock that returns a specific long value
    JsonElement elementMock = mock(JsonElement.class);
    when(elementMock.getAsLong()).thenReturn(123456789L);

    // Use reflection to set the private 'elements' field with a single element
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(elementMock);
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    long result = jsonArray.getAsLong();
    assertEquals(123456789L, result);
    verify(elementMock, times(1)).getAsLong();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_emptyArray_throwsException() {
    // Ensure elements list is empty (default on new JsonArray)
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
    String expectedMessage = "Array must have size 1";
    assertTrue(exception.getMessage().contains(expectedMessage) || exception.getMessage().contains("empty"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_multipleElements_throwsException() {
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);

    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(firstElement);
      list.add(secondElement);
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
    String expectedMessage = "Array must have size 1";
    assertTrue(exception.getMessage().contains(expectedMessage));
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_privateMethod() throws Exception {
    // Prepare list with one element
    JsonElement element = mock(JsonElement.class);
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(element);
      elementsField.set(jsonArray, list);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
    Object returned = getAsSingleElementMethod.invoke(jsonArray);
    assertSame(element, returned);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_emptyArray_throwsException() throws Exception {
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
    Exception exception = assertThrows(Exception.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // InvocationTargetException wraps the actual cause
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException || cause.getMessage().contains("empty"));
  }
}