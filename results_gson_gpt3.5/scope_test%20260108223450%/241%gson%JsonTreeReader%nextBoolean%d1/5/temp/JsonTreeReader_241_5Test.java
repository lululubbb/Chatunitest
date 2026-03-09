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

public class JsonTreeReader_241_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonPrimitive with boolean true value
    JsonPrimitive truePrimitive = new JsonPrimitive(true);

    // Instantiate JsonTreeReader with a dummy JsonPrimitive via constructor
    jsonTreeReader = new JsonTreeReader(truePrimitive);

    // Initialize stack and stackSize properly
    setStack(jsonTreeReader, new Object[32]);
    setStackSize(jsonTreeReader, 1);
    setStackElement(jsonTreeReader, 0, truePrimitive);

    // Initialize pathIndices properly
    setPathIndices(jsonTreeReader, new int[32]);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanTrue() throws Exception {
    // Prepare JsonPrimitive with true
    JsonPrimitive truePrimitive = new JsonPrimitive(true);
    jsonTreeReader = new JsonTreeReader(truePrimitive);

    // Initialize stack, stackSize and stack top element properly
    setStack(jsonTreeReader, new Object[32]);
    setStackSize(jsonTreeReader, 1);
    setStackElement(jsonTreeReader, 0, truePrimitive);

    setPathIndices(jsonTreeReader, new int[32]);

    boolean result = jsonTreeReader.nextBoolean();

    assertTrue(result);

    int[] pathIndices = getPathIndices(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanFalse() throws Exception {
    // Prepare JsonPrimitive with false
    JsonPrimitive falsePrimitive = new JsonPrimitive(false);
    jsonTreeReader = new JsonTreeReader(falsePrimitive);

    // Initialize stack, stackSize and stack top element properly
    setStack(jsonTreeReader, new Object[32]);
    setStackSize(jsonTreeReader, 1);
    setStackElement(jsonTreeReader, 0, falsePrimitive);

    setPathIndices(jsonTreeReader, new int[32]);

    boolean result = jsonTreeReader.nextBoolean();

    assertFalse(result);

    int[] pathIndices = getPathIndices(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBooleanStackSizeZero() throws Exception {
    // Prepare JsonPrimitive with true
    JsonPrimitive truePrimitive = new JsonPrimitive(true);
    jsonTreeReader = new JsonTreeReader(truePrimitive);

    // Initialize stack, stackSize and stack top element properly
    setStack(jsonTreeReader, new Object[32]);
    setStackSize(jsonTreeReader, 1);
    setStackElement(jsonTreeReader, 0, truePrimitive);

    setPathIndices(jsonTreeReader, new int[32]);

    // Call nextBoolean
    boolean result = jsonTreeReader.nextBoolean();

    assertTrue(result);

    // After popStack, stackSize will be zero, so pathIndices should not increment
    int[] pathIndices = getPathIndices(jsonTreeReader);
    assertEquals(0, pathIndices[0]);
  }

  // Helper methods for reflection

  private static void setStackSize(JsonTreeReader reader, int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, size);
  }

  private static void setPathIndices(JsonTreeReader reader, int[] pathIndices) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    pathIndicesField.set(reader, pathIndices);
  }

  private static int[] getPathIndices(JsonTreeReader reader) throws Exception {
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    return (int[]) pathIndicesField.get(reader);
  }

  private static void setStack(JsonTreeReader reader, Object[] stack) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(reader, stack);
  }

  private static void setStackElement(JsonTreeReader reader, int index, Object element) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[index] = element;
  }
}