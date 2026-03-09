package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonArray_660_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_singleElement_returnsLong() {
    // Arrange
    JsonElement elementMock = Mockito.mock(JsonElement.class);
    Mockito.when(elementMock.getAsLong()).thenReturn(123L);

    // Use reflection to add element to private elements list or use add(JsonElement)
    jsonArray.add(elementMock);

    // Act
    long result = jsonArray.getAsLong();

    // Assert
    assertEquals(123L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_noElements_throwsException() throws Exception {
    // Arrange
    // Make sure elements list is empty
    // Use reflection to clear private elements list
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsLong();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_behavior() throws Exception {
    // Arrange
    JsonElement elementMock = Mockito.mock(JsonElement.class);
    jsonArray.add(elementMock);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act
    JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    // Assert
    assertEquals(elementMock, returnedElement);
  }
}