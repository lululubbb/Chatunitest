package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_650_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenElementsListIsEmpty_shouldReturnTrue() throws Exception {
    // Using reflection to set elements to empty list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenElementsListIsNotEmpty_shouldReturnFalse() throws Exception {
    // Using reflection to set elements list with one element
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elements = new ArrayList<>();
    elements.add(new JsonPrimitive("test"));
    elementsField.set(jsonArray, elements);
    assertFalse(jsonArray.isEmpty());
  }
}