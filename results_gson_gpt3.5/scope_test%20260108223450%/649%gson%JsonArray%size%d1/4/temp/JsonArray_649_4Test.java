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

public class JsonArray_649_4Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testSize_EmptyElements() throws Exception {
    // Using reflection to set private field elements to empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    int size = jsonArray.size();

    assertEquals(0, size);
  }

  @Test
    @Timeout(8000)
  public void testSize_WithElements() throws Exception {
    // Using reflection to set private field elements to list with 3 mock JsonElement objects
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(new JsonPrimitive("one"));
    elements.add(new JsonPrimitive("two"));
    elements.add(new JsonPrimitive("three"));
    elementsField.set(jsonArray, elements);

    int size = jsonArray.size();

    assertEquals(3, size);
  }
}