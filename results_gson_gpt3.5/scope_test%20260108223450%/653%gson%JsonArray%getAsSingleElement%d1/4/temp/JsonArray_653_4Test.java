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

public class JsonArray_653_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_sizeOne_returnsElement() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    // Use reflection to access private field 'elements'
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    // Create a mock JsonElement
    JsonElement mockElement = mock(JsonElement.class);

    // Create ArrayList<JsonElement> with one element
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(mockElement);

    // Set the private elements field
    elementsField.set(jsonArray, elementsList);

    // Access private method getAsSingleElement
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Invoke method and assert returned value is the single element
    JsonElement result = (JsonElement) method.invoke(jsonArray);
    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_sizeZero_throwsIllegalStateException() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
    // Use reflection to access private field 'elements'
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    // Empty list
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsField.set(jsonArray, elementsList);

    // Access private method getAsSingleElement
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        method.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // Rethrow the cause
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_sizeMoreThanOne_throwsIllegalStateException() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
    // Use reflection to access private field 'elements'
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    // List with two mock elements
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(mock(JsonElement.class));
    elementsList.add(mock(JsonElement.class));
    elementsField.set(jsonArray, elementsList);

    // Access private method getAsSingleElement
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        method.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
  }
}