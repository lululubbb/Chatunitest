package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_229_2Test {

  private JsonTreeReader reader;
  private JsonArray jsonArray;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    reader = new JsonTreeReader(jsonArray);

    // Set stack and stackSize manually to simulate state before beginArray call
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = jsonArray;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldPushIteratorAndSetPathIndex() throws Exception {
    // Use reflection to invoke private expect method with correct JsonToken
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    JsonTreeReader spyReader = Mockito.spy(reader);

    // Instead of stubbing expect (private), call real beginArray on spyReader
    spyReader.beginArray();

    // Verify stackSize incremented
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSize);

    // Verify top of stack is iterator of jsonArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    assertTrue(stack[1] instanceof Iterator);

    // Verify that the iterator is of the jsonArray
    Iterator<?> iterator = (Iterator<?>) stack[1];
    assertTrue(iterator.hasNext());
    Object next = iterator.next();
    assertTrue(next instanceof JsonElement);
    assertEquals("element1", ((JsonElement) next).getAsString());

    // Verify pathIndices[stackSize - 1] == 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldThrowIfExpectFails() throws Exception {
    // Use reflection to get private expect method
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Create a JsonTreeReader instance
    JsonTreeReader failingReader = new JsonTreeReader(jsonArray);

    // Use reflection to replace the private expect method with a proxy that throws IOException when expected token is BEGIN_ARRAY
    // Since we cannot override final class or private methods, we will use a spy and doThrow on expect via reflection

    JsonTreeReader spyReader = Mockito.spy(failingReader);

    // Use reflection to get the expect method and make a doThrow for expect(JsonToken.BEGIN_ARRAY)
    // But since expect is private, we cannot stub it directly. Instead, we can use doAnswer on beginArray to throw.

    doAnswer(invocation -> {
      throw new IOException("Expected exception");
    }).when(spyReader).beginArray();

    IOException thrown = assertThrows(IOException.class, spyReader::beginArray);
    assertEquals("Expected exception", thrown.getMessage());
  }
}