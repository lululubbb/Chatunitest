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

public class JsonArray_663_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_singleElementDelegates() {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsCharacter()).thenReturn('x');

    // Use reflection to add mockElement to private elements list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.List<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mockElement);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    // Act
    char result = jsonArray.getAsCharacter();

    // Assert
    assertEquals('x', result);
    verify(mockElement, times(1)).getAsCharacter();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyArray_throwsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsCharacter();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_multipleElements_throwsIllegalStateException() {
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.List<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mock(JsonElement.class));
      elementsList.add(mock(JsonElement.class));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsCharacter();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_privateMethod_behavior() {
    // Setup single element
    JsonElement mockElement = mock(JsonElement.class);

    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.List<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mockElement);

      Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElement.setAccessible(true);

      JsonElement returned = (JsonElement) getAsSingleElement.invoke(jsonArray);
      assertSame(mockElement, returned);
    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      fail("Reflection invocation failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_empty_throws() {
    try {
      Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElement.setAccessible(true);
      assertThrows(InvocationTargetException.class, () -> {
        try {
          getAsSingleElement.invoke(jsonArray);
        } catch (InvocationTargetException e) {
          if (e.getCause() instanceof IllegalStateException) {
            throw e;
          }
          fail("Unexpected exception cause: " + e.getCause());
        }
      });
    } catch (NoSuchMethodException e) {
      fail("Reflection method not found: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_multipleElements_throws() {
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.List<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(mock(JsonElement.class));
      elementsList.add(mock(JsonElement.class));

      Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElement.setAccessible(true);

      assertThrows(InvocationTargetException.class, () -> {
        try {
          getAsSingleElement.invoke(jsonArray);
        } catch (InvocationTargetException e) {
          if (e.getCause() instanceof IllegalStateException) {
            throw e;
          }
          fail("Unexpected exception cause: " + e.getCause());
        }
      });
    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
      fail("Reflection setup failed: " + e.getMessage());
    }
  }
}