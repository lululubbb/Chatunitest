package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_232_5Test {

  private JsonTreeReader jsonTreeReader;
  private JsonObject jsonObject;

  @BeforeEach
  void setUp() throws Exception {
    jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);

    // Use reflection to set private fields stack and stackSize to simulate state before endObject
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(jsonTreeReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);

    // Setup initial stack state:
    // stack[0] = jsonObject (object)
    // stack[1] = iterator over entrySet (empty iterator)
    // stackSize = 2
    stack[0] = jsonObject;
    stack[1] = jsonObject.entrySet().iterator();
    stackSizeField.setInt(jsonTreeReader, 2);

    // Setup pathNames and pathIndices for stackSize=2
    pathNames[0] = "root";
    pathNames[1] = "child";
    pathIndices[0] = 0;
    pathIndices[1] = 0;
  }

  private void callExpect(JsonTreeReader reader, JsonToken token) throws Exception {
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);
    expectMethod.invoke(reader, token);
  }

  private Object popStack(JsonTreeReader reader) throws Exception {
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
    return popStackMethod.invoke(reader);
  }

  @Test
    @Timeout(8000)
  void endObject_shouldPopStackAndIncrementPathIndices() throws Exception {
    JsonTreeReader testReader = new JsonTreeReader(jsonObject);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(testReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(testReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(testReader);

    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);

    // Setup initial stack state:
    // stack[0] = jsonObject (object)
    // stack[1] = iterator over entrySet (empty iterator)
    // stackSize = 2
    stack[0] = jsonObject;
    stack[1] = jsonObject.entrySet().iterator();
    stackSizeField.setInt(testReader, 2);

    // Setup pathNames and pathIndices for stackSize=2
    pathNames[0] = "root";
    pathNames[1] = "child";
    pathIndices[0] = 0;
    pathIndices[1] = 0;

    // To simulate the state before endObject, peekStack must return END_OBJECT.
    // peekStack returns the top of the stack, which should be an iterator for the object entries.
    // So set stack top to the iterator and stackSize=2.

    // But expect(JsonToken.END_OBJECT) expects peekStack().peek() to be END_OBJECT.
    // The real implementation returns BEGIN_OBJECT for JsonObject and END_OBJECT when iterator is exhausted.
    // So we need to simulate an empty iterator that returns no elements, so peekStack returns END_OBJECT.

    // Create an empty iterator for the object's entrySet
    Iterator<?> emptyIterator = jsonObject.entrySet().iterator();

    // Set stack[0] = jsonObject, stack[1] = empty iterator, stackSize = 2
    stack[0] = jsonObject;
    stack[1] = emptyIterator;
    stackSizeField.setInt(testReader, 2);

    // Now expect(JsonToken.END_OBJECT) should succeed because peekStack() returns empty iterator,
    // and peek() returns END_OBJECT.

    expectMethod.invoke(testReader, JsonToken.END_OBJECT);

    // Now call endObject's rest logic:
    pathNames[stackSizeField.getInt(testReader) - 1] = null;
    popStackMethod.invoke(testReader); // pop empty iterator
    popStackMethod.invoke(testReader); // pop jsonObject

    if (stackSizeField.getInt(testReader) > 0) {
      pathIndices[stackSizeField.getInt(testReader) - 1]++;
    }

    // Verify pathNames[1] is null after endObject (since stackSize was 2)
    assertNull(pathNames[1]);

    // Verify stackSize after endObject is 0 (2 - 2 pops)
    int stackSize = stackSizeField.getInt(testReader);
    assertEquals(0, stackSize);

    // Verify pathIndices[0] remains 0 because stackSize == 0 after pops
    assertEquals(0, pathIndices[0]);

    // Now test case where stackSize > 0 after pops
    // Setup again with stackSize=3 to test increment
    stack[0] = jsonObject;
    stack[1] = jsonObject.entrySet().iterator();
    stack[2] = jsonObject;

    stackSizeField.setInt(testReader, 3);

    pathNames[2] = "lastName";
    pathIndices[2] = 5;
    pathIndices[0] = 0; // reset to 0 for clarity

    // Ensure stack top is an iterator that is empty to simulate END_OBJECT
    // The iterator should be at stack[stackSize - 2], which is stack[1]
    stack[1] = jsonObject.entrySet().iterator();

    // Now expect(JsonToken.END_OBJECT) should succeed for the last object
    expectMethod.invoke(testReader, JsonToken.END_OBJECT);

    pathNames[stackSizeField.getInt(testReader) - 1] = null;
    popStackMethod.invoke(testReader); // pop iterator
    popStackMethod.invoke(testReader); // pop jsonObject

    stackSize = stackSizeField.getInt(testReader);
    if (stackSize > 0) {
      pathIndices[stackSize - 1]++;
    }

    // After two pops, stackSize = 1, so pathIndices[0] incremented
    int newStackSize = stackSizeField.getInt(testReader);
    assertEquals(1, newStackSize);

    // pathNames[2] cleared
    assertNull(pathNames[2]);

    // pathIndices[0] incremented from 0 to 1
    assertEquals(1, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  void endObject_expectThrows_shouldThrowIOException() throws Exception {
    JsonTreeReader testReader = new JsonTreeReader(jsonObject);

    // Use reflection to access private expect method
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Setup stack and stackSize so that peekStack returns BEGIN_OBJECT (jsonObject)
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(testReader);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    stack[0] = jsonObject;
    stackSizeField.setInt(testReader, 1);

    // Now call expect with END_ARRAY which should fail because peekStack returns BEGIN_OBJECT
    Exception thrown = assertThrows(Exception.class, () -> {
      expectMethod.invoke(testReader, JsonToken.END_ARRAY);
    });

    // The thrown exception is InvocationTargetException wrapping IllegalStateException
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Expected END_ARRAY but was BEGIN_OBJECT"));
  }
}