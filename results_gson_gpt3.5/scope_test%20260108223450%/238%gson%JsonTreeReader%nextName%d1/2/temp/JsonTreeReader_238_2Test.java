package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class JsonTreeReader_238_2Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a minimal JsonElement to pass to constructor (can be mocked)
    JsonElement element = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(element);

    // Using reflection to set stack and stackSize for testing
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);

    // Initialize stack with capacity 32
    Object[] stack = new Object[32];
    stackField.set(jsonTreeReader, stack);
    stackSizeField.setInt(jsonTreeReader, 1); // stackSize = 1

    // Initialize pathNames array
    String[] pathNames = new String[32];
    pathNamesField.set(jsonTreeReader, pathNames);
  }

  @Test
    @Timeout(8000)
  public void nextName_returnsNameAndPushesValue_pathNamesSet_skipNameFalse() throws Exception {
    // Prepare a mock Iterator<Map.Entry<?, ?>>
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<?, ?>> iterator = mock(Iterator.class);

    // Prepare a mock Map.Entry
    @SuppressWarnings("unchecked")
    Map.Entry<String, JsonElement> entry = mock(Map.Entry.class);

    when(entry.getKey()).thenReturn("testName");
    JsonElement value = mock(JsonElement.class);
    when(entry.getValue()).thenReturn(value);
    when(iterator.next()).thenReturn(entry);

    // Inject the iterator into stack[stackSize - 1] (stack[0])
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = iterator;

    // Spy on jsonTreeReader to mock expect(JsonToken.NAME) call
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doNothing().when(spyReader).expect(JsonToken.NAME);

    // Use reflection to invoke private nextName(boolean)
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(spyReader, false);

    assertEquals("testName", result);

    // Verify that pathNames[stackSize - 1] is set to "testName"
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(spyReader);
    assertEquals("testName", pathNames[0]);

    // Verify that push() was called with entry.getValue()
    // push() is private, so verify by checking stack[stackSize] after push (stackSize should increase)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSize);
    Object[] updatedStack = (Object[]) stackField.get(spyReader);
    assertSame(value, updatedStack[1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_returnsSkippedName_pathNamesSet_skipNameTrue() throws Exception {
    // Prepare a mock Iterator<Map.Entry<?, ?>>
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<?, ?>> iterator = mock(Iterator.class);

    // Prepare a mock Map.Entry
    @SuppressWarnings("unchecked")
    Map.Entry<String, JsonElement> entry = mock(Map.Entry.class);

    when(entry.getKey()).thenReturn("anotherName");
    JsonElement value = mock(JsonElement.class);
    when(entry.getValue()).thenReturn(value);
    when(iterator.next()).thenReturn(entry);

    // Inject the iterator into stack[stackSize - 1] (stack[0])
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = iterator;

    // Spy on jsonTreeReader to mock expect(JsonToken.NAME) call
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doNothing().when(spyReader).expect(JsonToken.NAME);

    // Use reflection to invoke private nextName(boolean)
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(spyReader, true);

    assertEquals("anotherName", result);

    // Verify that pathNames[stackSize - 1] is set to "<skipped>"
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(spyReader);
    assertEquals("<skipped>", pathNames[0]);

    // Verify push() was called with entry.getValue()
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSize);
    Object[] updatedStack = (Object[]) stackField.get(spyReader);
    assertSame(value, updatedStack[1]);
  }

  @Test
    @Timeout(8000)
  public void nextName_expectThrowsIOException_propagates() throws Exception {
    // Spy on jsonTreeReader to throw IOException on expect
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doThrow(new IOException("expect failed")).when(spyReader).expect(JsonToken.NAME);

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> nextNameMethod.invoke(spyReader, false));
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("expect failed", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  public void nextName_iteratorNextThrowsNoSuchElementException_propagates() throws Exception {
    // Prepare a mock Iterator<Map.Entry<?, ?>>
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<?, ?>> iterator = mock(Iterator.class);
    when(iterator.next()).thenThrow(new NoSuchElementException("no more elements"));

    // Inject the iterator into stack[stackSize - 1] (stack[0])
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = iterator;

    // Spy on jsonTreeReader to do nothing on expect
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doNothing().when(spyReader).expect(JsonToken.NAME);

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    Exception thrown = assertThrows(Exception.class, () -> nextNameMethod.invoke(spyReader, false));
    // InvocationTargetException wraps the actual exception
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NoSuchElementException);
    assertEquals("no more elements", cause.getMessage());
  }
}