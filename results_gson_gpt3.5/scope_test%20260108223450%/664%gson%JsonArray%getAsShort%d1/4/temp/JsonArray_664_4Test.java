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

public class JsonArray_664_4Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_singleElement_returnsShort() {
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
  public void testGetAsShort_emptyArray_throwsException() throws Exception {
    // Arrange
    // Using reflection to invoke private getAsSingleElement method which throws if empty
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // unwrap the underlying exception thrown by the invoked method
        throw e.getCause();
      }
    });
    // No elements in array, so exception expected
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_multipleElements_throwsException() throws Exception {
    // Arrange
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Act & Assert
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // unwrap the underlying exception thrown by the invoked method
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_reflectionInvoke() throws Exception {
    // Arrange
    jsonArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 321);

    // Use reflection to invoke getAsShort()
    Method getAsShortMethod = JsonArray.class.getDeclaredMethod("getAsShort");
    getAsShortMethod.setAccessible(true);

    // Act
    short result = (short) getAsShortMethod.invoke(jsonArray);

    // Assert
    assertEquals(321, result);
    verify(mockElement).getAsShort();
  }
}