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

public class JsonArray_662_3Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_singleElement_returnsByte() {
    jsonArray.add(mockElement);
    when(mockElement.getAsByte()).thenReturn((byte) 123);

    byte result = jsonArray.getAsByte();

    assertEquals((byte) 123, result);
    verify(mockElement).getAsByte();
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement to confirm it throws when empty
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // unwrap the cause thrown by the invoked method
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_multipleElements_throwsException() throws Exception {
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // unwrap the cause thrown by the invoked method
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
  }
}