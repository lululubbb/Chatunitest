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

public class JsonArray_664_1Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_withSingleElement_returnsShort() {
    // Add mock element to JsonArray
    jsonArray.add(mockElement);
    // Stub getAsShort on mock element
    when(mockElement.getAsShort()).thenReturn((short) 123);

    short result = jsonArray.getAsShort();

    assertEquals(123, result);
    verify(mockElement).getAsShort();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_withNoElements_throwsIllegalStateException() throws Exception {
    // Use reflection to invoke private getAsSingleElement method and check exception
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });

    assertNotNull(thrown);
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException, 
      "Expected cause to be IllegalStateException but was " + cause.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_withMultipleElements_throwsIllegalStateException() throws Exception {
    // Add two mock elements
    JsonElement mockElement2 = mock(JsonElement.class);
    jsonArray.add(mockElement);
    jsonArray.add(mockElement2);

    // Use reflection to invoke private getAsSingleElement method and check exception
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });

    assertNotNull(thrown);
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException,
      "Expected cause to be IllegalStateException but was " + cause.getClass().getName());
  }
}