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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_659_5Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withOneElement_returnsElementFloat() {
    jsonArray.add(mockElement);
    when(mockElement.getAsFloat()).thenReturn(3.14f);

    float result = jsonArray.getAsFloat();

    assertEquals(3.14f, result);
    verify(mockElement).getAsFloat();
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withMultipleElements_throwsException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);
    when(first.getAsFloat()).thenReturn(1.23f);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsFloat();
    });

    assertTrue(exception.getMessage().contains("size 1"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement to confirm it throws on empty list
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Throwable thrown = assertThrows(Throwable.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("size 1"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_privateMethod_returnsFirstElement() throws Exception {
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    JsonElement result = (JsonElement) getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, result);
  }

}