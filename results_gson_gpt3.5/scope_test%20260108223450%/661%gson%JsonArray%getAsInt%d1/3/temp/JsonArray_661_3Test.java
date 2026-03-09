package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class JsonArray_661_3Test {

  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_returnsIntFromSingleElement() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(singleElementMock);

    // Use reflection to replace the private getAsSingleElement method's behavior
    // Since JsonArray is final, we cannot subclass it.
    // Instead, we spy on the JsonArray and mock the private method via reflection.

    JsonArray spy = spy(jsonArray);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Use Mockito to stub the private method by using doReturn on the spy and reflection
    doReturn(singleElementMock).when(spy).get(0); // fallback in case getAsSingleElement uses get(0)
    // Unfortunately, Mockito cannot mock private methods directly.
    // So we use reflection to invoke getAsInt on the singleElementMock directly.

    when(singleElementMock.getAsInt()).thenReturn(42);

    // Because getAsSingleElement is private and cannot be mocked directly,
    // we rely on the actual method which returns the first element.
    int result = spy.getAsInt();

    assertEquals(42, result);
    verify(singleElementMock).getAsInt();
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_throwsIfSingleElementThrows() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(singleElementMock);

    JsonArray spy = spy(jsonArray);

    when(singleElementMock.getAsInt()).thenThrow(new NumberFormatException("bad number"));

    assertThrows(NumberFormatException.class, spy::getAsInt);
    verify(singleElementMock).getAsInt();
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElement_privateMethod_returnsCorrectElement() throws Exception {
    JsonArray jsonArray = new JsonArray();
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    JsonElement returned = (JsonElement) method.invoke(jsonArray);

    assertSame(element, returned);
  }
}