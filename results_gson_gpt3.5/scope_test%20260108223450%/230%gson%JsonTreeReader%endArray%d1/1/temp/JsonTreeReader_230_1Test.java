package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

public class JsonTreeReader_230_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonArray with some elements for the JsonTreeReader
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("one"));
    jsonArray.add(new JsonPrimitive("two"));

    // Instantiate JsonTreeReader with the JsonArray
    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Use reflection to push an iterator of the array and the array itself on the stack
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Clear stack first to avoid residual state
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 0);

    // Push the array itself first (bottom)
    pushMethod.invoke(jsonTreeReader, jsonArray);
    // Push the iterator (simulate beginArray call)
    pushMethod.invoke(jsonTreeReader, jsonArray.iterator());

    // Set stackSize to 2 to reflect the two elements pushed
    stackSizeField.setInt(jsonTreeReader, 2);

    // Initialize pathIndices array at index 0 and 1 to 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 0;
    pathIndices[1] = 0;
  }

  @Test
    @Timeout(8000)
  public void testEndArray_normal() throws Exception {
    // Call endArray normally, expecting stackSize to decrease by 2 and pathIndices[0] incremented

    // Before calling endArray, check stackSize and pathIndices
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int beforeStackSize = stackSizeField.getInt(jsonTreeReader);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    int beforePathIndex = pathIndices[beforeStackSize - 2]; // pathIndices corresponds to array, so index before popping

    // Move iterator forward by calling next() to consume first element
    // This is necessary so that peek() returns END_ARRAY instead of STRING
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    Object top = peekStackMethod.invoke(jsonTreeReader);
    if (top instanceof Iterator) {
      Iterator<?> iterator = (Iterator<?>) top;
      if (iterator.hasNext()) {
        iterator.next();
      }
    }

    // Invoke endArray
    jsonTreeReader.endArray();

    // After calling endArray, stackSize should be decreased by 2
    int afterStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(beforeStackSize - 2, afterStackSize);

    // pathIndices at new top (stackSize-1) should be incremented by 1 if stackSize > 0
    if (afterStackSize > 0) {
      int afterPathIndex = pathIndices[afterStackSize - 1];
      assertEquals(beforePathIndex + 1, afterPathIndex);
    }
  }

  @Test
    @Timeout(8000)
  public void testEndArray_stackSizeZero_noIncrement() throws Exception {
    // Setup: push an array and its iterator, then endArray to empty stack, pathIndices should not increment

    // Create a JsonArray with one element
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("dummy"));

    // Reset jsonTreeReader with new JsonArray
    jsonTreeReader = new JsonTreeReader(jsonArray);

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Clear stack and push array and iterator
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 0);

    pushMethod.invoke(jsonTreeReader, jsonArray);
    pushMethod.invoke(jsonTreeReader, jsonArray.iterator());
    stackSizeField.setInt(jsonTreeReader, 2);

    // Initialize pathIndices
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = 5;
    pathIndices[1] = 0;

    // Move iterator forward by calling next() to consume first element
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    Object top = peekStackMethod.invoke(jsonTreeReader);
    if (top instanceof Iterator) {
      Iterator<?> iterator = (Iterator<?>) top;
      if (iterator.hasNext()) {
        iterator.next();
      }
    }

    // Call endArray - this should pop iterator and array, making stackSize 0
    jsonTreeReader.endArray();

    // After endArray, stackSize should be 0
    int afterStackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(0, afterStackSize);

    // pathIndices should not be incremented since stackSize == 0
    assertEquals(5, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testEndArray_expectThrows() throws Exception {
    // Create an empty JsonTreeReader with empty JsonArray to cause expect to throw IllegalStateException

    JsonArray emptyArray = new JsonArray();
    JsonTreeReader failingReader = new JsonTreeReader(emptyArray);

    // Clear stack to ensure expect fails expecting END_ARRAY but stack is empty
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(failingReader, 0);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, failingReader::endArray);
    assertNotNull(thrown.getMessage());
  }
}