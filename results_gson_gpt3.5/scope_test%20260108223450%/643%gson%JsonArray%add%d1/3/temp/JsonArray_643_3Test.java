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

public class JsonArray_643_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testAdd_NonNullElement() throws Exception {
    JsonElement element = mock(JsonElement.class);

    jsonArray.add(element);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    assertSame(element, elements.get(0));
  }

  @Test
    @Timeout(8000)
  public void testAdd_NullElement_ReplacesWithJsonNullInstance() throws Exception {
    jsonArray.add((JsonElement) null);

    ArrayList<JsonElement> elements = getElementsField(jsonArray);
    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  // Helper method to access the private final ArrayList<JsonElement> elements field using reflection
  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsField(JsonArray instance) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    return (ArrayList<JsonElement>) elementsField.get(instance);
  }
}