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

public class JsonArray_641_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testAdd_withNullNumber_addsJsonNullInstance() throws Exception {
    jsonArray.add((Number) null);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @Test
    @Timeout(8000)
  public void testAdd_withIntegerNumber_addsJsonPrimitive() throws Exception {
    Integer number = 42;
    jsonArray.add(number);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(number, ((JsonPrimitive) element).getAsNumber());
  }

  @Test
    @Timeout(8000)
  public void testAdd_withDoubleNumber_addsJsonPrimitive() throws Exception {
    Double number = 3.14;
    jsonArray.add(number);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(number, ((JsonPrimitive) element).getAsNumber());
  }
}