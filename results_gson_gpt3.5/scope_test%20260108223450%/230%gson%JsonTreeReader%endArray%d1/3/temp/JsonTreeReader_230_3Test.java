package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_230_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Prepare a JsonArray element for the constructor
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("one"));
    jsonArray.add(new JsonPrimitive("two"));

    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Using reflection to set stack and stackSize to simulate state before endArray call
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);

    // Prepare stack with two elements: an iterator and the JsonArray
    Iterator<JsonElement> iterator = jsonArray.iterator();
    stack[0] = iterator;
    stack[1] = jsonArray;
    stackSizeField.setInt(jsonTreeReader, 2);

    // Prepare pathIndices with some initial values
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndices[1] = 1;

    // Prepare pathNames with some dummy values
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);
    pathNames[0] = "array1";
    pathNames[1] = "array2";
  }

  @Test
    @Timeout(8000)
  void endArray_shouldPopStackAndIncrementPathIndices() throws Exception {
    // Spy on jsonTreeReader
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Use reflection to get the private expect method
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Mock the private expect method by overriding it via doAnswer and reflection
    doAnswer(invocation -> {
      expectMethod.invoke(spyReader, invocation.getArgument(0));
      return null;
    }).when(spyReader).endArray(); // This won't work directly because endArray is public and calls expect internally

    // Instead, we use a workaround: mock expect by replacing it with a no-op via reflection proxy

    // To mock private method expect, we use a dynamic proxy approach: replace the method via reflection
    // But since it's private, Mockito cannot mock it directly.
    // Instead, we use reflection to replace the method temporarily with a no-op.

    // So instead of mocking expect, we use reflection to replace it with a no-op:
    // But Java doesn't allow method replacement easily.
    // So the better approach is to use reflection to invoke endArray without mocking expect,
    // or to suppress expect by temporarily making it accessible and invoking endArray.

    // Since the test requires mocking expect to avoid its internal logic,
    // we can use a subclass to override expect.

    // Create subclass to override expect:
    class JsonTreeReaderMock extends JsonTreeReader {
      JsonTreeReaderMock(JsonElement element) {
        super(element);
      }
      @Override
      protected void expect(JsonToken expected) {
        // no-op
      }
    }
    // Create instance of subclass and set fields same as spyReader
    JsonTreeReaderMock mockReader = new JsonTreeReaderMock(jsonTreeReader.stack[1] instanceof JsonArray ? (JsonArray) jsonTreeReader.stack[1] : new JsonArray());
    // Copy internal state from jsonTreeReader to mockReader
    copyInternalState(jsonTreeReader, mockReader);

    // Setup stack and stackSize for mockReader to simulate state before endArray call
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(mockReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Prepare stack with two elements: an iterator and the JsonArray
    JsonArray jsonArray = (JsonArray) stack[1];
    if (jsonArray == null) {
      jsonArray = new JsonArray();
      jsonArray.add(new JsonPrimitive("one"));
      jsonArray.add(new JsonPrimitive("two"));
      stack[1] = jsonArray;
    }
    Iterator<JsonElement> iterator = jsonArray.iterator();
    stack[0] = iterator;
    stackSizeField.setInt(mockReader, 2);

    int[] pathIndices = (int[]) pathIndicesField.get(mockReader);
    pathIndices[0] = 0;
    pathIndices[1] = 1;

    // Before calling endArray, verify stackSize and pathIndices
    int beforeStackSize = stackSizeField.getInt(mockReader);
    int beforePathIndex = pathIndices[beforeStackSize - 1];

    // Call endArray
    mockReader.endArray();

    // After calling endArray, stackSize should decrease by 2
    int afterStackSize = stackSizeField.getInt(mockReader);
    assertEquals(beforeStackSize - 2, afterStackSize);

    // pathIndices at new top (after pop) should be incremented by 1
    if (afterStackSize > 0) {
      int afterPathIndex = pathIndices[afterStackSize - 1];
      assertEquals(beforePathIndex + 1, afterPathIndex);
    }
  }

  @Test
    @Timeout(8000)
  void endArray_shouldNotIncrementPathIndicesWhenStackSizeZero() throws Exception {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Setup stack with two elements to pop, then after pop stackSize will be 0
    stack[0] = jsonArray.iterator();
    stack[1] = jsonArray;
    stackSizeField.setInt(reader, 2);

    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = 5;

    // Use subclass to override expect
    class JsonTreeReaderMock extends JsonTreeReader {
      JsonTreeReaderMock(JsonElement element) {
        super(element);
      }
      @Override
      protected void expect(JsonToken expected) {
        // no-op
      }
    }
    JsonTreeReaderMock mockReader = new JsonTreeReaderMock(jsonArray);
    copyInternalState(reader, mockReader);

    mockReader.endArray();

    // After popping twice from 2, stackSize should be 0
    int afterStackSize = stackSizeField.getInt(mockReader);
    assertEquals(0, afterStackSize);

    // No increment should happen for pathIndices since stackSize == 0
    assertEquals(5, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void endArray_expectThrows_shouldPropagateException() throws Exception {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    stack[0] = jsonArray.iterator();
    stack[1] = jsonArray;
    stackSizeField.setInt(reader, 2);

    // Create subclass to override expect and throw exception
    class JsonTreeReaderMock extends JsonTreeReader {
      JsonTreeReaderMock(JsonElement element) {
        super(element);
      }
      @Override
      protected void expect(JsonToken expected) {
        throw new IllegalStateException("Expected token not found");
      }
    }
    JsonTreeReaderMock mockReader = new JsonTreeReaderMock(jsonArray);
    copyInternalState(reader, mockReader);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, mockReader::endArray);
    assertEquals("Expected token not found", thrown.getMessage());

    // stackSize should remain unchanged since exception thrown before popping
    int afterStackSize = stackSizeField.getInt(mockReader);
    assertEquals(2, afterStackSize);
  }

  // Helper method to copy internal private fields from one JsonTreeReader to another
  private void copyInternalState(JsonTreeReader source, JsonTreeReader target) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");

    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);
    pathIndicesField.setAccessible(true);
    pathNamesField.setAccessible(true);

    stackField.set(target, stackField.get(source));
    stackSizeField.setInt(target, stackSizeField.getInt(source));
    pathIndicesField.set(target, pathIndicesField.get(source));
    pathNamesField.set(target, pathNamesField.get(source));
  }
}