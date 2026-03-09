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

class JsonArray_642_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testAdd_withNonNullString_addsJsonPrimitive() throws Exception {
    String testString = "test";

    jsonArray.add(testString);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(testString, ((JsonPrimitive) element).getAsString());
  }

  @Test
    @Timeout(8000)
  void testAdd_withNullString_addsJsonNullInstance() throws Exception {
    // Cast null to String to resolve ambiguity
    jsonArray.add((String) null);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertSame(JsonNull.INSTANCE, element);
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray array) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(array);
  }
}