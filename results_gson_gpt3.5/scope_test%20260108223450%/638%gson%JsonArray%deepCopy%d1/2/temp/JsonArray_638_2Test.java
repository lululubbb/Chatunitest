package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArrayDeepCopyTest {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void deepCopy_emptyElements_returnsNewEmptyJsonArray() throws Exception {
    // elements is empty by default
    JsonArray copy = jsonArray.deepCopy();
    assertNotSame(jsonArray, copy);
    assertTrue(copy.isEmpty());
  }

  @Test
    @Timeout(8000)
  void deepCopy_nonEmptyElements_returnsDeepCopiedJsonArray() throws Exception {
    // Prepare a mock JsonElement with deepCopy behavior
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element1Copy = mock(JsonElement.class);
    when(element1.deepCopy()).thenReturn(element1Copy);

    JsonElement element2 = mock(JsonElement.class);
    JsonElement element2Copy = mock(JsonElement.class);
    when(element2.deepCopy()).thenReturn(element2Copy);

    // Inject elements list with two mocked elements using reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(element1);
    elements.add(element2);
    elementsField.set(jsonArray, elements);

    JsonArray copy = jsonArray.deepCopy();

    assertNotSame(jsonArray, copy);
    assertEquals(2, copy.size());

    // Verify deepCopy was called on each element
    verify(element1).deepCopy();
    verify(element2).deepCopy();

    // Verify elements in copy are the deep copies
    assertSame(element1Copy, copy.get(0));
    assertSame(element2Copy, copy.get(1));
  }
}