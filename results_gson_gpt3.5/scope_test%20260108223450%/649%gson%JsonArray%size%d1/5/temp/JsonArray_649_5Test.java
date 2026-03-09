package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_649_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testSize_emptyElements() throws Exception {
    // Use reflection to set private field 'elements' to an empty ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    int size = jsonArray.size();
    assertEquals(0, size);
  }

  @Test
    @Timeout(8000)
  void testSize_withElements() throws Exception {
    // Use reflection to set private field 'elements' with 3 JsonElement mocks
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> mockElements = new ArrayList<>();
    mockElements.add(new JsonPrimitive("one"));
    mockElements.add(new JsonPrimitive("two"));
    mockElements.add(new JsonPrimitive("three"));
    elementsField.set(jsonArray, mockElements);

    int size = jsonArray.size();
    assertEquals(3, size);
  }
}