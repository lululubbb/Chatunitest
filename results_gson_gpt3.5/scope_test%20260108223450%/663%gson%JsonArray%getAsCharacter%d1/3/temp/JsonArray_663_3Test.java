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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_663_3Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_singleElementReturnsChar() {
    // Use reflection to set the private 'elements' field with a list containing singleElementMock
    try {
      // Access private field elements
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> list = new ArrayList<>();
      list.add(singleElementMock);
      elementsField.set(jsonArray, list);

      // Stub getAsCharacter on singleElementMock
      when(singleElementMock.getAsCharacter()).thenReturn('x');

      // Test getAsCharacter
      char result = jsonArray.getAsCharacter();
      assertEquals('x', result);

      // Verify getAsCharacter called on singleElementMock
      verify(singleElementMock).getAsCharacter();
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyArray_throwsIllegalStateException() {
    // elements list is empty by default in new JsonArray
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      jsonArray.getAsCharacter();
    });
    // No specific message expected, just assert exception thrown
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_multipleElements_throwsIllegalStateException() {
    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      ArrayList<JsonElement> list = new ArrayList<>();
      JsonElement firstElement = mock(JsonElement.class);
      JsonElement secondElement = mock(JsonElement.class);
      list.add(firstElement);
      list.add(secondElement);
      elementsField.set(jsonArray, list);

      // Because getAsSingleElement expects exactly one element, this should throw IllegalStateException
      assertThrows(IllegalStateException.class, () -> jsonArray.getAsCharacter());

      // Verify getAsCharacter is never called on mocks
      verify(firstElement, never()).getAsCharacter();
      verify(secondElement, never()).getAsCharacter();
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}