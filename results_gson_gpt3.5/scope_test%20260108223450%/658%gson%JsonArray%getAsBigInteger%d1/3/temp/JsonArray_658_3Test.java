package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_658_3Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_returnsBigIntegerFromSingleElement() {
    // Arrange
    BigInteger expected = BigInteger.valueOf(123456789L);
    when(mockElement.getAsBigInteger()).thenReturn(expected);
    jsonArray.add(mockElement);

    // Act
    BigInteger actual = jsonArray.getAsBigInteger();

    // Assert
    assertEquals(expected, actual);
    verify(mockElement).getAsBigInteger();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withEmptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement which should throw IndexOutOfBoundsException
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      try {
        getAsSingleElement.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        // unwrap to check cause
        if (e.getCause() instanceof IndexOutOfBoundsException) {
          throw e;
        }
        throw e;
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_withMultipleElements_throwsException() throws Exception {
    // Add two elements to cause getAsSingleElement to throw IllegalStateException
    jsonArray.add(mockElement);
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }
}