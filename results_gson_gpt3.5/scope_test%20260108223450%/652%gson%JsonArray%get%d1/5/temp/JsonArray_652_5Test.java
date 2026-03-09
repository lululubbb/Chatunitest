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

class JsonArray_652_5Test {

  private JsonArray jsonArray;
  private JsonElement element1;
  private JsonElement element2;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    element1 = new JsonPrimitive("test1");
    element2 = new JsonPrimitive("test2");
  }

  @Test
    @Timeout(8000)
  void testGet_ValidIndex_ReturnsCorrectElement() {
    jsonArray.add(element1);
    jsonArray.add(element2);

    JsonElement result0 = jsonArray.get(0);
    JsonElement result1 = jsonArray.get(1);

    assertSame(element1, result0);
    assertSame(element2, result1);
  }

  @Test
    @Timeout(8000)
  void testGet_IndexOutOfBounds_ThrowsException() {
    jsonArray.add(element1);

    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(1));
  }

  @Test
    @Timeout(8000)
  void testGet_ReflectiveAccessToElementsField() throws Exception {
    // Prepare elements list with reflection
    ArrayList<JsonElement> elementsList = new ArrayList<>();
    elementsList.add(element1);
    elementsList.add(element2);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, elementsList);

    JsonElement result0 = jsonArray.get(0);
    JsonElement result1 = jsonArray.get(1);

    assertSame(element1, result0);
    assertSame(element2, result1);
  }
}