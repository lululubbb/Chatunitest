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

public class JsonArray_654_6Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withSingleElement_returnsNumber() {
    // Add mockElement to jsonArray's elements list via reflection
    addElementToJsonArray(jsonArray, mockElement);

    Number expectedNumber = 42;
    when(mockElement.getAsNumber()).thenReturn(expectedNumber);

    Number actualNumber = jsonArray.getAsNumber();

    assertEquals(expectedNumber, actualNumber);
    verify(mockElement).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withEmptyArray_throwsIllegalStateException() {
    // jsonArray is empty, so getAsSingleElement() should throw IllegalStateException
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsNumber();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflectiveInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Add mockElement to jsonArray's elements list via reflection
    addElementToJsonArray(jsonArray, mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, returnedElement);
  }

  private void addElementToJsonArray(JsonArray array, JsonElement element) {
    try {
      Method addMethod = JsonArray.class.getMethod("add", JsonElement.class);
      addMethod.invoke(array, element);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}