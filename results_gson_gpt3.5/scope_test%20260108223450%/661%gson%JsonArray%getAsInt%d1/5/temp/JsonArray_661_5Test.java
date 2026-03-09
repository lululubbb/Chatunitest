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

class JsonArray_661_5Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_singleElement_returnsInt() {
    // Add a mock element to the JsonArray
    jsonArray.add(mockElement);

    // Stub getAsInt on the mock element
    when(mockElement.getAsInt()).thenReturn(42);

    // Call getAsInt on JsonArray
    int result = jsonArray.getAsInt();

    // Verify the returned int is as expected
    assertEquals(42, result);

    // Verify getAsInt was called on the single element
    verify(mockElement).getAsInt();
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement method
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Since the array is empty, getAsSingleElement should throw IllegalStateException
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // Unwrap InvocationTargetException to check cause
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_multipleElements_throwsException() throws Exception {
    // Add two mock elements to the array
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    // Use reflection to invoke private getAsSingleElement method
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Since the array has more than one element, getAsSingleElement should throw IllegalStateException
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // Unwrap InvocationTargetException to check cause
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
  }
}