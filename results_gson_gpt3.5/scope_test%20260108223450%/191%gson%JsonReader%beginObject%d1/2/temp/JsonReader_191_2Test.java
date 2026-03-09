package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_191_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  private static final int EMPTY_OBJECT = 1; // Corrected value for JsonScope.EMPTY_OBJECT

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedBeginObject_pushCalledAndPeekedReset() throws Exception {
    // Set peeked to PEEKED_BEGIN_OBJECT (1)
    setField(jsonReader, "peeked", 1);

    // spy on jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Instead of stubbing push(), we verify invocation after calling beginObject()
    spyReader.beginObject();

    // verify push called with JsonScope.EMPTY_OBJECT (1)
    verifyPushCalledWith(spyReader, EMPTY_OBJECT);

    // verify peeked reset to PEEKED_NONE (0)
    int peeked = (int) getField(spyReader, "peeked");
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  public void beginObject_peekedIsPeekedNone_doPeekCalledAndPushCalled() throws Exception {
    // Set peeked to PEEKED_NONE (0)
    setField(jsonReader, "peeked", 0);

    // spy on jsonReader
    JsonReader spyReader = spy(jsonReader);

    // mock doPeek to return PEEKED_BEGIN_OBJECT (1)
    doReturn(1).when(spyReader).doPeek();

    // invoke beginObject()
    spyReader.beginObject();

    // verify doPeek called
    verify(spyReader).doPeek();

    // verify push called with JsonScope.EMPTY_OBJECT (1)
    verifyPushCalledWith(spyReader, EMPTY_OBJECT);

    // verify peeked reset to PEEKED_NONE (0)
    int peeked = (int) getField(spyReader, "peeked");
    assertEquals(0, peeked);
  }

  @Test
    @Timeout(8000)
  public void beginObject_wrongPeeked_throwsIllegalStateException() throws Exception {
    // Set peeked to PEEKED_END_OBJECT (2) to trigger error
    setField(jsonReader, "peeked", 2);

    // spy on jsonReader to mock peek() and locationString()
    JsonReader spyReader = spy(jsonReader);

    // mock peek() to return JsonToken.END_OBJECT
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    // mock locationString() to return " at path $"
    doReturn(" at path $").when(spyReader).locationString();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::beginObject);
    assertEquals("Expected BEGIN_OBJECT but was END_OBJECT at path $", thrown.getMessage());
  }

  // Helper to set private field via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private field via reflection
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  // Helper to verify private push(int) method was called with given argument
  private void verifyPushCalledWith(JsonReader spyReader, int expectedArg) throws Exception {
    // Using reflection to get the private method 'push'
    Method pushMethod = JsonReader.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);

    // Use Mockito's mocking details to get invocations
    var mockingDetails = org.mockito.Mockito.mockingDetails(spyReader);
    var invocations = mockingDetails.getInvocations();

    boolean found = false;
    for (var invocation : invocations) {
      // Because push is private, the Method instance from invocation may not equal pushMethod
      // So compare by method name and parameter types as fallback
      if (invocation.getMethod().getName().equals("push")
          && invocation.getMethod().getParameterCount() == 1
          && invocation.getMethod().getParameterTypes()[0] == int.class) {
        Object arg = invocation.getArgument(0);
        if (arg instanceof Integer && ((Integer) arg) == expectedArg) {
          found = true;
          break;
        }
      }
    }
    assertTrue(found, "Expected push(" + expectedArg + ") to be called");
  }
}