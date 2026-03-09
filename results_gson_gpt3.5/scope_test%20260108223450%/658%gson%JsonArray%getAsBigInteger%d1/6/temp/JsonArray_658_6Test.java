package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_658_6Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_emptyList_throwsException() throws Exception {
    // Use reflection to set private 'elements' field to empty list
    setElements(jsonArray, new ArrayList<>());

    // getAsSingleElement should throw IllegalStateException (not IndexOutOfBoundsException)
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigInteger());
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_singleElement_returnsBigInteger() throws Exception {
    ArrayList<JsonElement> list = new ArrayList<>();
    list.add(mockElement);
    setElements(jsonArray, list);

    BigInteger expected = BigInteger.valueOf(12345L);
    when(mockElement.getAsBigInteger()).thenReturn(expected);

    BigInteger actual = jsonArray.getAsBigInteger();

    assertEquals(expected, actual);
    verify(mockElement).getAsBigInteger();
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_multipleElements_throwsException() throws Exception {
    JsonElement mockElement2 = mock(JsonElement.class);
    ArrayList<JsonElement> list = new ArrayList<>();
    list.add(mockElement);
    list.add(mockElement2);
    setElements(jsonArray, list);

    // Expect IllegalStateException because getAsSingleElement requires exactly one element
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigInteger());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_reflection_access() throws Exception {
    ArrayList<JsonElement> list = new ArrayList<>();
    list.add(mockElement);
    setElements(jsonArray, list);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement result = (JsonElement) method.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  private void setElements(JsonArray array, ArrayList<JsonElement> elements) throws Exception {
    var field = JsonArray.class.getDeclaredField("elements");
    field.setAccessible(true);
    field.set(array, elements);
  }
}