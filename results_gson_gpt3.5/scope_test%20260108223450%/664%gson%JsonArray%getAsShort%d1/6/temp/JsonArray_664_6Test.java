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

public class JsonArray_664_6Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_singleElement() {
    // Arrange
    jsonArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 123);

    // Act
    short result = jsonArray.getAsShort();

    // Assert
    assertEquals(123, result);
    verify(mockElement).getAsShort();
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_emptyArray_throwsIllegalStateException() throws Exception {
    // Arrange
    // No elements added, so getAsSingleElement() should fail

    // Use reflection to access private getAsSingleElement method
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act & Assert
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 0", thrown.getCause().getMessage());

    // Also verify getAsShort throws IllegalStateException if no elements
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> jsonArray.getAsShort());
    assertEquals("Array must have size 1, but has size 0", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_singleElement_returnsElement() throws Exception {
    // Arrange
    JsonElement only = mock(JsonElement.class);
    jsonArray.add(only);

    // Use reflection to access private getAsSingleElement method
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act
    JsonElement result = (JsonElement) getAsSingleElement.invoke(jsonArray);

    // Assert
    assertSame(only, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_multipleElements_throwsIllegalStateException() throws Exception {
    // Arrange
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    // Use reflection to access private getAsSingleElement method
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act & Assert
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertEquals("Array must have size 1, but has size 2", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_callsGetAsSingleElement() throws Exception {
    // Spy on JsonArray to verify getAsSingleElement is called by getAsShort
    JsonArray spyArray = spy(new JsonArray());
    spyArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 321);

    // Use reflection to get private getAsSingleElement method
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    short result = spyArray.getAsShort();

    assertEquals(321, result);

    // Verify getAsSingleElement was called by invoking it and comparing to expected
    JsonElement reflectedElement = (JsonElement) getAsSingleElement.invoke(spyArray);
    assertSame(mockElement, reflectedElement);
  }
}