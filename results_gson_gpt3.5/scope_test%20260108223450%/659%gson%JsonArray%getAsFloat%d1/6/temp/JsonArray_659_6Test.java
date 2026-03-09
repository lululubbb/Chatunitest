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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonArray_659_6Test {

  private JsonArray jsonArraySpy;
  private JsonElement jsonElementMock;

  @BeforeEach
  void setUp() {
    jsonArraySpy = Mockito.spy(new JsonArray());
    jsonElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsFloat_singleElement_returnsFloat() throws Exception {
    // Arrange
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // Stub the private method getAsSingleElement on jsonArraySpy using doAnswer
    doAnswer(invocation -> jsonElementMock)
        .when(jsonArraySpy)
        .getAsSingleElement();

    when(jsonElementMock.getAsFloat()).thenReturn(3.14f);

    // Act
    float result = jsonArraySpy.getAsFloat();

    // Assert
    assertEquals(3.14f, result, 0.0001f);
    verify(jsonElementMock).getAsFloat();
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_privateMethod_returnsFirstElement() throws Exception {
    // Arrange
    JsonArray jsonArray = new JsonArray();
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    // Use reflection to invoke private method getAsSingleElement
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act
    JsonElement result = (JsonElement) method.invoke(jsonArray);

    // Assert
    assertSame(first, result);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_privateMethod_emptyList_throwsException() throws Exception {
    JsonArray jsonArray = new JsonArray();

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    assertThrows(IndexOutOfBoundsException.class, () -> {
      method.invoke(jsonArray);
    });
  }

  @Test
    @Timeout(8000)
  void getAsFloat_withRealJsonArray_returnsFloat() {
    JsonArray jsonArray = new JsonArray();
    JsonPrimitive jsonPrimitive = new JsonPrimitive(5.5f);
    jsonArray.add(jsonPrimitive);

    float result = jsonArray.getAsFloat();

    assertEquals(5.5f, result, 0.0001f);
  }
}