package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_668_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() throws Exception {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_returnsElementsHashCode() throws Exception {
    // Create a real ArrayList and set it to the private final field 'elements'
    ArrayList<JsonElement> realElements = new ArrayList<>();

    // Use reflection to set the private final field 'elements' to the real list
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, realElements);

    // Calculate expected hash code from the real list
    int expectedHashCode = realElements.hashCode();

    int actualHashCode = jsonArray.hashCode();

    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withRealElements() throws Exception {
    // Replace with a real ArrayList with some JsonElements
    ArrayList<JsonElement> realElements = new ArrayList<>();
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, realElements);

    JsonElement element1 = new JsonPrimitive("element1");
    JsonElement element2 = new JsonPrimitive("element2");

    realElements.add(element1);
    realElements.add(element2);

    int expectedHashCode = realElements.hashCode();
    int actualHashCode = jsonArray.hashCode();

    assertEquals(expectedHashCode, actualHashCode);
  }
}