package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_663_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_singleElementDelegates() {
    JsonElement mockElement = mock(JsonElement.class);
    when(mockElement.getAsCharacter()).thenReturn('x');

    jsonArray.add(mockElement);

    char result = jsonArray.getAsCharacter();

    assertEquals('x', result);
    verify(mockElement).getAsCharacter();
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyArray_throwsException() throws Exception {
    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    // The private method getAsSingleElement should throw IllegalStateException when empty
    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_multipleElements_throwsException() throws Exception {
    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);
    jsonArray.add(mockElement1);
    jsonArray.add(mockElement2);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      getAsSingleElement.invoke(jsonArray);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_singleElement_returnsElement() throws Exception {
    JsonElement mockElement = mock(JsonElement.class);
    jsonArray.add(mockElement);

    Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElement.setAccessible(true);

    Object result = getAsSingleElement.invoke(jsonArray);

    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_callsPrivateGetAsSingleElement() throws Exception {
    // Spy on jsonArray to verify private method call via reflection
    JsonArray spyArray = spy(new JsonArray());
    JsonElement mockElement = mock(JsonElement.class);
    spyArray.add(mockElement);

    // Because getAsSingleElement is private, we cannot mock it directly.
    // Instead, we verify behavior through getAsCharacter returned value.
    when(mockElement.getAsCharacter()).thenReturn('z');

    char result = spyArray.getAsCharacter();

    assertEquals('z', result);
    verify(mockElement).getAsCharacter();
  }
}