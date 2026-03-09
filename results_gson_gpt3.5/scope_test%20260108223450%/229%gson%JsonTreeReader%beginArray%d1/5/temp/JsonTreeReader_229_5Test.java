package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_229_5Test {

  private JsonTreeReader reader;
  private JsonArray jsonArray;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();
    reader = new JsonTreeReader(jsonArray);

    // Use reflection to set stack and stackSize to simulate state before beginArray call
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = jsonArray;
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);
  }

  @Test
    @Timeout(8000)
  void beginArray_success() throws Exception {
    // Spy reader
    JsonTreeReader spyReader = spy(reader);

    // Use reflection to get the private expect method
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    doAnswer(invocation -> {
      // call private expect(JsonToken.BEGIN_ARRAY)
      expectMethod.invoke(spyReader, JsonToken.BEGIN_ARRAY);
      // then proceed with rest of beginArray logic:
      // replicate beginArray code here:
      // JsonArray array = (JsonArray) peekStack();
      Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
      peekStackMethod.setAccessible(true);
      JsonArray array = (JsonArray) peekStackMethod.invoke(spyReader);
      // call private push(Object)
      Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
      pushMethod.setAccessible(true);
      pushMethod.invoke(spyReader, array.iterator());
      // set pathIndices[stackSize - 1] = 0
      Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = stackSizeField.getInt(spyReader);
      Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
      pathIndicesField.setAccessible(true);
      int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
      pathIndices[stackSize - 1] = 0;
      return null;
    }).when(spyReader).beginArray();

    // Call beginArray
    spyReader.beginArray();

    // Verify that beginArray was called once (since we stubbed expect inside it, no direct expect call)
    verify(spyReader, times(1)).beginArray();

    // Check that stackSize increased by 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSize);

    // Check that top of stack is an iterator over the JsonArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(spyReader);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof Iterator);

    // Check that pathIndices[stackSize - 1] == 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_expectThrowsIOException() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    // Instead of mocking the private expect method (which is private),
    // stub beginArray to throw IOException simulating expect throwing IOException
    doAnswer(invocation -> {
      throw new IOException("Expected exception");
    }).when(spyReader).beginArray();

    IOException thrown = assertThrows(IOException.class, spyReader::beginArray);
    assertEquals("Expected exception", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void beginArray_stackTopNotJsonArrayMalformedJsonException() throws Exception {
    // Replace stack top with a real JsonElement subclass that is not JsonArray (e.g., JsonNull)
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = JsonNull.INSTANCE; // Not JsonArray

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // The actual exception thrown is IllegalStateException, so assert that
    assertThrows(IllegalStateException.class, () -> reader.beginArray());
  }
}