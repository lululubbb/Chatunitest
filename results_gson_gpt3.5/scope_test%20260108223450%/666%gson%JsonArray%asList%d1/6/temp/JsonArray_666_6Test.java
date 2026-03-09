package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.internal.NonNullElementWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class JsonArray_666_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void asList_emptyElements_returnsEmptyNonNullElementWrapperList() throws Exception {
    // Use reflection to set private final elements field to empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    List<JsonElement> list = jsonArray.asList();

    assertNotNull(list);
    assertTrue(list instanceof NonNullElementWrapperList);
    assertTrue(list.isEmpty());
  }

  @Test
    @Timeout(8000)
  void asList_nonEmptyElements_returnsNonNullElementWrapperListWithElements() throws Exception {
    // Prepare a list with mocked JsonElement(s)
    ArrayList<JsonElement> elements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    elements.add(element1);
    elements.add(element2);

    // Inject elements list into private final field via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);

    List<JsonElement> list = jsonArray.asList();

    assertNotNull(list);
    assertTrue(list instanceof NonNullElementWrapperList);
    assertEquals(2, list.size());
    assertSame(element1, list.get(0));
    assertSame(element2, list.get(1));
  }
}