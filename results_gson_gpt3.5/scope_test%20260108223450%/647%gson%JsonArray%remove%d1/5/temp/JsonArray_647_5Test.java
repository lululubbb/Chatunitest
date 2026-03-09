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

public class JsonArray_647_5Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  public void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to replace the private final elements field with a mock
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    mockElements = mock(ArrayList.class);
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  public void testRemove_validIndex_returnsRemovedElement() {
    JsonElement removedElement = mock(JsonElement.class);
    when(mockElements.remove(0)).thenReturn(removedElement);

    JsonElement result = jsonArray.remove(0);

    verify(mockElements).remove(0);
    assertSame(removedElement, result);
  }

  @Test
    @Timeout(8000)
  public void testRemove_indexOutOfBounds_throwsException() {
    when(mockElements.remove(10)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(10));

    verify(mockElements).remove(10);
  }
}