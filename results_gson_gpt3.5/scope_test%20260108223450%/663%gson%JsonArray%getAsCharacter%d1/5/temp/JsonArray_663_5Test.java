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

public class JsonArray_663_5Test {

  private JsonArray jsonArray;
  private JsonElement singleElementMock;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
    singleElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_delegatesToSingleElement() {
    try {
      // Add mock element to jsonArray
      jsonArray.add(singleElementMock);

      // Spy on jsonArray
      JsonArray spyJsonArray = Mockito.spy(jsonArray);

      // Use reflection to get private method
      Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
      getAsSingleElementMethod.setAccessible(true);

      // Instead of mocking private method directly, mock the singleElementMock's getAsCharacter() 
      // so when getAsSingleElement() is called, it returns the singleElementMock whose getAsCharacter() is mocked.
      when(singleElementMock.getAsCharacter()).thenReturn('x');

      // Call getAsCharacter() on spyJsonArray, which calls getAsSingleElement() internally
      char result = spyJsonArray.getAsCharacter();

      assertEquals('x', result);

    } catch (Exception e) {
      fail("Reflection or mocking failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_whenEmpty_throwsIllegalStateException() throws Exception {
    JsonArray spyJsonArray = Mockito.spy(jsonArray);

    Method getAsSingleElementMethod = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    getAsSingleElementMethod.setAccessible(true);

    // InvocationTargetException is thrown by reflection, unwrap cause to assert the actual exception type
    Throwable thrown = assertThrows(Throwable.class, () -> {
      getAsSingleElementMethod.invoke(spyJsonArray);
    });
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException, "Expected IllegalStateException but was " + cause.getClass());
    assertEquals("Array must have size 1, but has size 0", cause.getMessage());

    // The public getAsCharacter() method throws the same IllegalStateException
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      spyJsonArray.getAsCharacter();
    });
    assertEquals("Array must have size 1, but has size 0", ex.getMessage());
  }
}