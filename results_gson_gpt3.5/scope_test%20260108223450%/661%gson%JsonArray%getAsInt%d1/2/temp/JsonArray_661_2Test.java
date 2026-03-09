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

public class JsonArray_661_2Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_withSingleElement_returnsElementInt() {
    // Arrange
    jsonArray.add(mockElement);
    when(mockElement.getAsInt()).thenReturn(42);

    // Act
    int result = jsonArray.getAsInt();

    // Assert
    assertEquals(42, result);
    verify(mockElement).getAsInt();
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_withEmptyArray_throwsException() throws Exception {
    // Arrange
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_access() throws Exception {
    // Arrange
    jsonArray.add(mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act
    JsonElement element = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    // Assert
    assertSame(mockElement, element);
  }
}