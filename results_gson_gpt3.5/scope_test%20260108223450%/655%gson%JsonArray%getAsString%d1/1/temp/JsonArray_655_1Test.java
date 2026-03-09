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

public class JsonArray_655_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_singleElement() {
    // Add one JsonElement mock that returns a specific string
    JsonElement element = mock(JsonElement.class);
    when(element.getAsString()).thenReturn("testString");
    jsonArray.add(element);

    String result = jsonArray.getAsString();

    assertEquals("testString", result);
    verify(element).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement to confirm it throws on empty
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    // Also confirm getAsString throws (indirectly) on empty array
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_multipleElements_throwsException() throws Exception {
    // Add multiple elements so getAsSingleElement throws IllegalStateException
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    jsonArray.add(element1);
    jsonArray.add(element2);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    // getAsString also throws
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_singleElement_nullReturnsNullString() {
    // Add one JsonElement mock that returns null string
    JsonElement element = mock(JsonElement.class);
    when(element.getAsString()).thenReturn(null);
    jsonArray.add(element);

    String result = jsonArray.getAsString();

    assertNull(result);
    verify(element).getAsString();
  }

}