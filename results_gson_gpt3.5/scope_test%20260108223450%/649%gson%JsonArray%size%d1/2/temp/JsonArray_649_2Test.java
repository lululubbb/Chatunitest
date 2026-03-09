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

public class JsonArray_649_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testSize_EmptyElements() throws Exception {
    // Use reflection to set elements to empty ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    assertEquals(0, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_WithElements() throws Exception {
    // Prepare ArrayList with mocked JsonElements
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(new JsonPrimitive("one"));
    elementsList.add(new JsonPrimitive("two"));
    elementsList.add(new JsonPrimitive("three"));

    // Use reflection to set elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elementsList);

    assertEquals(3, jsonArray.size());
  }
}