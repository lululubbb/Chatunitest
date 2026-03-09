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

public class JsonArray_648_6Test {

  private JsonArray jsonArray;
  private JsonElement elementMock;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    elementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testContains_ElementPresent() throws Exception {
    // Use reflection to access private 'elements' field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(elementMock);

    boolean result = jsonArray.contains(elementMock);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testContains_ElementAbsent() {
    boolean result = jsonArray.contains(elementMock);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testContains_NullElement() throws Exception {
    // Add null element to elements list via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(null);

    // contains(null) should return true because null is present
    assertTrue(jsonArray.contains(null));

    // contains(some element) should return false
    assertFalse(jsonArray.contains(elementMock));
  }
}