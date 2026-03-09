package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_658_1Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withSingleElement() throws Exception {
    // Add a mock element to the elements list via reflection
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();
    elements.add(mockElement);

    BigInteger expected = BigInteger.valueOf(12345L);
    when(mockElement.getAsBigInteger()).thenReturn(expected);

    BigInteger actual = jsonArray.getAsBigInteger();

    assertEquals(expected, actual);
    verify(mockElement).getAsBigInteger();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_emptyArray_throwsException() throws Exception {
    // Ensure elements list is empty
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsBigInteger();
    });
    // The exception comes from getAsSingleElement which throws if empty
    String expectedMessage = "one element";
    assertTrue(exception.getMessage().toLowerCase().contains(expectedMessage));
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws Exception {
    // Use reflection to test private getAsSingleElement method

    // Prepare elements list with exactly one element
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();
    elements.add(mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, returnedElement);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_multipleElements_throwsException() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (java.util.ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.clear();
    elements.add(mockElement);
    elements.add(mock(JsonElement.class));

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // The invocation target exception wraps the actual exception
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    String expectedMessage = "one element";
    assertTrue(cause.getMessage().toLowerCase().contains(expectedMessage));
  }
}