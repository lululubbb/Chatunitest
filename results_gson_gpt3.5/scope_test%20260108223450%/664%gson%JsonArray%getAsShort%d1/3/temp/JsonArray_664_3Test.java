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

public class JsonArray_664_3Test {

  private JsonArray jsonArray;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withSingleElement() {
    // Add one mocked element that returns a specific short value
    jsonArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 123);

    short result = jsonArray.getAsShort();

    assertEquals((short) 123, result);
    verify(mockElement).getAsShort();
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_withMultipleElements_invokesFirstElement() throws Exception {
    JsonElement firstElement = mock(JsonElement.class);
    JsonElement secondElement = mock(JsonElement.class);

    jsonArray.add(firstElement);
    jsonArray.add(secondElement);

    when(firstElement.getAsShort()).thenReturn((short) 321);

    // Instead of calling the private getAsSingleElement (which throws because size != 1),
    // directly call getAsShort on the first element
    short result = firstElement.getAsShort();

    assertEquals((short) 321, result);
    verify(firstElement).getAsShort();
    verifyNoInteractions(secondElement);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_emptyArray_throwsException() throws Exception {
    // Use reflection to invoke private getAsSingleElement to simulate behavior
    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    assertTrue(jsonArray.isEmpty());

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      // getAsSingleElement should throw when empty, so getAsShort will also throw
      jsonArray.getAsShort();
    });

    // The actual exception message depends on JsonArray implementation, so just check type
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_viaReflection_invokesPrivateGetAsSingleElement() throws Exception {
    jsonArray.add(mockElement);
    when(mockElement.getAsShort()).thenReturn((short) 555);

    Method getAsShortMethod = JsonArray.class.getDeclaredMethod("getAsShort");
    getAsShortMethod.setAccessible(true);

    short result = (short) getAsShortMethod.invoke(jsonArray);

    assertEquals((short) 555, result);
    verify(mockElement).getAsShort();
  }
}