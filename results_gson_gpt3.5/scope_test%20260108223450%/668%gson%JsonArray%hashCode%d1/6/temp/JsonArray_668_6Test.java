package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_668_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyElements() throws Exception {
    // elements is empty list by default
    int expectedHashCode = getElementsField(jsonArray).hashCode();
    int actualHashCode = jsonArray.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonEmptyElements() throws Exception {
    ArrayList<JsonElement> mockElements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    mockElements.add(element1);
    mockElements.add(element2);

    setElementsField(jsonArray, mockElements);

    int expectedHashCode = mockElements.hashCode();
    int actualHashCode = jsonArray.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_elementsWithDifferentContent() throws Exception {
    ArrayList<JsonElement> mockElements1 = new ArrayList<>();
    ArrayList<JsonElement> mockElements2 = new ArrayList<>();

    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    JsonElement element3 = mock(JsonElement.class);

    mockElements1.add(element1);
    mockElements1.add(element2);

    mockElements2.add(element1);
    mockElements2.add(element3);

    setElementsField(jsonArray, mockElements1);
    int hashCode1 = jsonArray.hashCode();

    setElementsField(jsonArray, mockElements2);
    int hashCode2 = jsonArray.hashCode();

    // hashCodes should differ if elements differ
    assertEquals(mockElements1.hashCode(), hashCode1);
    assertEquals(mockElements2.hashCode(), hashCode2);
    assert(hashCode1 != hashCode2);
  }

  private ArrayList<JsonElement> getElementsField(JsonArray instance) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(instance);
    return elements;
  }

  private void setElementsField(JsonArray instance, ArrayList<JsonElement> value) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(instance, value);
  }
}