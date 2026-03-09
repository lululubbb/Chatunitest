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

public class JsonArray_655_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_singleElement() {
    JsonElement element = mock(JsonElement.class);
    when(element.getAsString()).thenReturn("mockedString");

    jsonArray.add(element);

    String result = jsonArray.getAsString();

    assertEquals("mockedString", result);
    verify(element).getAsString();
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_emptyList_shouldThrow() {
    // getAsSingleElement() likely throws if no elements, so test that getAsString propagates exception
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Object result = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_multipleElements_shouldThrow() {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    jsonArray.add(element1);
    jsonArray.add(element2);

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });

    verify(element1, never()).getAsString();
    verify(element2, never()).getAsString();
  }
}