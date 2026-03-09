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

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReader_241_6Test {

  private JsonTreeReader reader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonPrimitive boolean true element to initialize the reader
    JsonPrimitive booleanTrue = new JsonPrimitive(true);
    reader = new JsonTreeReader(booleanTrue);

    // Using reflection to set stack and stackSize for controlled test environment
    // stack will contain a JsonPrimitive boolean
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = booleanTrue;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    // Set pathIndices[0] to -1 as initial value to match JsonTreeReader behavior
    pathIndices[0] = -1;
    pathIndicesField.set(reader, pathIndices);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_returnsTrueAndIncrementsPathIndex() throws Exception {
    boolean result = reader.nextBoolean();
    assertTrue(result);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(0, pathIndices[0]); // Expect increment from -1 to 0
  }

  @Test
    @Timeout(8000)
  void nextBoolean_stackSizeZero_noPathIndexIncrement() throws Exception {
    // Prepare a JsonPrimitive false and set stack accordingly
    JsonPrimitive booleanFalse = new JsonPrimitive(false);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = booleanFalse;
    stackField.set(reader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    // Set pathIndices[0] to -1 as initial value to match JsonTreeReader behavior
    pathIndices[0] = -1;
    pathIndicesField.set(reader, pathIndices);

    // Call nextBoolean, should return false
    boolean result = reader.nextBoolean();
    assertFalse(result);

    // After popStack(), stackSize should be 0, so pathIndices not incremented further
    int[] updatedPathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(-1, updatedPathIndices[0]);

    // Also verify stackSize is 0
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  void nextBoolean_expectThrowsIllegalStateException() throws Exception {
    // Create a reader with a non-boolean JsonPrimitive to cause expect to fail
    JsonPrimitive nonBoolean = new JsonPrimitive("notABoolean");
    JsonTreeReader badReader = new JsonTreeReader(nonBoolean);

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stackArray = new Object[32];
    stackArray[0] = nonBoolean;
    stackField.set(badReader, stackArray);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(badReader, 1);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, badReader::nextBoolean);
    assertTrue(thrown.getMessage().contains("Expected BOOLEAN but was STRING"));
  }
}