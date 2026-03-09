package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

class JsonTreeReader_230_5Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonArray with two elements
    JsonArray array = new JsonArray();
    array.add(new JsonPrimitive("one"));
    array.add(new JsonPrimitive("two"));

    // Create the JsonTreeReader instance with the array
    reader = new JsonTreeReader(array);

    // Begin the array to set up internal stack properly
    reader.beginArray();

    // Consume first element to advance iterator
    reader.nextString();
  }

  @Test
    @Timeout(8000)
  void testEndArray() throws Exception {
    // Before endArray call, stackSize and pathIndices should be set accordingly
    // Use reflection to access private fields
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    int beforeStackSize = (int) stackSizeField.get(reader);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    int beforePathIndex = pathIndices[beforeStackSize - 1];

    // Consume second element to advance iterator to end of array
    reader.nextString();

    // Now call endArray which should expect END_ARRAY, pop two stack elements, and increment pathIndices if stackSize > 0
    reader.endArray();

    int afterStackSize = (int) stackSizeField.get(reader);

    // If stackSize > 0 after popping, pathIndices[stackSize - 1] should be incremented by 1
    int afterPathIndex = afterStackSize > 0 ? pathIndices[afterStackSize - 1] : -1;

    // After endArray, stackSize should have decreased by 2
    assertEquals(beforeStackSize - 2, afterStackSize);

    if (afterStackSize > 0) {
      assertEquals(beforePathIndex + 1, afterPathIndex);
    }
  }

  @Test
    @Timeout(8000)
  void testEndArrayThrowsOnWrongToken() throws Exception {
    // Create a JsonArray with no elements so next peek token is END_ARRAY
    JsonArray array = new JsonArray();
    JsonTreeReader localReader = new JsonTreeReader(array);
    localReader.beginArray();

    // Consume the empty array by calling endArray once to pop the array from stack
    localReader.endArray();

    // Use reflection to access private fields
    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    Object[] stack = (Object[]) stackField.get(localReader);
    int stackSize = (int) stackSizeField.get(localReader);

    // Push a new JsonArray without calling beginArray
    JsonArray newArray = new JsonArray();
    stack[stackSize] = newArray;
    stackSizeField.set(localReader, stackSize + 1);

    // Reset pathIndices for the new stack size to avoid unexpected increments
    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(localReader);
    pathIndices[stackSize] = 0;

    // Now calling endArray should throw IllegalStateException due to token mismatch
    IllegalStateException exception = assertThrows(IllegalStateException.class, localReader::endArray);
    assertTrue(exception.getMessage().contains("Expected END_ARRAY but was BEGIN_ARRAY"));
  }
}