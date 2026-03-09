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

class JsonArray_645_4Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to set private final field 'elements' to a mock
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    mockElements = mock(ArrayList.class);
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  void set_WithNonNullElement_ShouldCallSetOnElements() {
    JsonElement element = mock(JsonElement.class);
    JsonElement returned = mock(JsonElement.class);

    when(mockElements.set(0, element)).thenReturn(returned);

    JsonElement result = jsonArray.set(0, element);

    assertSame(returned, result);
    verify(mockElements).set(0, element);
  }

  @Test
    @Timeout(8000)
  void set_WithNullElement_ShouldReplaceWithJsonNullInstance() {
    JsonElement returned = mock(JsonElement.class);

    when(mockElements.set(eq(1), eq(JsonNull.INSTANCE))).thenReturn(returned);

    JsonElement result = jsonArray.set(1, null);

    assertSame(returned, result);
    verify(mockElements).set(1, JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  void set_WithIndexOutOfBounds_ShouldThrowException() {
    JsonElement element = mock(JsonElement.class);

    doThrow(new IndexOutOfBoundsException()).when(mockElements).set(5, element);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.set(5, element));
  }
}