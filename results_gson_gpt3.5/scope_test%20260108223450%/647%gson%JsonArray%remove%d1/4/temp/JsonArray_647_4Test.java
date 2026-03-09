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

class JsonArray_647_4Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Create mock JsonElement instances by implementing the abstract method deepCopy()
    element1 = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    element2 = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    // Use reflection to access the private final elements field and initialize it to a new ArrayList
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    // Add element1 and element2 to the elements list to prepare for remove tests
    ((ArrayList<JsonElement>) elementsField.get(jsonArray)).add(element1);
    ((ArrayList<JsonElement>) elementsField.get(jsonArray)).add(element2);
  }

  @Test
    @Timeout(8000)
  void remove_validIndex_removesAndReturnsElement() {
    JsonElement removed = jsonArray.remove(0);
    assertSame(element1, removed);

    // Verify element1 is removed and element2 remains
    // Access elements list via reflection
    ArrayList<JsonElement> elements = getElementsList(jsonArray);
    assertEquals(1, elements.size());
    assertSame(element2, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void remove_lastIndex_removesAndReturnsElement() {
    JsonElement removed = jsonArray.remove(1);
    assertSame(element2, removed);

    ArrayList<JsonElement> elements = getElementsList(jsonArray);
    assertEquals(1, elements.size());
    assertSame(element1, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void remove_invalidIndex_throwsIndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.remove(2));
  }

  @SuppressWarnings("unchecked")
  private ArrayList<JsonElement> getElementsList(JsonArray jsonArray) {
    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      return (ArrayList<JsonElement>) elementsField.get(jsonArray);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}