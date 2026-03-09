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

class JsonArray_660_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void getAsLong_singleElement_returnsElementLong() {
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsLong()).thenReturn(123456789L);

    jsonArray.add(mockElement);

    long result = jsonArray.getAsLong();

    assertEquals(123456789L, result);
    verify(mockElement).getAsLong();
  }

  @Test
    @Timeout(8000)
  void getAsLong_emptyArray_throwsIndexOutOfBoundsException() throws Exception {
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Ensure elements list is empty
    // Call getAsSingleElement directly to confirm exception
    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());

    // getAsLong should also throw since getAsSingleElement throws
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
    assertEquals("Array must have size 1, but has size 0", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsLong_multipleElements_throwsIllegalStateException() throws Exception {
    // Add two elements to cause getAsSingleElement to throw IllegalStateException
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 2", thrown.getCause().getMessage());

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
    assertEquals("Array must have size 1, but has size 2", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_accessibleAndReturnsElement() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive(42L);
    jsonArray.add(primitive);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Object result = getAsSingleElement.invoke(jsonArray);

    assertTrue(result instanceof JsonElement);
    assertEquals(primitive, result);
  }
}