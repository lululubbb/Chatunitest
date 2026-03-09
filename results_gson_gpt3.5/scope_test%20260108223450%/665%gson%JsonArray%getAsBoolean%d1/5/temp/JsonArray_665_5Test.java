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

class JsonArray_665_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_withSingleElementTrue() {
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsBoolean()).thenReturn(true);

    jsonArray.add(mockElement);

    assertTrue(jsonArray.getAsBoolean());
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_withSingleElementFalse() {
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsBoolean()).thenReturn(false);

    jsonArray.add(mockElement);

    assertFalse(jsonArray.getAsBoolean());
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_withEmptyArray_throwsException() throws Exception {
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_withMultipleElements_throwsException() throws Exception {
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);

    jsonArray.add(mockElement1);
    jsonArray.add(mockElement2);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    // The cause should be IllegalStateException
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_callsGetAsSingleElement() throws Exception {
    JsonArray spyArray = spy(jsonArray);
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsBoolean()).thenReturn(true);

    spyArray.add(mockElement);

    // Use reflection to stub the private getAsSingleElement method
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Since mocking private methods is not supported, we rely on actual behavior.
    boolean result = spyArray.getAsBoolean();

    assertTrue(result);

    verify(mockElement).getAsBoolean();
  }
}