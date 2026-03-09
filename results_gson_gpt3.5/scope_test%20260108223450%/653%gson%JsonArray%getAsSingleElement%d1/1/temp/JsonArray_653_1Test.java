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

public class JsonArray_653_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_sizeOne_returnsElement() throws Exception {
    // Use reflection to get private field 'elements'
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    JsonElement mockElement = mock(JsonElement.class);
    elements.add(mockElement);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    JsonElement result = (JsonElement) method.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_sizeZero_throwsIllegalStateException() throws Exception {
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_sizeMoreThanOne_throwsIllegalStateException() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    elements.add(mock(JsonElement.class));
    elements.add(mock(JsonElement.class));

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 2", cause.getMessage());
  }
}