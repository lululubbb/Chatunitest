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

class JsonArray_661_1Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsInt_singleElement_returnsInt() {
    jsonArray.add(mockElement);
    when(mockElement.getAsInt()).thenReturn(42);

    int result = jsonArray.getAsInt();

    assertEquals(42, result);
    verify(mockElement).getAsInt();
  }

  @Test
    @Timeout(8000)
  void getAsInt_emptyArray_throwsIllegalStateException() throws Exception {
    // Use reflection to access private getAsSingleElement()
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsInt_multipleElements_throwsIllegalStateException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsInt();
    });

    assertEquals("Array must have size 1, but has size 2", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflectiveInvocation_returnsCorrectElement() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    JsonElement returned = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, returned);
  }
}