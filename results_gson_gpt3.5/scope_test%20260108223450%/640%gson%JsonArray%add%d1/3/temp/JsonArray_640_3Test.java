package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonArray_640_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testAdd_NullCharacter_AddsJsonNullInstance() throws Exception {
    jsonArray.add((Character) null);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @Test
    @Timeout(8000)
  public void testAdd_NonNullCharacter_AddsJsonPrimitive() throws Exception {
    Character c = 'x';
    jsonArray.add(c);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(c, primitive.getAsCharacter());
  }
}