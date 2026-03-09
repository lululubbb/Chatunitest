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

class JsonArray_667_5Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    JsonArray array = new JsonArray();
    assertTrue(array.equals(array));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    JsonArray array = new JsonArray();
    assertFalse(array.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    JsonArray array = new JsonArray();
    String other = "not a JsonArray";
    assertFalse(array.equals(other));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentElements() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    // Add different elements to array2 using reflection to access private field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> elements1 = new ArrayList<>();
    elementsField.set(array1, elements1);

    ArrayList<JsonElement> elements2 = new ArrayList<>();
    JsonPrimitive prim = new JsonPrimitive("test");
    elements2.add(prim);
    elementsField.set(array2, elements2);

    assertFalse(array1.equals(array2));
  }

  @Test
    @Timeout(8000)
  void testEquals_equalElements() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    JsonPrimitive prim = new JsonPrimitive("test");

    ArrayList<JsonElement> elements1 = new ArrayList<>();
    elements1.add(prim);
    elementsField.set(array1, elements1);

    ArrayList<JsonElement> elements2 = new ArrayList<>();
    elements2.add(prim);
    elementsField.set(array2, elements2);

    assertTrue(array1.equals(array2));
  }
}