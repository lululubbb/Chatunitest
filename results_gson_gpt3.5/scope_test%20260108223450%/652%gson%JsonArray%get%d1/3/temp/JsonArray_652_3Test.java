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

class JsonArray_652_3Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Create mock JsonElement instances using anonymous class or real instances as JsonElement is abstract
    element1 = new JsonElement() {
      @Override public JsonElement deepCopy() { return this; }
    };
    element2 = new JsonElement() {
      @Override public JsonElement deepCopy() { return this; }
    };

    // Use reflection to set private final ArrayList<JsonElement> elements field for controlled testing
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsList.add(element2);
    elementsField.set(jsonArray, elementsList);
  }

  @Test
    @Timeout(8000)
  void get_validIndex_returnsCorrectElement() {
    assertSame(element1, jsonArray.get(0));
    assertSame(element2, jsonArray.get(1));
  }

  @Test
    @Timeout(8000)
  void get_invalidIndex_throwsIndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(2));
  }
}