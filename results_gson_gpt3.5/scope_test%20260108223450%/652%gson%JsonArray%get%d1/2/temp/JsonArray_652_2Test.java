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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.lang.reflect.Field;
import java.util.ArrayList;

class JsonArray_652_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testGet_existingIndex_returnsElement() throws Exception {
    // Prepare a JsonElement mock
    JsonElement element = org.mockito.Mockito.mock(JsonElement.class);

    // Use reflection to get the private 'elements' field and add the element
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element);

    // Act
    JsonElement returned = jsonArray.get(0);

    // Assert
    assertSame(element, returned);
  }

  @Test
    @Timeout(8000)
  void testGet_indexOutOfBounds_throwsException() {
    // The elements list is empty, so get(0) must throw IndexOutOfBoundsException
    assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(0));
  }

  @Test
    @Timeout(8000)
  void testGet_multipleElements_returnsCorrectElements() throws Exception {
    JsonElement element1 = org.mockito.Mockito.mock(JsonElement.class);
    JsonElement element2 = org.mockito.Mockito.mock(JsonElement.class);
    JsonElement element3 = org.mockito.Mockito.mock(JsonElement.class);

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    elements.add(element1);
    elements.add(element2);
    elements.add(element3);

    assertSame(element1, jsonArray.get(0));
    assertSame(element2, jsonArray.get(1));
    assertSame(element3, jsonArray.get(2));
  }
}