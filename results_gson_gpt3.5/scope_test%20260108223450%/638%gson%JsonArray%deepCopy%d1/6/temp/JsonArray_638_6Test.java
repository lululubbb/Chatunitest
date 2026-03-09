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

public class JsonArray_638_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_emptyElements_returnsNewEmptyJsonArray() throws Exception {
    // Ensure elements list is empty
    setElements(jsonArray, new ArrayList<>());

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_nonEmptyElements_copiesAllElements() throws Exception {
    // Create mock JsonElement with deepCopy behavior
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    JsonElement copy1 = mock(JsonElement.class);
    JsonElement copy2 = mock(JsonElement.class);

    when(element1.deepCopy()).thenReturn(copy1);
    when(element2.deepCopy()).thenReturn(copy2);

    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    elements.add(element2);

    setElements(jsonArray, elements);

    JsonArray copy = jsonArray.deepCopy();

    assertNotNull(copy);
    assertNotSame(jsonArray, copy);
    assertEquals(2, copy.size());

    // Verify that deepCopy was called on each element
    verify(element1).deepCopy();
    verify(element2).deepCopy();

    // Verify that copied elements are those returned by deepCopy
    assertSame(copy1, copy.get(0));
    assertSame(copy2, copy.get(1));
  }

  // Utility method to set private final field 'elements' via reflection
  @SuppressWarnings("unchecked")
  private void setElements(JsonArray jsonArray, ArrayList<JsonElement> newElements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, newElements);
  }
}