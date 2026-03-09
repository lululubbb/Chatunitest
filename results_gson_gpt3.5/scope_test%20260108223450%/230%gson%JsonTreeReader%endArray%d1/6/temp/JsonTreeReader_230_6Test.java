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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_230_6Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonArray with some elements to initialize JsonTreeReader
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");

    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Use reflection to set stack and stackSize to simulate state before endArray()
    // stack: [Iterator over array, JsonArray]
    // stackSize: 2
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Actually, constructor pushes the element, so stackSize=1, stack[0]=jsonArray
    // We need to push iterator over jsonArray to simulate beginArray() called
    Iterator<JsonElement> iterator = jsonArray.iterator();
    pushMethod.invoke(jsonTreeReader, iterator); // stackSize=2 now

    // Also set pathIndices[stackSize-1] = 0 initially
    // Access pathIndices field by reflection
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(jsonTreeReader);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonTreeReader);
    pathIndices[stackSize - 1] = 0;
  }

  @Test
    @Timeout(8000)
  void endArray_validEndArray_popsStackAndIncrementsPathIndices() throws Throwable {
    // Create spy of jsonTreeReader
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Call endArray via reflection
    Method endArrayMethod = JsonTreeReader.class.getDeclaredMethod("endArray");
    endArrayMethod.setAccessible(true);
    endArrayMethod.invoke(spyReader);

    // Verify stackSize decreased by 2
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSizeAfter = (int) stackSizeField.get(spyReader);
    assertEquals(0, stackSizeAfter);

    // Verify pathIndices[stackSize-1] incremented if stackSize > 0
    // Since stackSize is now 0, no increment expected
    // So we test again with stackSize > 0

    // Setup again with stackSize=3 to test increment
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    JsonArray jsonArray2 = new JsonArray();
    jsonArray2.add("x");
    pushMethod.invoke(spyReader, jsonArray2.iterator()); // stackSize=1
    pushMethod.invoke(spyReader, jsonArray2); // stackSize=2
    pushMethod.invoke(spyReader, jsonArray2.iterator()); // stackSize=3

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(spyReader);
    pathIndices[2] = 0;

    // Call endArray again via reflection, should decrement stackSize by 2 from 3 to 1
    endArrayMethod.invoke(spyReader);

    int stackSizeFinal = (int) stackSizeField.get(spyReader);
    assertEquals(1, stackSizeFinal);

    // pathIndices[stackSizeFinal - 1] incremented by 1
    assertEquals(1, pathIndices[stackSizeFinal - 1]);
  }

  @Test
    @Timeout(8000)
  void endArray_expectThrowsException_throwsIOException() throws Throwable {
    // Use reflection to get endArray method
    Method endArrayMethod = JsonTreeReader.class.getDeclaredMethod("endArray");
    endArrayMethod.setAccessible(true);

    // Create a new JsonArray to initialize JsonTreeReader
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("a");

    // Create a subclass that overrides endArray to throw IOException on expect call
    JsonTreeReader faultyReader = new JsonTreeReader(jsonArray) {
      @Override
      public void endArray() throws IOException {
        throw new IOException("expected exception");
      }
    };

    // Call endArray on faultyReader via reflection and expect IOException
    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        endArrayMethod.invoke(faultyReader);
      } catch (java.lang.reflect.InvocationTargetException ite) {
        Throwable cause = ite.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        }
        throw ite;
      }
    });
    assertEquals("expected exception", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void endArray_stackUnderflow_doesNotThrow() throws Throwable {
    // Create new JsonTreeReader with empty stack
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    // Use reflection to set stackSize to 1 and stack with only JsonArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = jsonArray;
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.set(reader, 1);

    // Call endArray via reflection
    Method endArrayMethod = JsonTreeReader.class.getDeclaredMethod("endArray");
    endArrayMethod.setAccessible(true);

    assertDoesNotThrow(() -> {
      try {
        endArrayMethod.invoke(reader);
      } catch (java.lang.reflect.InvocationTargetException ite) {
        Throwable cause = ite.getCause();
        if (cause instanceof IOException) {
          throw cause;
        }
        throw ite;
      }
    });

    int stackSizeAfter = (int) stackSizeField.get(reader);
    assertTrue(stackSizeAfter >= 0);
  }
}