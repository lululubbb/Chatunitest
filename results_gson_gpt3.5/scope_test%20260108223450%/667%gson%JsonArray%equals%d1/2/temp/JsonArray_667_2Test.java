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
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_667_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameReference() {
    assertTrue(jsonArray.equals(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    assertFalse(jsonArray.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClassObject() {
    assertFalse(jsonArray.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentJsonArray_sameElements() throws Exception {
    JsonArray other = new JsonArray();

    // Prepare elements list with mocks
    ArrayList<JsonElement> elements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    elements.add(element1);
    elements.add(element2);

    // Set elements field in jsonArray
    setElementsField(jsonArray, elements);

    // Set elements field in other JsonArray with same elements
    ArrayList<JsonElement> otherElements = new ArrayList<>(elements);
    setElementsField(other, otherElements);

    assertTrue(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentJsonArray_differentElements() throws Exception {
    JsonArray other = new JsonArray();

    ArrayList<JsonElement> elements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    elements.add(element1);

    ArrayList<JsonElement> otherElements = new ArrayList<>();
    JsonElement element2 = mock(JsonElement.class);
    otherElements.add(element2);

    setElementsField(jsonArray, elements);
    setElementsField(other, otherElements);

    assertFalse(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentJsonArray_elementsListSameReference() throws Exception {
    JsonArray other = new JsonArray();

    ArrayList<JsonElement> elements = new ArrayList<>();
    JsonElement element1 = mock(JsonElement.class);
    elements.add(element1);

    setElementsField(jsonArray, elements);
    setElementsField(other, elements);

    assertTrue(jsonArray.equals(other));
  }

  private void setElementsField(JsonArray jsonArrayInstance, ArrayList<JsonElement> elements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArrayInstance, elements);
  }
}