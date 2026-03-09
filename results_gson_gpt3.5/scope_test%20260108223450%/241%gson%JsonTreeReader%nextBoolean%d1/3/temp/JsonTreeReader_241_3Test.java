package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_241_3Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonPrimitive with a boolean true value
    JsonPrimitive jsonBoolean = new JsonPrimitive(true);

    // Instantiate JsonTreeReader with a dummy JsonElement (null here, will inject stack manually)
    reader = new JsonTreeReader(null);

    // Use reflection to set private fields: stack, stackSize, pathIndices
    // stack[0] = jsonBoolean
    Object[] stack = new Object[32];
    stack[0] = jsonBoolean;

    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(reader, stack);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0; // explicitly initialize to 0
    pathIndicesField.set(reader, pathIndices);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_returnsBooleanAndIncrementsPathIndex() throws Exception {
    // Before calling nextBoolean, pathIndices[stackSize - 1] should be 0
    var pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);

    assertEquals(0, pathIndices[stackSize - 1]);

    // Call nextBoolean and check returned value
    boolean result = reader.nextBoolean();
    assertTrue(result);

    // After call, pathIndices[stackSize - 1] should be incremented by 1
    // Note: stackSize is decremented by 1 after popStack(), so retrieve new stackSize
    int newStackSize = stackSizeField.getInt(reader);
    if (newStackSize > 0) {
      assertEquals(1, pathIndices[newStackSize - 1]);
    } else {
      // If stackSize is 0, no pathIndices to check, so just assert stackSize decreased
      assertEquals(stackSize - 1, newStackSize);
    }

    // stackSize should be decremented by 1 because popStack() removes the element
    assertEquals(stackSize - 1, newStackSize);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_throwsIfTokenNotBoolean() throws Exception {
    // Prepare a JsonPrimitive with non-boolean value (e.g. string)
    JsonPrimitive jsonString = new JsonPrimitive("notABoolean");

    // Inject stack with jsonString at top
    Object[] stack = new Object[32];
    stack[0] = jsonString;

    var stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(reader, stack);

    var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Use reflection to invoke private expect(JsonToken) method to simulate the check inside nextBoolean
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Expect to throw IOException (MalformedJsonException is subclass of IOException) because token is not BOOLEAN
    Exception exception = assertThrows(Exception.class, () -> {
      try {
        expectMethod.invoke(reader, JsonToken.BOOLEAN);
      } catch (Exception e) {
        // unwrap InvocationTargetException to throw cause
        Throwable cause = e.getCause();
        if (cause != null) {
          throw cause;
        }
        throw e;
      }
    });
    // assertTrue on the actual exception type (IOException or subclass)
    assertTrue(exception instanceof java.io.IOException || exception.getCause() instanceof java.io.IOException);
  }
}