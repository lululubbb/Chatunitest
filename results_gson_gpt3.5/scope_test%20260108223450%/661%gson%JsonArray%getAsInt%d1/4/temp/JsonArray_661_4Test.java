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

public class JsonArray_661_4Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_returnsElementInt() {
    jsonArray.add(mockElement);
    when(mockElement.getAsInt()).thenReturn(42);

    int result = jsonArray.getAsInt();

    assertEquals(42, result);
    verify(mockElement).getAsInt();
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_emptyArray_throwsIndexOutOfBoundsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement() to confirm behavior
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // When invoking a method via reflection that throws an exception,
    // the actual exception is wrapped in InvocationTargetException.
    // So we need to unwrap it and check the cause.
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());

    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsInt();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_withMultipleElements_throwsIllegalStateException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    // According to JsonArray#getAsSingleElement, if size != 1, it throws IllegalStateException
    // So getAsInt() will throw IllegalStateException when there are multiple elements
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsInt();
    });
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
  }
}