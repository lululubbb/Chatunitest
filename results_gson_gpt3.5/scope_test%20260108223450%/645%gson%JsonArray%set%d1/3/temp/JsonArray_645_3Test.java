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

class JsonArray_645_3Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to replace the private final elements list with a mock
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    mockElements = mock(ArrayList.class);
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  void set_shouldDelegateToElementsSet_whenElementIsNotNull() {
    JsonElement element = mock(JsonElement.class);
    JsonElement oldElement = mock(JsonElement.class);
    when(mockElements.set(0, element)).thenReturn(oldElement);

    JsonElement result = jsonArray.set(0, element);

    verify(mockElements).set(0, element);
    assertSame(oldElement, result);
  }

  @Test
    @Timeout(8000)
  void set_shouldDelegateToElementsSetWithJsonNullInstance_whenElementIsNull() {
    JsonElement oldElement = mock(JsonElement.class);
    when(mockElements.set(1, JsonNull.INSTANCE)).thenReturn(oldElement);

    JsonElement result = jsonArray.set(1, null);

    verify(mockElements).set(1, JsonNull.INSTANCE);
    assertSame(oldElement, result);
  }

  @Test
    @Timeout(8000)
  void set_shouldThrowIndexOutOfBoundsException_whenIndexIsOutOfBounds() {
    doThrow(new IndexOutOfBoundsException()).when(mockElements).set(5, JsonNull.INSTANCE);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(5, null));
  }
}