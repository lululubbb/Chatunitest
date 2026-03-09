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

public class JsonArray_662_2Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_singleElement() {
    jsonArray.add(mockElement);
    when(mockElement.getAsByte()).thenReturn((byte) 42);

    byte result = jsonArray.getAsByte();

    assertEquals(42, result);
    verify(mockElement).getAsByte();
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement method to test empty case
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_multipleElements_throwsException() throws Exception {
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_delegatesToSingleElement() throws Exception {
    // Add exactly one element and verify getAsByte delegates
    jsonArray.add(mockElement);
    when(mockElement.getAsByte()).thenReturn((byte) 7);

    byte result = jsonArray.getAsByte();

    assertEquals(7, result);
    verify(mockElement).getAsByte();
  }
}