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

public class JsonArray_658_4Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_singleElement() throws Exception {
    // Add one element to the JsonArray
    jsonArray.add(mockElement);

    // Stub getAsBigInteger on the mock element
    BigInteger expected = new BigInteger("12345678901234567890");
    when(mockElement.getAsBigInteger()).thenReturn(expected);

    // Invoke getAsBigInteger on JsonArray
    BigInteger actual = jsonArray.getAsBigInteger();

    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_reflectiveInvocation() throws Exception {
    // Add one element to the JsonArray
    jsonArray.add(mockElement);

    // Stub getAsBigInteger on the mock element
    BigInteger expected = new BigInteger("98765432109876543210");
    when(mockElement.getAsBigInteger()).thenReturn(expected);

    // Access private method getAsSingleElement via reflection
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
    JsonElement singleElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(mockElement, singleElement);

    // Verify getAsBigInteger called on the single element
    BigInteger actual = singleElement.getAsBigInteger();
    assertEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_emptyArray_throwsException() throws Exception {
    // JsonArray is empty, getAsSingleElement should throw IllegalStateException
    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      // Use reflection to invoke private getAsSingleElement() which is called by getAsBigInteger()
      Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElementMethod.setAccessible(true);
      getAsSingleElementMethod.invoke(jsonArray);
    });

    // The cause of InvocationTargetException should be IllegalStateException
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    String expectedMessage = "Array must have size 1, but has size 0";
    assertTrue(cause.getMessage().contains(expectedMessage));
  }
}