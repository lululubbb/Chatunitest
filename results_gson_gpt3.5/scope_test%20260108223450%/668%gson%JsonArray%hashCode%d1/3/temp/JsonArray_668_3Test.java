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
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_668_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyElements() throws Exception {
    // elements is empty list by default
    int expectedHashCode = getElementsField(jsonArray).hashCode();
    int actualHashCode = jsonArray.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withElements() throws Exception {
    ArrayList<JsonElement> elements = new ArrayList<>();
    JsonPrimitive element1 = new JsonPrimitive("test1");
    JsonPrimitive element2 = new JsonPrimitive(123);
    elements.add(element1);
    elements.add(element2);

    setElementsField(jsonArray, elements);

    int expectedHashCode = elements.hashCode();
    int actualHashCode = jsonArray.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  private ArrayList<JsonElement> getElementsField(JsonArray jsonArray) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    return elements;
  }

  private void setElementsField(JsonArray jsonArray, ArrayList<JsonElement> elements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elements);
  }
}