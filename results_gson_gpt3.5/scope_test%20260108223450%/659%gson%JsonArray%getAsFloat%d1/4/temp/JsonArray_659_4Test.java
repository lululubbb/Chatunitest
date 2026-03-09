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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonArray_659_4Test {

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
  public void testGetAsFloat_withNoElements_throwsIllegalStateException() throws Exception {
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Using reflection to invoke private getAsSingleElement to confirm it throws when empty
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());

    // Since getAsFloat calls getAsSingleElement, this also throws IllegalStateException
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsFloat();
    });
    assertEquals("Array must have size 1, but has size 0", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_withMultipleElements_throwsIllegalStateException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);

    jsonArray.add(first);
    jsonArray.add(second);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsFloat();
    });
    assertEquals("Array must have size 1, but has size 2", ex.getMessage());
  }
}