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

public class JsonArray_665_2Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementTrue() {
    jsonArray.add(mockElement);
    when(mockElement.getAsBoolean()).thenReturn(true);

    boolean result = jsonArray.getAsBoolean();

    assertTrue(result);
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementFalse() {
    jsonArray.add(mockElement);
    when(mockElement.getAsBoolean()).thenReturn(false);

    boolean result = jsonArray.getAsBoolean();

    assertFalse(result);
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_noElements_throwsException() throws NoSuchMethodException, IllegalAccessException {
    // Use reflection to invoke private getAsSingleElement() to confirm behavior with empty elements
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);

    // getAsBoolean calls getAsSingleElement, so calling getAsBoolean on empty should throw same exception
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_multipleElements_throwsException() throws NoSuchMethodException, IllegalAccessException {
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);

    // Also test getAsBoolean throws when multiple elements present
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBoolean());
  }
}