package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_657_6Test {

  private JsonArray jsonArray;
  private JsonElement jsonElementMock;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    jsonElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_singleElement_returnsBigDecimal() {
    // Arrange
    jsonArray.add(jsonElementMock);
    BigDecimal expected = new BigDecimal("123.45");
    when(jsonElementMock.getAsBigDecimal()).thenReturn(expected);

    // Act
    BigDecimal actual = jsonArray.getAsBigDecimal();

    // Assert
    assertEquals(expected, actual);
    verify(jsonElementMock).getAsBigDecimal();
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_empty_throwsIllegalStateException() {
    // The private method getAsSingleElement is called by getAsBigDecimal.
    // When empty, getAsSingleElement() will throw IllegalStateException.
    // We test that getAsBigDecimal also throws this exception.
    assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_returnsCorrectElement() throws Exception {
    // Arrange
    jsonArray.add(jsonElementMock);

    // Use reflection to invoke private getAsSingleElement()
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);

    // Act
    Object result = method.invoke(jsonArray);

    // Assert
    assertSame(jsonElementMock, result);
  }

  @Test
    @Timeout(8000)
  void getAsSingleElement_reflection_empty_throwsException() throws Exception {
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> method.invoke(jsonArray));
    assertTrue(thrown.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_multipleElements_throwsIllegalStateException() {
    JsonElement first = mock(JsonElement.class);
    JsonElement second = mock(JsonElement.class);
    jsonArray.add(first);
    jsonArray.add(second);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonArray.getAsBigDecimal());
    assertEquals("Array must have size 1, but has size 2", thrown.getMessage());

    verifyNoInteractions(first, second);
  }
}