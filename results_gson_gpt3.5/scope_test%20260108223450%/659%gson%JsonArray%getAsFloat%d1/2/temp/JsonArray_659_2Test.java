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

class JsonArray_659_2Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsFloat_singleElement_returnsMockedValue() {
    jsonArray.add(mockElement);
    when(mockElement.getAsFloat()).thenReturn(3.14f);

    float result = jsonArray.getAsFloat();

    assertEquals(3.14f, result);
    verify(mockElement).getAsFloat();
  }

  @Test
    @Timeout(8000)
  void getAsFloat_emptyArray_throwsIllegalStateException() throws Throwable {
    // Use reflection to invoke private getAsSingleElement to confirm exception
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertNotNull(exception);
    assertTrue(exception.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", exception.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_returnsFirstElement() throws Throwable {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    JsonElement element = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, element);
  }

  @Test
    @Timeout(8000)
  void getAsFloat_multipleElements_throwsIllegalStateException() {
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);
    jsonArray.add(firstElement);
    jsonArray.add(secondElement);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsFloat();
    });

    assertEquals("Array must have size 1, but has size 2", exception.getMessage());
    verifyNoInteractions(firstElement);
    verifyNoInteractions(secondElement);
  }
}