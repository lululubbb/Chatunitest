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

public class JsonArray_667_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void equals_SameReference_ReturnsTrue() {
    assertTrue(jsonArray.equals(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void equals_Null_ReturnsFalse() {
    assertFalse(jsonArray.equals(null));
  }

  @Test
    @Timeout(8000)
  public void equals_DifferentClass_ReturnsFalse() {
    assertFalse(jsonArray.equals(new Object()));
  }

  @Test
    @Timeout(8000)
  public void equals_DifferentElements_ReturnsFalse() throws Exception {
    JsonArray other = new JsonArray();

    // Set elements list in jsonArray with one element
    ArrayList<JsonElement> elements1 = new ArrayList<>();
    elements1.add(mock(JsonElement.class));
    setElementsField(jsonArray, elements1);

    // Set elements list in other with different element
    ArrayList<JsonElement> elements2 = new ArrayList<>();
    elements2.add(mock(JsonElement.class));
    setElementsField(other, elements2);

    assertFalse(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void equals_SameElements_ReturnsTrue() throws Exception {
    JsonArray other = new JsonArray();

    // Create a shared list with elements
    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);
    ArrayList<JsonElement> sharedElements = new ArrayList<>(Arrays.asList(elem1, elem2));

    setElementsField(jsonArray, sharedElements);
    setElementsField(other, sharedElements);

    assertTrue(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void equals_ElementsEqualButDifferentLists_ReturnsTrue() throws Exception {
    JsonArray other = new JsonArray();

    JsonElement elem1 = mock(JsonElement.class);
    JsonElement elem2 = mock(JsonElement.class);

    ArrayList<JsonElement> elements1 = new ArrayList<>(Arrays.asList(elem1, elem2));
    ArrayList<JsonElement> elements2 = new ArrayList<>(Arrays.asList(elem1, elem2));

    setElementsField(jsonArray, elements1);
    setElementsField(other, elements2);

    // elements lists are different instances but equal
    assertTrue(jsonArray.equals(other));
  }

  private void setElementsField(JsonArray instance, ArrayList<JsonElement> elements) throws Exception {
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(instance, elements);
  }
}