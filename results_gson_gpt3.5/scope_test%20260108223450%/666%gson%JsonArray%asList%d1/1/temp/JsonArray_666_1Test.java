package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.NonNullElementWrapperList;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_666_1Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Use reflection to set the private final elements field with a mock ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    mockElements = spy(new ArrayList<>());
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  void asList_returnsNonNullElementWrapperListWrappingElements() {
    // Arrange: add some elements to the mockElements list
    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);
    mockElements.add(elem1);
    mockElements.add(elem2);

    // Act
    List<JsonElement> result = jsonArray.asList();

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof NonNullElementWrapperList);

    // Use new helper method to extract the wrapped list
    List<JsonElement> wrappedList = extractWrappedList(result);
    assertEquals(mockElements, wrappedList);
    assertEquals(2, result.size());
    assertEquals(elem1, result.get(0));
    assertEquals(elem2, result.get(1));
  }

  // Helper method to extract the wrapped list from NonNullElementWrapperList via reflection
  @SuppressWarnings("unchecked")
  private List<JsonElement> extractWrappedList(List<JsonElement> wrapperList) {
    try {
      Class<?> clazz = wrapperList.getClass();
      while (clazz != null) {
        for (Field field : clazz.getDeclaredFields()) {
          if (List.class.isAssignableFrom(field.getType())) {
            field.setAccessible(true);
            Object value = field.get(wrapperList);
            if (value instanceof List) {
              return (List<JsonElement>) value;
            }
          }
        }
        clazz = clazz.getSuperclass();
      }
      fail("Failed to find 'list' field in NonNullElementWrapperList or its superclasses");
      return null;
    } catch (IllegalAccessException e) {
      fail("Failed to access 'list' field in NonNullElementWrapperList or its superclasses");
      return null;
    }
  }
}