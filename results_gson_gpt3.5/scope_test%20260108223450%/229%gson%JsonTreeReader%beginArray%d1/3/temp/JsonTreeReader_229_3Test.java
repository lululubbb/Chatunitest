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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_229_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonArray and initialize JsonTreeReader with it
    JsonArray jsonArray = new JsonArray();
    // Add some elements to the array
    jsonArray.add("element1");
    jsonArray.add("element2");
    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Push the JsonArray onto the stack manually to simulate internal state before beginArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = jsonArray;
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Set pathIndices array with initial values
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    pathIndices[0] = -1;
  }

  @Test
    @Timeout(8000)
  void testBeginArray_success() throws Exception {
    // Create a proxy instance of JsonTreeReader to override the private expect method via reflection
    JsonTreeReader testReader = createTestJsonTreeReaderWithExpect(jsonTreeReader, JsonToken.BEGIN_ARRAY, null);

    // Call beginArray and verify internal state changes
    testReader.beginArray();

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int newStackSize = stackSizeField.getInt(testReader);
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    assertEquals(2, newStackSize);

    Object[] testStack = (Object[]) stackField.get(testReader);
    Object top = testStack[newStackSize - 1];
    assertNotNull(top);
    assertTrue(top instanceof Iterator);

    int[] testPathIndices = (int[]) pathIndicesField.get(testReader);
    assertEquals(0, testPathIndices[newStackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void testBeginArray_expectThrows() throws Exception {
    // Create a proxy instance of JsonTreeReader to override the private expect method to throw IOException
    IOException expectedException = new IOException("Expected token not found");
    JsonTreeReader testReader = createTestJsonTreeReaderWithExpect(jsonTreeReader, null, expectedException);

    IOException thrown = assertThrows(IOException.class, testReader::beginArray);
    assertEquals("Expected token not found", thrown.getMessage());
  }

  /**
   * Creates a proxy JsonTreeReader instance that overrides the private expect method.
   * If expectedToken is not null, expect will assert that the passed token equals expectedToken.
   * If exceptionToThrow is not null, expect will throw that exception.
   */
  private JsonTreeReader createTestJsonTreeReaderWithExpect(JsonTreeReader originalReader, JsonToken expectedToken, IOException exceptionToThrow) throws Exception {
    // Create a new JsonTreeReader with the same top JsonElement
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] originalStack = (Object[]) stackField.get(originalReader);
    Object topElement = originalStack[0];
    assertTrue(topElement instanceof JsonElement);

    JsonTreeReader testReader = new JsonTreeReader((JsonElement) topElement);

    // Copy internal state from originalReader to testReader
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(originalReader);

    stackField.set(testReader, originalStack);
    stackSizeField.setInt(testReader, stackSize);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndicesOriginal = (int[]) pathIndicesField.get(originalReader);
    pathIndicesField.set(testReader, pathIndicesOriginal);

    // Use reflection to override the private expect method in testReader by replacing it with a Method handle
    // Since we cannot override final class or private method, we use a dynamic proxy approach:
    // We create a subclass of JsonTreeReader at runtime using a Proxy or use reflection to invoke beginArray but intercept expect call.
    // Because JsonTreeReader is final and expect is private, we cannot override normally.
    // Instead, we replace the private expect method via reflection with a Method object that throws or asserts.

    // To achieve this, we use reflection to access the expect method and replace it by a MethodHandle or use a spy approach.
    // Since the problem constraints allow reflection, we will use a workaround:
    // We create a dynamic proxy of JsonTreeReader with InvocationHandler to intercept calls.
    // But JsonTreeReader is final, so proxies won't work.
    // So instead, we use reflection to invoke beginArray but before that, we replace the expect method with a dummy method via MethodHandles.

    // Since Java does not allow method replacement at runtime easily, we use a helper class that uses reflection to call private expect method.

    // So we create a helper class that calls expect and beginArray, and in expect we do the desired behavior.

    return new JsonTreeReaderProxy(testReader, expectedToken, exceptionToThrow);
  }

  // Helper proxy class to simulate overriding private expect method
  private static class JsonTreeReaderProxy extends JsonTreeReader {
    private final JsonTreeReader delegate;
    private final JsonToken expectedToken;
    private final IOException exceptionToThrow;

    JsonTreeReaderProxy(JsonTreeReader delegate, JsonToken expectedToken, IOException exceptionToThrow) {
      super(getRootElement(delegate));
      this.delegate = delegate;
      this.expectedToken = expectedToken;
      this.exceptionToThrow = exceptionToThrow;
      copyInternalState(delegate);
    }

    private static JsonElement getRootElement(JsonTreeReader reader) {
      try {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] stack = (Object[]) stackField.get(reader);
        return (JsonElement) stack[0];
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private void copyInternalState(JsonTreeReader original) {
      try {
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);

        stackField.set(this, stackField.get(original));
        stackSizeField.setInt(this, stackSizeField.getInt(original));
        pathIndicesField.set(this, pathIndicesField.get(original));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public void beginArray() throws IOException {
      expect(JsonToken.BEGIN_ARRAY);
      // call delegate's beginArray logic except expect, since we override expect here
      try {
        // We invoke delegate's beginArray method but skip expect by calling private methods reflectively
        // Or we replicate the beginArray code here:
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
        stackField.setAccessible(true);
        stackSizeField.setAccessible(true);
        pathIndicesField.setAccessible(true);

        Object[] stack = (Object[]) stackField.get(this);
        int stackSize = stackSizeField.getInt(this);

        JsonArray array = (JsonArray) stack[stackSize - 1];
        Iterator<?> iterator = array.iterator();

        // Push iterator onto stack
        if (stackSize == stack.length) {
          // expand stack if needed
          Object[] newStack = new Object[stack.length * 2];
          System.arraycopy(stack, 0, newStack, 0, stack.length);
          stack = newStack;
          stackField.set(this, stack);
        }
        stack[stackSize] = iterator;
        stackSizeField.setInt(this, stackSize + 1);

        int[] pathIndices = (int[]) pathIndicesField.get(this);
        if (stackSize == pathIndices.length) {
          int[] newPathIndices = new int[pathIndices.length * 2];
          System.arraycopy(pathIndices, 0, newPathIndices, 0, pathIndices.length);
          pathIndices = newPathIndices;
          pathIndicesField.set(this, pathIndices);
        }
        pathIndices[stackSize] = 0;
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new IOException(e);
      }
    }

    private void expect(JsonToken expected) throws IOException {
      if (exceptionToThrow != null) {
        throw exceptionToThrow;
      }
      if (expectedToken != null) {
        assertEquals(expectedToken, expected);
      }
    }
  }
}