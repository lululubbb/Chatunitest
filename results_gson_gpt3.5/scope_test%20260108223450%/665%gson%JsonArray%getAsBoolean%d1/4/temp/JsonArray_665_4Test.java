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

public class JsonArray_665_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementTrue() {
    JsonElement elementMock = mock(JsonElement.class);
    when(elementMock.getAsBoolean()).thenReturn(true);

    jsonArray.add(elementMock);

    boolean result = jsonArray.getAsBoolean();

    assertTrue(result);
    verify(elementMock).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_singleElementFalse() {
    JsonElement elementMock = mock(JsonElement.class);
    when(elementMock.getAsBoolean()).thenReturn(false);

    jsonArray.add(elementMock);

    boolean result = jsonArray.getAsBoolean();

    assertFalse(result);
    verify(elementMock).getAsBoolean();
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_emptyArray_throwsException() throws Exception {
    // getAsSingleElement is private, use reflection to invoke directly to confirm exception
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    IllegalStateException thrown1 = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 0", thrown1.getMessage());

    // Also test getAsBoolean throws exception when empty
    IllegalStateException thrown2 = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsBoolean();
    });
    assertEquals("Array must have size 1, but has size 0", thrown2.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_multipleElements_throwsException() throws Exception {
    JsonElement elementMock1 = mock(JsonElement.class);
    JsonElement elementMock2 = mock(JsonElement.class);

    jsonArray.add(elementMock1);
    jsonArray.add(elementMock2);

    // getAsSingleElement should throw IllegalStateException if multiple elements
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    IllegalStateException thrown1 = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("Array must have size 1, but has size 2", thrown1.getMessage());

    // getAsBoolean should also throw IllegalStateException
    IllegalStateException thrown2 = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsBoolean();
    });
    assertEquals("Array must have size 1, but has size 2", thrown2.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_returnsCorrectElement() throws Exception {
    JsonElement elementMock = mock(JsonElement.class);
    jsonArray.add(elementMock);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    Object result = getAsSingleElementMethod.invoke(jsonArray);

    assertSame(elementMock, result);
  }
}