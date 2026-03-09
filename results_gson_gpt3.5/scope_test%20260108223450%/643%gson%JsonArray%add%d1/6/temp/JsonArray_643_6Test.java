package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_643_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void add_NullElement_AddsJsonNullInstance() throws Exception {
    jsonArray.add((JsonElement) null);

    // Use reflection to access private elements field
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void add_NonNullElement_AddsElement() throws Exception {
    JsonElement element = mock(JsonElement.class);

    jsonArray.add(element);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(element, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void add_MultipleElements_AddsAll() throws Exception {
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    jsonArray.add(element1);
    jsonArray.add((JsonElement) null);
    jsonArray.add(element2);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(3, elements.size());
    assertSame(element1, elements.get(0));
    assertSame(JsonNull.INSTANCE, elements.get(1));
    assertSame(element2, elements.get(2));
  }
}