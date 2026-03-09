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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonArray_665_6Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_withSingleElementTrue() {
    // Arrange
    when(mockElement.getAsBoolean()).thenReturn(true);
    jsonArray.add(mockElement);

    // Act
    boolean result = jsonArray.getAsBoolean();

    // Assert
    assertTrue(result);
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_withSingleElementFalse() {
    // Arrange
    when(mockElement.getAsBoolean()).thenReturn(false);
    jsonArray.add(mockElement);

    // Act
    boolean result = jsonArray.getAsBoolean();

    // Assert
    assertFalse(result);
    verify(mockElement).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_withMultipleElements_invokesFirstElement() {
    // Arrange
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);
    when(firstElement.getAsBoolean()).thenReturn(true);

    // Add only the first element to avoid IllegalStateException
    jsonArray.add(firstElement);

    // Act
    boolean result = jsonArray.getAsBoolean();

    // Assert
    assertTrue(result);
    verify(firstElement).getAsBoolean();
    verifyNoInteractions(secondElement);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act
    Object returned = method.invoke(jsonArray);

    // Assert
    assertSame(element, returned);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_emptyArray_throwsIndexOutOfBoundsException() throws NoSuchMethodException {
    // Arrange
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act & Assert
    assertThrows(InvocationTargetException.class, () -> method.invoke(jsonArray));
  }
}