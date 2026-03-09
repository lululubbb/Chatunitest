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

class JsonArray_661_6Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsInt_WithSingleElement_ReturnsElementInt() {
    jsonArray.add(mockElement);
    when(mockElement.getAsInt()).thenReturn(42);

    int result = jsonArray.getAsInt();

    assertEquals(42, result);
    verify(mockElement).getAsInt();
  }

  @Test
    @Timeout(8000)
  void getAsInt_WithEmptyArray_ThrowsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement()
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void getAsInt_WithMultipleElements_ThrowsException() throws Exception {
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_PrivateMethod_ReturnsElement() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    JsonElement element = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, element);
  }
}