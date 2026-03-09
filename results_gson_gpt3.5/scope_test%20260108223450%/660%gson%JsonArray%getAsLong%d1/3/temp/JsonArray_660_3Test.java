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
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_660_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = spy(new JsonArray());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_singleElement() {
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsLong()).thenReturn(123L);

    // Use reflection to set the private field 'elements' with a list containing mockElement
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, new java.util.ArrayList<>(Collections.singletonList(mockElement)));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to set private field 'elements' via reflection: " + e.getMessage());
    }

    long result = jsonArray.getAsLong();

    assertEquals(123L, result);
    verify(mockElement).getAsLong();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_noElements() throws Exception {
    // Set elements to empty list
    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, new java.util.ArrayList<>());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to set private field 'elements' via reflection: " + e.getMessage());
    }

    // getAsSingleElement() is private, let's invoke it to check behavior
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // The cause should be IndexOutOfBoundsException or IllegalStateException depending on implementation
    assertTrue(thrown.getCause() instanceof IndexOutOfBoundsException || thrown.getCause() instanceof IllegalStateException);

    // The public getAsLong() will throw the same exception
    assertThrows(RuntimeException.class, () -> jsonArray.getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_multipleElements() {
    JsonElement mockElementFirst = mock(JsonElement.class);
    JsonElement mockElementSecond = mock(JsonElement.class);
    when(mockElementFirst.getAsLong()).thenReturn(456L);

    try {
      var elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      var list = new java.util.ArrayList<JsonElement>();
      list.add(mockElementFirst);
      list.add(mockElementSecond);
      elementsField.set(jsonArray, list);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to set private field 'elements' via reflection: " + e.getMessage());
    }

    // Since getAsSingleElement() throws IllegalStateException if size != 1, expect that
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonArray.getAsLong());
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());

    verify(mockElementFirst, never()).getAsLong();
    verify(mockElementSecond, never()).getAsLong();
  }
}