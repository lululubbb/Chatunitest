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
import org.mockito.Mockito;

public class JsonArray_665_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementTrue() {
    JsonElement element = mock(JsonElement.class);
    when(element.getAsBoolean()).thenReturn(true);

    // Use reflection to invoke private method getAsSingleElement and mock it by adding element
    addElementToJsonArray(jsonArray, element);

    boolean result = jsonArray.getAsBoolean();

    assertTrue(result);
    verify(element).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementFalse() {
    JsonElement element = mock(JsonElement.class);
    when(element.getAsBoolean()).thenReturn(false);

    addElementToJsonArray(jsonArray, element);

    boolean result = jsonArray.getAsBoolean();

    assertFalse(result);
    verify(element).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_noElements_throwsException() {
    // JsonArray is empty, getAsSingleElement() should throw IllegalStateException
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsBoolean();
    });
    assertNotNull(exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_multipleElements_throwsException() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    addElementToJsonArray(jsonArray, element1);
    addElementToJsonArray(jsonArray, element2);

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsBoolean();
    });
    assertNotNull(exception.getMessage());
  }

  /**
   * Helper method to add element to JsonArray's private elements list using reflection.
   */
  private void addElementToJsonArray(JsonArray jsonArray, JsonElement element) {
    try {
      Method addMethod = JsonArray.class.getDeclaredMethod("add", JsonElement.class);
      addMethod.setAccessible(true);
      addMethod.invoke(jsonArray, element);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Failed to add element to JsonArray via reflection: " + e.getMessage());
    }
  }
}