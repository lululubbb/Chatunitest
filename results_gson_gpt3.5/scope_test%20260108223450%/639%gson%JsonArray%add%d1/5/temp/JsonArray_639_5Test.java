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

public class JsonArray_639_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testAddBoolean_true() throws Exception {
    jsonArray.add(true);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddBoolean_false() throws Exception {
    jsonArray.add(false);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(false, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddBoolean_null() throws Exception {
    jsonArray.add((Boolean) null);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertSame(JsonNull.INSTANCE, element);
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray jsonArray) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(jsonArray);
  }
}