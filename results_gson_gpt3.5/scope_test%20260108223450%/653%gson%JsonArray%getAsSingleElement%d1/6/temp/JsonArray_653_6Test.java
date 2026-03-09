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

public class JsonArray_653_6Test {

  private JsonArray jsonArray;
  private Method getAsSingleElementMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonArray = new JsonArray();
    getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeOne_ReturnsElement() throws InvocationTargetException, IllegalAccessException {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    // Act
    JsonElement result = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    // Assert
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeZero_ThrowsIllegalStateException() {
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertTrue(thrown.getMessage().contains("Array must have size 1, but has size 0"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeMoreThanOne_ThrowsIllegalStateException() {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    jsonArray.add(element1);
    jsonArray.add(element2);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        getAsSingleElementMethod.invoke(jsonArray);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    assertTrue(thrown.getMessage().contains("Array must have size 1, but has size 2"));
  }
}