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

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonTreeReader_241_1Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonPrimitive with boolean true
    JsonPrimitive truePrimitive = new JsonPrimitive(true);

    // Create instance with a dummy JsonElement (null for now)
    reader = new JsonTreeReader(null);

    // Use reflection to set private fields stack, stackSize, pathIndices
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = truePrimitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndicesArray = new int[32];
    pathIndicesArray[0] = 0;
    pathIndicesField.set(reader, pathIndicesArray);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanReturnsTrueAndIncrementsPathIndex() throws Exception {
    // Initial pathIndices[0] = 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(0, pathIndices[0]);

    // Call nextBoolean
    boolean result = reader.nextBoolean();

    assertTrue(result);

    // Verify stackSize decreased by 1 after popStack()
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);

    // Since stackSize is now 0, pathIndices should NOT be incremented
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanWithStackSizeZeroDoesNotIncrementPathIndex() throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);

    // Setup stack with one boolean JsonPrimitive true
    JsonPrimitive truePrimitive = new JsonPrimitive(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = truePrimitive;
    stackField.set(reader, stackArray);

    // Set stackSize to 1
    stackSizeField.setInt(reader, 1);

    // Set pathIndices[0] to 0
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);

    // Call nextBoolean once - pops the stack, stackSize becomes 0
    boolean result = reader.nextBoolean();
    assertTrue(result);

    // Now stackSize is 0, pathIndices[0] is NOT incremented after popStack() because stackSize == 0
    // So expected value remains 0 here
    assertEquals(0, pathIndices[0]);

    // Prepare for next call: push boolean false and set stackSize to 2 to simulate parent context
    // Because pathIndices increments only if stackSize > 0 after popStack()
    // So to have pathIndices increment, stackSize after popStack must be > 0
    // We simulate stackSize = 2 before nextBoolean call, so after popStack it will be 1 > 0
    stackArray[0] = new JsonPrimitive(true); // set a boolean at bottom to keep stack consistent
    stackArray[1] = new JsonPrimitive(false);
    stackField.set(reader, stackArray);
    stackSizeField.setInt(reader, 2);

    // Set pathIndices[0] and pathIndices[1]
    pathIndices[0] = 0;
    pathIndices[1] = 5;

    // Call nextBoolean again
    boolean result2 = reader.nextBoolean();
    assertFalse(result2);

    // pathIndices[1] should be incremented by 1 (from 5 to 6)
    assertEquals(6, pathIndices[1]);

    // After pop, stackSize should be 1
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(1, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanThrowsIfTopStackIsNotBooleanPrimitive() throws Exception {
    // Setup stack with a JsonPrimitive that is not boolean (string)
    JsonPrimitive stringPrimitive = new JsonPrimitive("notBoolean");
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = stringPrimitive;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // nextBoolean should throw IllegalStateException because expect fails (expected BOOLEAN but was STRING)
    assertThrows(IllegalStateException.class, () -> {
      reader.nextBoolean();
    });
  }
}