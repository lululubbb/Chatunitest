package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_650_1Test {

  @Test
    @Timeout(8000)
  void testIsEmptyWhenEmpty() throws Exception {
    JsonArray jsonArray = new JsonArray();

    // Use reflection to clear elements field to empty list to be sure
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testIsEmptyWhenNotEmpty() throws Exception {
    JsonArray jsonArray = new JsonArray();

    // Use reflection to add one element to elements list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    elements.add(new JsonPrimitive("element"));

    assertFalse(jsonArray.isEmpty());
  }
}