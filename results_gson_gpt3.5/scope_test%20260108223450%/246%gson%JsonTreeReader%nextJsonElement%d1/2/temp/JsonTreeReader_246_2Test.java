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

class JsonTreeReader_246_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonElement to pass to constructor
    JsonObject root = new JsonObject();
    root.add("key", new JsonPrimitive("value"));
    reader = new JsonTreeReader(root);

    // Use reflection to set stack and stackSize for coverage
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = root;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldReturnElementAndSkipValue_whenPeekIsValid() throws Exception {
    // Spy on reader
    JsonTreeReader spyReader = spy(reader);

    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

    // Use reflection to invoke private peekStack method
    Method peekStack = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStack.setAccessible(true);
    Object topElement = peekStack.invoke(spyReader);

    // Instead of mocking private method, stub peekStack invocation via doAnswer on peekStack method call through reflection
    // We cannot mock private methods with Mockito directly, so we do not mock peekStack,
    // but verify that the returned value from nextJsonElement equals topElement.

    doNothing().when(spyReader).skipValue();

    JsonElement result = spyReader.nextJsonElement();

    assertNotNull(result);
    assertEquals(topElement, result);

    verify(spyReader).peek();
    verify(spyReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsName() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextJsonElement);
    assertTrue(exception.getMessage().contains("Unexpected NAME"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndArray() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextJsonElement);
    assertTrue(exception.getMessage().contains("Unexpected END_ARRAY"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndObject() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextJsonElement);
    assertTrue(exception.getMessage().contains("Unexpected END_OBJECT"));
  }

  @Test
    @Timeout(8000)
  void nextJsonElement_shouldThrowIllegalStateException_whenPeekIsEndDocument() throws Exception {
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    IllegalStateException exception = assertThrows(IllegalStateException.class, spyReader::nextJsonElement);
    assertTrue(exception.getMessage().contains("Unexpected END_DOCUMENT"));
  }
}