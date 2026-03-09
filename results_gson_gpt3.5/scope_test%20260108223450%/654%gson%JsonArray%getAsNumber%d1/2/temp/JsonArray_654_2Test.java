package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_654_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_singleElement_returnsNumber() {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsNumber()).thenReturn(42);

    // Use reflection to set private 'elements' field with a single mockElement
    try {
      var field = JsonArray.class.getDeclaredField("elements");
      field.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(mockElement);
      field.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }

    // Act
    Number result = jsonArray.getAsNumber();

    // Assert
    assertEquals(42, result);
    verify(mockElement).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_emptyArray_throwsIllegalStateException() {
    // Arrange
    // Ensure elements list is empty (default constructor already does this)
    // Act & Assert
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsNumber();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_accessAndReturnsElement() throws Exception {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    var field = JsonArray.class.getDeclaredField("elements");
    field.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mockElement);
    field.set(jsonArray, list);

    // Act
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement returned = (JsonElement) method.invoke(jsonArray);

    // Assert
    assertSame(mockElement, returned);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_singleElement_usesFirstElement() {
    // Arrange
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    when(mockElement1.getAsNumber()).thenReturn(123);
    // Add only one element to the list to avoid IllegalStateException
    try {
      var field = JsonArray.class.getDeclaredField("elements");
      field.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(mockElement1);
      // intentionally not adding mockElement2 to avoid size != 1
      field.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }

    // Act
    Number result = jsonArray.getAsNumber();

    // Assert
    assertEquals(123, result);
    verify(mockElement1).getAsNumber();
    verifyNoInteractions(mockElement2);
  }
}