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

public class JsonArray_647_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void remove_validIndex_removesAndReturnsElement() throws Exception {
    JsonElement element1 = new JsonPrimitive("one");
    JsonElement element2 = new JsonPrimitive("two");

    // Use reflection to add elements directly to the private elements list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element1);
    elements.add(element2);

    // Remove element at index 0
    JsonElement removed = jsonArray.remove(0);
    assertEquals(element1, removed);

    // Verify that the element was removed from the list
    assertEquals(1, elements.size());
    assertEquals(element2, elements.get(0));
  }

  @Test
    @Timeout(8000)
  public void remove_invalidIndex_throwsIndexOutOfBoundsException() throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(new JsonPrimitive("test"));

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(-1));
  }
}