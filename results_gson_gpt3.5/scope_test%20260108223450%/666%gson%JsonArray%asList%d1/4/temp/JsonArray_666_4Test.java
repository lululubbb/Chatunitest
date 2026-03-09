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

class JsonArray_666_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void asList_shouldReturnNonNullElementWrapperListWrappingElements() throws Exception {
    // Prepare a spy on the elements list inside jsonArray
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    elementsList.add(element1);
    elementsList.add(element2);

    // Use reflection to set the private final 'elements' field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elementsList);

    // Call asList and verify the returned list
    List<JsonElement> result = jsonArray.asList();

    assertNotNull(result, "asList() should not return null");
    assertTrue(result instanceof NonNullElementWrapperList, "Returned list should be instance of NonNullElementWrapperList");
    assertEquals(2, result.size(), "Returned list should have the same size as elements list");
    assertEquals(element1, result.get(0), "First element should match");
    assertEquals(element2, result.get(1), "Second element should match");
  }
}