package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.NonNullElementWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class JsonArray_666_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void asList_shouldReturnNonNullElementWrapperListWrappingElements() throws Exception {
    // Prepare a mock element list
    ArrayList<JsonElement> mockElements = spy(new ArrayList<>());
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    mockElements.add(element1);
    mockElements.add(element2);

    // Inject mockElements into private final field 'elements' of jsonArray using reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, mockElements);

    // Call the focal method
    List<JsonElement> result = jsonArray.asList();

    // Verify the result is a NonNullElementWrapperList wrapping the same elements list
    assertNotNull(result);
    assertTrue(result instanceof NonNullElementWrapperList);

    // NonNullElementWrapperList delegates contains, size, get calls to underlying list
    assertEquals(2, result.size());
    assertTrue(result.contains(element1));
    assertTrue(result.contains(element2));

    // The underlying list inside NonNullElementWrapperList should be our mockElements list
    // (We can verify this by reflection since NonNullElementWrapperList is internal)
    Field listField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    listField.setAccessible(true);
    Object underlyingList = listField.get(result);
    assertSame(mockElements, underlyingList);
  }
}