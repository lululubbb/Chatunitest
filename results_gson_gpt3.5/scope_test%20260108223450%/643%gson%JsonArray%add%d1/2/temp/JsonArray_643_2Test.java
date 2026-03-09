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

class JsonArray_643_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void add_NullElement_AddsJsonNullInstance() throws Exception {
    jsonArray.add((JsonElement) null);

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
    JsonElement mockElement = mock(JsonElement.class);
    jsonArray.add(mockElement);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(mockElement, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void add_MultipleElements_AddsAll() throws Exception {
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);

    jsonArray.add(mockElement1);
    jsonArray.add((JsonElement) null);
    jsonArray.add(mockElement2);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(3, elements.size());
    assertSame(mockElement1, elements.get(0));
    assertSame(JsonNull.INSTANCE, elements.get(1));
    assertSame(mockElement2, elements.get(2));
  }
}