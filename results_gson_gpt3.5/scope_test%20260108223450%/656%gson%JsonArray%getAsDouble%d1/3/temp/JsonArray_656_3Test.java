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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_656_3Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsDouble_returnsCorrectDouble() throws Exception {
    // Use reflection to set private elements field with a list containing singleElementMock
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    list.add(singleElementMock);
    elementsField.set(jsonArray, list);

    // Mock getAsDouble on singleElementMock
    when(singleElementMock.getAsDouble()).thenReturn(42.42);

    // Use reflection to invoke private getAsSingleElement method
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    JsonElement returnedElement = (JsonElement) getAsSingleElementMethod.invoke(jsonArray);
    assertSame(singleElementMock, returnedElement);

    // Call getAsDouble and verify it returns mocked value
    double result = jsonArray.getAsDouble();
    assertEquals(42.42, result);

    // Verify getAsDouble called on the singleElementMock
    verify(singleElementMock).getAsDouble();
  }

  @Test
    @Timeout(8000)
  void testGetAsDouble_emptyArray_throwsException() throws Exception {
    // elements is empty by default, but forcibly clear it to be sure
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    var list = new java.util.ArrayList<JsonElement>();
    elementsField.set(jsonArray, list);

    // Use reflection to get getAsSingleElement method
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Expect IllegalStateException when getAsDouble is called on empty array because getAsSingleElement throws it
    assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsDouble();
    });

    // Also test invoking private method directly throws IllegalStateException
    // Because invoke wraps the exception in InvocationTargetException, unwrap it here
    IllegalStateException thrown = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    }).getCause() instanceof IllegalStateException ? (IllegalStateException) assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElementMethod.invoke(jsonArray);
    }).getCause() : null;

    assertNotNull(thrown);
  }

}