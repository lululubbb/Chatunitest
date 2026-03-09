package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_647_6Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to set the private final elements field to a mock ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    mockElements = mock(ArrayList.class);
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  void remove_validIndex_removesElementAndReturnsIt() {
    JsonElement removedElement = mock(JsonElement.class);
    when(mockElements.remove(0)).thenReturn(removedElement);

    JsonElement result = jsonArray.remove(0);

    verify(mockElements).remove(0);
    assertSame(removedElement, result);
  }

  @Test
    @Timeout(8000)
  void remove_invalidIndex_throwsIndexOutOfBoundsException() {
    when(mockElements.remove(5)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(5));
    verify(mockElements).remove(5);
  }

  @Test
    @Timeout(8000)
  void remove_negativeIndex_throwsIndexOutOfBoundsException() {
    when(mockElements.remove(-1)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(-1));
    verify(mockElements).remove(-1);
  }
}