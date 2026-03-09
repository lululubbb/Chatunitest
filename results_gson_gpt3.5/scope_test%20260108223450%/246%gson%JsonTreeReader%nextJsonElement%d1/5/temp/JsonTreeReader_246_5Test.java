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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonTreeReader_246_5Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonElement to initialize JsonTreeReader
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    reader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldReturnElementAndSkipValue_whenPeekIsValid() throws Exception {
    // Create a spy of the reader
    JsonTreeReader spyReader = spy(reader);

    // Stub peek() to return a valid JsonToken (e.g. BEGIN_OBJECT)
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Use reflection to set the private stack field to contain our element
    JsonObject element = new JsonObject();
    element.addProperty("foo", "bar");

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    stack[0] = element;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    // Call nextJsonElement() via reflection since it has package-private access
    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);
    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    // Verify returned element is the one from peekStack()
    assertSame(element, result);

    // Verify skipValue() was called once
    verify(spyReader, times(1)).skipValue();
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsName() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      nextJsonElementMethod.invoke(spyReader);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Unexpected NAME"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndArray() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      nextJsonElementMethod.invoke(spyReader);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Unexpected END_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndObject() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      nextJsonElementMethod.invoke(spyReader);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Unexpected END_OBJECT"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndDocument() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      nextJsonElementMethod.invoke(spyReader);
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Unexpected END_DOCUMENT"));
  }
}