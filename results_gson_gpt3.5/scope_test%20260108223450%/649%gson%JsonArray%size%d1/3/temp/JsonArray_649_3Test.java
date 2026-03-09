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

public class JsonArray_649_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testSize_EmptyElements() throws Exception {
    // Use reflection to set the private final elements field to an empty ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    int size = jsonArray.size();
    assertEquals(0, size);
  }

  @Test
    @Timeout(8000)
  public void testSize_WithElements() throws Exception {
    // Create an ArrayList with some JsonElement mocks
    ArrayList<JsonElement> mockList = new ArrayList<>();
    mockList.add(new JsonPrimitive("a"));
    mockList.add(new JsonPrimitive("b"));
    mockList.add(new JsonPrimitive("c"));

    // Set the private final elements field to the mockList via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, mockList);

    int size = jsonArray.size();
    assertEquals(3, size);
  }
}