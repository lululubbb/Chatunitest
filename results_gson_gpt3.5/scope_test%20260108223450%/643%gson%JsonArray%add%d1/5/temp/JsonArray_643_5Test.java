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

class JsonArray_643_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void add_shouldAddElement_whenElementIsNotNull() throws Exception {
    JsonElement element = mock(JsonElement.class);

    jsonArray.add(element);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    assertSame(element, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void add_shouldAddJsonNullInstance_whenElementIsNull() throws Exception {
    jsonArray.add((JsonElement) null);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray instance) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(instance);
  }
}