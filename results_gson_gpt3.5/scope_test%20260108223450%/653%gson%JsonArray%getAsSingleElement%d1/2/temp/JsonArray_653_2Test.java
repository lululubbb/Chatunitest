package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_653_2Test {

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
  public void testGetAsSingleElement_SizeOne_ReturnsElement() throws Throwable {
    // Arrange
    JsonElement element = mock(JsonElement.class);
    // Use reflection to add element to private elements list
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) getPrivateField(jsonArray, "elements");
    elements.add(element);

    // Act
    Object result = invokeGetAsSingleElement();

    // Assert
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeZero_ThrowsIllegalStateException() {
    // elements list is empty by default

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      invokeGetAsSingleElement();
    });
    assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_SizeMoreThanOne_ThrowsIllegalStateException() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) getPrivateField(jsonArray, "elements");
    elements.add(element1);
    elements.add(element2);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      invokeGetAsSingleElement();
    });
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());
  }

  private Object invokeGetAsSingleElement() throws Throwable {
    try {
      return getAsSingleElementMethod.invoke(jsonArray);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private Object getPrivateField(Object obj, String fieldName) {
    try {
      var field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}