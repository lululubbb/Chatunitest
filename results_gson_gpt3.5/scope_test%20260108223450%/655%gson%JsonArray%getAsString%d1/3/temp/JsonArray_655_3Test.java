package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_655_3Test {

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
  public void testGetAsString_emptyArray_expectException() throws Exception {
    // getAsSingleElement is private, test getAsString when no element exists
    // According to source, no elements means getAsSingleElement throws IllegalStateException
    // We invoke getAsString and expect it to propagate the exception.

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });

    String msg = exception.getMessage();
    assertNotNull(msg);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Add one element and invoke private getAsSingleElement via reflection
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    Object result = method.invoke(jsonArray);

    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_empty_expectException() throws NoSuchMethodException {
    Method method = null;
    try {
      method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      method.setAccessible(true);
      method.invoke(jsonArray);
      fail("Expected InvocationTargetException wrapping IllegalStateException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalStateException);
    } catch (IllegalAccessException e) {
      fail("IllegalAccessException not expected");
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_multipleElements_expectException() {
    // Add two elements and call getAsString should throw IllegalStateException
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    jsonArray.add(element1);
    jsonArray.add(element2);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });

    assertNotNull(thrown.getMessage());
  }
}