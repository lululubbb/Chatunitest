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

class JsonArray_662_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void getAsByte_singleElement_returnsByte() {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsByte()).thenReturn((byte) 123);

    // Use reflection to add element to private field elements
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(mockElement);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }

    // Act
    byte result = jsonArray.getAsByte();

    // Assert
    assertEquals(123, result);
    verify(mockElement, times(1)).getAsByte();
  }

  @Test
    @Timeout(8000)
  void getAsByte_noElements_throwsIndexOutOfBoundsException() throws Exception {
    // Arrange
    // elements list is empty by default

    // Use reflection to invoke private getAsSingleElement method which is called by getAsByte()
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // InvocationTargetException wraps the actual exception, check cause
    assertTrue(exception.getCause() instanceof IndexOutOfBoundsException || exception.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void getAsByte_multipleElements_throwsIllegalStateException() throws Exception {
    // Arrange
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);

    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
      elements.add(mockElement1);
      elements.add(mockElement2);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }

    // Use reflection to invoke private getAsSingleElement method which is called by getAsByte()
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // InvocationTargetException wraps the actual exception, check cause
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }
}