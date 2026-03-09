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

class JsonArray_652_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testGetReturnsCorrectElement() throws Exception {
    // Prepare elements list via reflection
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    JsonElement element1 = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    JsonElement element2 = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    elements.add(element1);
    elements.add(element2);

    // Test get(0)
    JsonElement result0 = jsonArray.get(0);
    assertSame(element1, result0);

    // Test get(1)
    JsonElement result1 = jsonArray.get(1);
    assertSame(element2, result1);
  }

  @Test
    @Timeout(8000)
  void testGetThrowsIndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(0));
  }
}