package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_638_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyElements_returnsNewEmptyJsonArray() throws Exception {
    // Ensure elements is empty
    setElements(jsonArray, new ArrayList<>());

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_nonEmptyElements_returnsDeepCopiedJsonArray() throws Exception {
    // Prepare mock JsonElement with deepCopy behavior
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    elements.add(element2);

    setElements(jsonArray, elements);

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);
    assertEquals(2, copy.size());
    assertSame(element1Copy, copy.get(0));
    assertSame(element2Copy, copy.get(1));
  }

  // Helper method to set private final 'elements' field by reflection
  @SuppressWarnings("unchecked")
  private void setElements(JsonArray array, ArrayList<JsonElement> newElements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(array, newElements);
  }
}