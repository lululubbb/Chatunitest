package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

class JsonTreeReader_250_1Test {
  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonElement for constructor (can be a JsonNull)
    JsonElement element = JsonNull.INSTANCE;
    jsonTreeReader = new JsonTreeReader(element);

    // Setup stack and stackSize fields for testing promoteNameToValue
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 0); // set to 0 initially

    // Setup pathNames and pathIndices (not used here but required for completeness)
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    pathNamesField.set(jsonTreeReader, new String[32]);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(jsonTreeReader, new int[32]);
  }

  private void setStackAndSize(Object[] stack, int size) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  private int getStackSize() throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    return stackSizeField.getInt(jsonTreeReader);
  }

  private Object[] getStack() throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (Object[]) stackField.get(jsonTreeReader);
  }

  private void push(Object obj) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);

    stack[stackSize] = obj;
    stackSizeField.setInt(jsonTreeReader, stackSize + 1);
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, size);
  }

  @Test
    @Timeout(8000)
  void promoteNameToValue_shouldPromoteNameToValue() throws Exception {
    // Prepare a Map.Entry mock with key and value
    Map.Entry<String, JsonElement> entry = mock(Map.Entry.class);
    when(entry.getKey()).thenReturn("mockKey");
    JsonPrimitive valuePrimitive = new JsonPrimitive("mockValue");
    when(entry.getValue()).thenReturn(valuePrimitive);

    // Prepare an Iterator mock that returns the entry once and then no more
    Iterator<Map.Entry<String, JsonElement>> iterator = mock(Iterator.class);
    when(iterator.next()).thenReturn(entry);
    when(iterator.hasNext()).thenReturn(true).thenReturn(false);

    // Inject the iterator into the stack at top with stackSize = 1
    Object[] stack = new Object[32];
    stack[0] = iterator;
    setStackAndSize(stack, 1);

    // Also set pathIndices[stackSize-1] = 0 to avoid ArrayIndexOutOfBoundsException in peek()
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;

    // Call promoteNameToValue by reflection; expect will run normally
    Method promoteNameToValueMethod = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValueMethod.setAccessible(true);
    promoteNameToValueMethod.invoke(jsonTreeReader);

    // After promoteNameToValue, stackSize should be 3 (iterator, value, key)
    int stackSize = getStackSize();
    assertEquals(3, stackSize);

    // The top of the stack should be a JsonPrimitive with key "mockKey"
    Object[] updatedStack = getStack();
    Object top = updatedStack[stackSize - 1];
    assertTrue(top instanceof JsonPrimitive);
    assertEquals("mockKey", ((JsonPrimitive) top).getAsString());

    // The second from top should be the valuePrimitive
    Object second = updatedStack[stackSize - 2];
    assertSame(valuePrimitive, second);

    // The third from top is still the iterator
    Object third = updatedStack[stackSize - 3];
    assertSame(iterator, third);
  }

  @Test
    @Timeout(8000)
  void promoteNameToValue_expectThrowsIOException() throws Exception {
    // Prepare a stack that will cause expect(JsonToken.NAME) to fail
    // For example, put an element that is not an iterator on top of the stack

    Object[] stack = new Object[32];
    stack[0] = new Object(); // non-iterator to cause expect to fail
    setStackAndSize(stack, 1);

    // Also set pathIndices[stackSize-1] = 0 to avoid ArrayIndexOutOfBoundsException in peek()
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;

    Method promoteNameToValueMethod = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValueMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        promoteNameToValueMethod.invoke(jsonTreeReader);
      } catch (Exception e) {
        // unwrap InvocationTargetException
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw e;
      }
    });
    // The message may vary depending on expect implementation, so just check it's an IOException
    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void promoteNameToValue_iteratorNextThrowsNoSuchElementException() throws Exception {
    // Prepare an Iterator mock that throws NoSuchElementException on next()
    Iterator<Map.Entry<String, JsonElement>> iterator = mock(Iterator.class);
    when(iterator.next()).thenThrow(new NoSuchElementException());
    when(iterator.hasNext()).thenReturn(true);

    // Inject the iterator into the stack at top with stackSize = 1
    Object[] stack = new Object[32];
    stack[0] = iterator;
    setStackAndSize(stack, 1);

    // Also set pathIndices[stackSize-1] = 0 to avoid ArrayIndexOutOfBoundsException in peek()
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;

    Method promoteNameToValueMethod = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValueMethod.setAccessible(true);

    // Calling promoteNameToValue will throw NoSuchElementException from iterator.next()
    NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
      try {
        promoteNameToValueMethod.invoke(jsonTreeReader);
      } catch (Exception e) {
        // unwrap InvocationTargetException
        Throwable cause = e.getCause();
        if (cause instanceof NoSuchElementException) {
          throw (NoSuchElementException) cause;
        }
        throw e;
      }
    });

    assertNotNull(thrown);
  }
}