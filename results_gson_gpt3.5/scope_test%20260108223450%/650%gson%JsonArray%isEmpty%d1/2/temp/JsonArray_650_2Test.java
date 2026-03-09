package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

class JsonArray_650_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testIsEmpty_whenElementsIsEmpty_shouldReturnTrue() throws Exception {
    // Use reflection to set elements to an empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testIsEmpty_whenElementsHasElements_shouldReturnFalse() throws Exception {
    // Create a non-empty list with a mocked JsonElement
    ArrayList<JsonElement> list = new ArrayList<>();
    JsonElement mockElement = mock(JsonElement.class);
    list.add(mockElement);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, list);

    assertFalse(jsonArray.isEmpty());
  }
}