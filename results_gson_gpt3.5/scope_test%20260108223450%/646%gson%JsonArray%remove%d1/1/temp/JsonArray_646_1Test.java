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

public class JsonArray_646_1Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    element1 = mock(JsonElement.class);
    element2 = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testRemove_ElementPresent_ReturnsTrue() throws Exception {
    // Use reflection to get private field 'elements'
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element1);
    elements.add(element2);

    boolean removed = jsonArray.remove(element1);
    assertTrue(removed);
    assertFalse(elements.contains(element1));
    assertEquals(1, elements.size());
  }

  @Test
    @Timeout(8000)
  public void testRemove_ElementNotPresent_ReturnsFalse() {
    boolean removed = jsonArray.remove(element1);
    assertFalse(removed);
  }

  @Test
    @Timeout(8000)
  public void testRemove_NullElement_ReturnsFalse() {
    boolean removed = jsonArray.remove(null);
    assertFalse(removed);
  }
}