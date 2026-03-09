package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class JsonTreeReader_246_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement to initialize JsonTreeReader
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldReturnElementAndSkipValue_whenPeekIsValid() throws Exception {
    // Use reflection to access private method peekStack
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    // Use reflection to access private method skipValue
    Method skipValueMethod = JsonTreeReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);

    // Spy on jsonTreeReader to stub peek() and verify skipValue() call
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);

    // Stub peek() to return a JsonToken that is NOT in the exception list
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Push the element onto the stack to avoid ArrayIndexOutOfBoundsException
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Instead of calling jsonTreeReader.peekStack() directly (private), use reflection on spyReader
    Object peekedStack = peekStackMethod.invoke(spyReader);
    pushMethod.invoke(spyReader, peekedStack);

    // Call nextJsonElement via reflection
    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);
    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    // The returned element should be the same as peekStack()
    Object peekedStackAfter = peekStackMethod.invoke(spyReader);
    assertSame(peekedStackAfter, result);

    // Verify skipValue() was called exactly once
    verify(spyReader, times(1)).skipValue();
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsName() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        nextJsonElementMethod.invoke(spyReader);
      } catch (Exception e) {
        // unwrap InvocationTargetException
        Throwable cause = e.getCause();
        if (cause instanceof IllegalStateException) {
          throw (IllegalStateException) cause;
        }
        throw e;
      }
    });
    assertTrue(thrown.getMessage().contains("Unexpected NAME"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndArray() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        nextJsonElementMethod.invoke(spyReader);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof IllegalStateException) {
          throw (IllegalStateException) cause;
        }
        throw e;
      }
    });
    assertTrue(thrown.getMessage().contains("Unexpected END_ARRAY"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndObject() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        nextJsonElementMethod.invoke(spyReader);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof IllegalStateException) {
          throw (IllegalStateException) cause;
        }
        throw e;
      }
    });
    assertTrue(thrown.getMessage().contains("Unexpected END_OBJECT"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndDocument() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      try {
        nextJsonElementMethod.invoke(spyReader);
      } catch (Exception e) {
        Throwable cause = e.getCause();
        if (cause instanceof IllegalStateException) {
          throw (IllegalStateException) cause;
        }
        throw e;
      }
    });
    assertTrue(thrown.getMessage().contains("Unexpected END_DOCUMENT"));
  }
}