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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_654_5Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withSingleElement_returnsNumber() {
    // Arrange
    when(mockElement.getAsNumber()).thenReturn(42);
    jsonArray.add(mockElement);

    // Act
    Number result = jsonArray.getAsNumber();

    // Assert
    assertEquals(42, result);
    verify(mockElement).getAsNumber();
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withMultipleElements_throwsIllegalStateException() {
    // Arrange
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsNumber();
    });
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
    verifyNoInteractions(first, second);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_emptyArray_throwsIllegalStateException() {
    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsNumber();
    });
    assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection_singleElement() throws Exception {
    // Arrange
    jsonArray.add(mockElement);
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // Act
    JsonElement result = (JsonElement) getAsSingleElement.invoke(jsonArray);

    // Assert
    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withNullElement_throwsUnsupportedOperationException() {
    // Arrange
    jsonArray.add((JsonElement) null);

    // Act & Assert
    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonArray.getAsNumber();
    });
    assertEquals("JsonNull", thrown.getMessage());
  }
}