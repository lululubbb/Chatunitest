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

class JsonArray_664_5Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withSingleElement_returnsElementShort() {
    jsonArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 123);

    short result = jsonArray.getAsShort();

    assertEquals((short) 123, result);
    verify(mockElement).getAsShort();
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withNoElements_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement to confirm exception
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    // The getAsShort calls getAsSingleElement, so calling getAsShort without elements throws too
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withMultipleElements_throwsException() throws Exception {
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertThrows(IllegalStateException.class, () -> jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_returnsOnlyElement() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Object element = getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, element);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_callsGetAsShortOnSingleElement() {
    JsonElement element = spy(new JsonPrimitive((short) 42));
    jsonArray.add(element);

    short result = jsonArray.getAsShort();

    assertEquals(42, result);
    verify(element).getAsShort();
  }
}