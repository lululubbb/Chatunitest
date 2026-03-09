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

public class JsonArray_659_1Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withSingleElement() throws Exception {
    // Use reflection to set private field 'elements' with singleElementMock
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    when(singleElementMock.getAsFloat()).thenReturn(42.5f);

    float result = jsonArray.getAsFloat();

    assertEquals(42.5f, result);
    verify(singleElementMock).getAsFloat();
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withEmptyArray_shouldThrow() throws Exception {
    // Set empty elements list
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new java.util.ArrayList<JsonElement>());

    // Access private method getAsSingleElement via reflection to check exception
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withMultipleElements_shouldThrow() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(mock(JsonElement.class));
    list.add(mock(JsonElement.class));
    elementsField.set(jsonArray, list);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }
}