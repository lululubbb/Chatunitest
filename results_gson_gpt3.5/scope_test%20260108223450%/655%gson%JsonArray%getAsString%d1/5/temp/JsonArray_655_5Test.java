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

public class JsonArray_655_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void getAsString_singleElement_returnsElementString() {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsString()).thenReturn("mockString");

    jsonArray.add(mockElement);

    // Act
    String result = jsonArray.getAsString();

    // Assert
    assertEquals("mockString", result);
    verify(mockElement).getAsString();
  }

  @Test
    @Timeout(8000)
  public void getAsString_emptyArray_throwsIllegalStateException() throws Exception {
    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void getAsSingleElement_reflection_behavior() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Arrange
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsString()).thenReturn("reflectString");
    jsonArray.add(mockElement);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act
    JsonElement result = (JsonElement) method.invoke(jsonArray);

    // Assert
    assertNotNull(result);
    assertEquals(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void getAsString_multipleElements_throwsIllegalStateException() {
    // Arrange
    JsonElement firstMock = mock(JsonElement.class);
    JsonElement secondMock = mock(JsonElement.class);

    jsonArray.add(firstMock);
    jsonArray.add(secondMock);

    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsString();
    });
    assertNotNull(thrown);
  }
}