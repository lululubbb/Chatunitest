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

class JsonArray_641_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void add_NumberAddsJsonPrimitiveWhenNotNull() throws Exception {
    Number number = 42;
    jsonArray.add(number);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertTrue(elements.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) elements.get(0);
    assertEquals(number, primitive.getAsNumber());
  }

  @Test
    @Timeout(8000)
  void add_NumberAddsJsonNullWhenNull() throws Exception {
    jsonArray.add((Number) null);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }
}