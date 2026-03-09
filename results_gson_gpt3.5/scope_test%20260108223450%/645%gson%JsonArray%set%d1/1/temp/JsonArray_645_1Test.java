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

class JsonArray_645_1Test {

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
  void set_ShouldDelegateToElementsSet_WhenElementNotNull() {
    JsonElement oldElement = mock(JsonElement.class);
    JsonElement newElement = mock(JsonElement.class);
    int index = 2;

    when(mockElements.set(index, newElement)).thenReturn(oldElement);

    JsonElement result = jsonArray.set(index, newElement);

    assertSame(oldElement, result);
    verify(mockElements).set(index, newElement);
  }

  @Test
    @Timeout(8000)
  void set_ShouldDelegateToElementsSetWithJsonNullInstance_WhenElementIsNull() {
    JsonElement oldElement = mock(JsonElement.class);
    int index = 1;

    when(mockElements.set(eq(index), eq(JsonNull.INSTANCE))).thenReturn(oldElement);

    JsonElement result = jsonArray.set(index, null);

    assertSame(oldElement, result);
    verify(mockElements).set(index, JsonNull.INSTANCE);
  }
}