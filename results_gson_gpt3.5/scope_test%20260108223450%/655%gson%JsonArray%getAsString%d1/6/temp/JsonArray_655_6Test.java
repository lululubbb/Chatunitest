package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_655_6Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsString_withOneElement() throws Exception {
    // Use reflection to set private field 'elements'
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    when(singleElementMock.getAsString()).thenReturn("mockedString");

    String result = jsonArray.getAsString();

    assertEquals("mockedString", result);
    verify(singleElementMock).getAsString();
  }

  @Test
    @Timeout(8000)
  void testGetAsString_withNoElements_throwsIllegalStateException() throws Exception {
    // Ensure elements list is empty
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, new ArrayList<JsonElement>());

    // Use reflection to invoke private getAsSingleElement method that is called by getAsString
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // getAsString calls getAsSingleElement which will throw IllegalStateException on empty list
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsString());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_reflection() throws Exception {
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement result = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);

    assertSame(singleElementMock, result);
  }
}