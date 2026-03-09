package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_653_3Test {

  private JsonArray jsonArray;
  private Method getAsSingleElementMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    jsonArray = new JsonArray();
    getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_SizeOne_ReturnsElement() throws InvocationTargetException, IllegalAccessException {
    JsonElement element = mock(JsonElement.class);
    // Use reflection to add element to private field elements
    ArrayList<JsonElement> elements = getElementsList(jsonArray);
    elements.add(element);

    Object result = getAsSingleElementMethod.invoke(jsonArray);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_SizeZero_ThrowsIllegalStateException() {
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_SizeMoreThanOne_ThrowsIllegalStateException() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    ArrayList<JsonElement> elements = getElementsList(jsonArray);
    elements.add(element1);
    elements.add(element2);

    Throwable cause = null;
    try {
      getAsSingleElementMethod.invoke(jsonArray);
    } catch (InvocationTargetException e) {
      cause = e.getCause();
    }
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 2", cause.getMessage());
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsList(JsonArray jsonArray) {
    try {
      var field = JsonArray.class.getDeclaredField("elements");
      field.setAccessible(true);
      return (ArrayList<JsonElement>) field.get(jsonArray);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}