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

class JsonArray_668_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void hashCode_emptyElements() throws Exception {
    // elements is empty list initially
    int expectedHashCode = getElementsField(jsonArray).hashCode();
    assertEquals(expectedHashCode, jsonArray.hashCode());
  }

  @Test
    @Timeout(8000)
  void hashCode_nonEmptyElements() throws Exception {
    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    JsonElement element1 = new JsonPrimitive("test1");
    JsonElement element2 = new JsonPrimitive(123);
    elements.add(element1);
    elements.add(element2);

    int expectedHashCode = elements.hashCode();
    assertEquals(expectedHashCode, jsonArray.hashCode());
  }

  @Test
    @Timeout(8000)
  void hashCode_elementsWithNulls() throws Exception {
    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    elements.add(null);
    elements.add(new JsonPrimitive(true));

    int expectedHashCode = elements.hashCode();
    assertEquals(expectedHashCode, jsonArray.hashCode());
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray jsonArray) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(jsonArray);
  }
}