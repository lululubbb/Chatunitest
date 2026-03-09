package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_238_6Test {

  JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    JsonObject obj = new JsonObject();
    obj.addProperty("key1", "value1");
    reader = new JsonTreeReader(obj);
  }

  @Test
    @Timeout(8000)
  void nextName_skipFalse_returnsKeyAndUpdatesPathNames() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<String, String>> iterator = mock(Iterator.class);
    @SuppressWarnings("unchecked")
    Map.Entry<String, String> entry = mock(Map.Entry.class);
    when(entry.getKey()).thenReturn("mockKey");
    when(entry.getValue()).thenReturn("mockValue");
    when(iterator.next()).thenReturn(entry);

    setStackTop(iterator);
    setStackSize(1);

    setPathNames(new String[32]);

    JsonTreeReader spyReader = Mockito.spy(reader);

    prepareExpect(spyReader, JsonToken.NAME);

    // Act
    String result = invokeNextName(spyReader, false);

    // Assert
    assertEquals("mockKey", result);
    String[] pathNames = getPathNames(spyReader);
    assertEquals("mockKey", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  void nextName_skipTrue_returnsSkippedAndUpdatesPathNames() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<String, String>> iterator = mock(Iterator.class);
    @SuppressWarnings("unchecked")
    Map.Entry<String, String> entry = mock(Map.Entry.class);
    when(entry.getKey()).thenReturn("mockKey");
    when(entry.getValue()).thenReturn("mockValue");
    when(iterator.next()).thenReturn(entry);

    setStackTop(iterator);
    setStackSize(1);

    setPathNames(new String[32]);

    JsonTreeReader spyReader = Mockito.spy(reader);

    prepareExpect(spyReader, JsonToken.NAME);

    // Act
    String result = invokeNextName(spyReader, true);

    // Assert
    assertEquals("mockKey", result);
    String[] pathNames = getPathNames(spyReader);
    assertEquals("<skipped>", pathNames[0]);
  }

  @Test
    @Timeout(8000)
  void nextName_pushesEntryValueToStack() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<String, String>> iterator = mock(Iterator.class);
    @SuppressWarnings("unchecked")
    Map.Entry<String, String> entry = mock(Map.Entry.class);
    when(entry.getKey()).thenReturn("mockKey");
    String pushedValue = "pushedValue";
    when(entry.getValue()).thenReturn(pushedValue);
    when(iterator.next()).thenReturn(entry);

    setStackTop(iterator);
    setStackSize(1);

    JsonTreeReader spyReader = Mockito.spy(reader);

    prepareExpect(spyReader, JsonToken.NAME);

    // Act
    invokeNextName(spyReader, false);

    // Assert
    Object[] stack = getStack(spyReader);
    int stackSize = getStackSize(spyReader);
    assertEquals(pushedValue, stack[stackSize - 1]);
  }

  // Helper to invoke private nextName(boolean)
  private String invokeNextName(JsonTreeReader spyReader, boolean skipName) throws Exception {
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);
    return (String) nextNameMethod.invoke(spyReader, skipName);
  }

  // Helpers to modify private fields via reflection
  private void setStackTop(Object top) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = top;
    stackField.set(reader, stack);
  }

  private Object[] getStack(JsonTreeReader instance) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (Object[]) stackField.get(instance);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, size);
  }

  private int getStackSize(JsonTreeReader instance) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(instance);
  }

  private void setPathNames(String[] pathNames) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(reader, pathNames);
  }

  private String[] getPathNames(JsonTreeReader instance) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    return (String[]) pathNamesField.get(instance);
  }

  // Prepare the stack top to be JsonToken.NAME to satisfy expect call
  private void prepareExpect(JsonTreeReader spyReader, JsonToken expectedToken) throws Exception {
    // We need to make peekStack() return the correct token to satisfy expect
    // Since expect calls peekStack() and compares with expectedToken

    // We will mock peekStack() via spyReader to return the expected token

    // peekStack is private, so we use reflection to override it via spy

    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    // Mockito cannot mock private methods directly, but spy can intercept public methods.
    // So we do the following workaround:

    // Instead, since peekStack() returns stack[stackSize -1], 
    // and expect(JsonToken) calls peekStack() and compares to expectedToken,
    // we can set the top of the stack to the expectedToken.

    // But in the real code, stack top is an Iterator, so peekStack() returns that.
    // Actually, expect(JsonToken.NAME) checks if the next token is NAME.

    // The method expect(JsonToken) does something like:
    // if (peek() != expected) throw IOException

    // peek() calls peekStack() and returns a JsonToken.

    // So instead of mocking peekStack(), we set stack top to an Iterator whose next token is NAME.

    // To do this, we can mock the iterator to behave as expected.

    // But since we already set stack top to iterator in tests, expect(JsonToken.NAME) will succeed if peek() returns NAME.

    // So we must ensure that peek() returns JsonToken.NAME.

    // peek() is public, so we can spy and override peek() to return expectedToken.

    JsonTreeReader spy = spyReader;

    // Override peek() method to return expectedToken
    // peek() is public JsonToken peek()

    Method peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Use Mockito to stub peek() on spyReader
    doReturn(expectedToken).when(spy).peek();
  }
}