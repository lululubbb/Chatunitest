package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.util.ArrayList;

class JsonArrayEqualsTest {

  @Test
    @Timeout(8000)
  public void testEquals_sameInstance() {
    JsonArray jsonArray = new JsonArray();
    assertTrue(jsonArray.equals(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    JsonArray jsonArray = new JsonArray();
    assertFalse(jsonArray.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClassObject() {
    JsonArray jsonArray = new JsonArray();
    Object other = new Object();
    assertFalse(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_equalElements() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    // Use reflection to set elements field with same content
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> list1 = new ArrayList<>();
    ArrayList<JsonElement> list2 = new ArrayList<>();

    JsonElement elementMock = Mockito.mock(JsonElement.class);
    list1.add(elementMock);
    list2.add(elementMock);

    elementsField.set(array1, list1);
    elementsField.set(array2, list2);

    assertTrue(array1.equals(array2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentElements() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    ArrayList<JsonElement> list1 = new ArrayList<>();
    ArrayList<JsonElement> list2 = new ArrayList<>();

    JsonElement elementMock1 = Mockito.mock(JsonElement.class);
    JsonElement elementMock2 = Mockito.mock(JsonElement.class);

    list1.add(elementMock1);
    list2.add(elementMock2);

    elementsField.set(array1, list1);
    elementsField.set(array2, list2);

    // The equals of ArrayList compares elements by equals, so mock equals to return false
    Mockito.when(elementMock1.equals(elementMock2)).thenReturn(false);

    assertFalse(array1.equals(array2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_elementsListEqualsCalled() throws Exception {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);

    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> mockedList1 = Mockito.mock(ArrayList.class);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> mockedList2 = Mockito.mock(ArrayList.class);

    elementsField.set(array1, mockedList1);
    elementsField.set(array2, mockedList2);

    Mockito.when(mockedList2.equals(mockedList1)).thenReturn(true);
    // We want to test array2.equals(array1) calls elements.equals
    assertFalse(array1.equals(array2)); // array1.elements.equals(array2.elements) mocked, so false

    // But we can test array2.equals(array1)
    Mockito.when(mockedList1.equals(mockedList2)).thenReturn(true);
    assertTrue(array2.equals(array1));
  }
}