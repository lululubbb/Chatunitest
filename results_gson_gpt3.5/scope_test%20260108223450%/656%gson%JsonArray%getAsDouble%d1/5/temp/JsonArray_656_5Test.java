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

public class JsonArray_656_5Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withSingleElement_returnsElementDouble() {
    jsonArray.add(mockElement);
    when(mockElement.getAsDouble()).thenReturn(123.456);

    double result = jsonArray.getAsDouble();

    assertEquals(123.456, result);
    verify(mockElement).getAsDouble();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withMultipleElements_throwsException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);
    when(first.getAsDouble()).thenReturn(42.0);

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsDouble();
    });

    verify(first, never()).getAsDouble();
    verify(second, never()).getAsDouble();
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withEmptyArray_throwsException() throws Exception {
    // getAsSingleElement is private, use reflection to invoke it and verify it throws
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);

    // Also test that getAsDouble throws when no elements present
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsDouble();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_access() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Object result = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withNullElement_throwsException() {
    // Add a null element forcibly via reflection to test behavior if elements contain null
    // This tests robustness of getAsDouble when element is null
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      var elementsList = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
      elementsList.add(null);
    } catch (Exception e) {
      fail("Reflection setup failed: " + e.getMessage());
    }

    assertThrows(NullPointerException.class, () -> {
      jsonArray.getAsDouble();
    });
  }
}