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

class JsonArray_654_3Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsNumber_emptyArray_throwsIllegalStateException() throws Exception {
    // Use reflection to access private getAsSingleElement method to confirm exception on empty
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> getAsSingleElement.invoke(jsonArray));
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());

    IllegalStateException thrown2 = assertThrows(IllegalStateException.class, () -> jsonArray.getAsNumber());
    assertEquals("Array must have size 1, but has size 0", thrown2.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsNumber_singleElement_returnsNumber() {
    jsonArray.add(mockElement);
    Number expected = 42;
    when(mockElement.getAsNumber()).thenReturn(expected);

    Number actual = jsonArray.getAsNumber();

    assertSame(expected, actual);
    verify(mockElement).getAsNumber();
  }

  @Test
    @Timeout(8000)
  void getAsNumber_multipleElements_throwsIllegalStateException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonArray.getAsNumber());
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_privateMethod_returnsFirstElement() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    JsonElement result = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_privateMethod_throwsExceptionWhenEmpty() throws Exception {
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> getAsSingleElement.invoke(jsonArray));
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());
  }
}