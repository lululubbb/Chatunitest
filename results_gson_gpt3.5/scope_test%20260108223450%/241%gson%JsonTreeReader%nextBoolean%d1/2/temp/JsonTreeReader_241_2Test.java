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

public class JsonTreeReader_241_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonPrimitive boolean true element to pass to constructor
    JsonPrimitive truePrimitive = new JsonPrimitive(true);
    reader = new JsonTreeReader(truePrimitive);

    // Use reflection to set stack and stackSize because constructor sets them
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = truePrimitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Also set pathIndices to default zero values
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(reader, pathIndices);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_true() throws Exception {
    // Set stack with a JsonPrimitive boolean true and stackSize > 0
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    JsonPrimitive boolPrimitive = new JsonPrimitive(true);
    stack[0] = boolPrimitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    // Also set pathIndices to test increment
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndicesField.set(reader, pathIndices);

    // Call nextBoolean and verify result
    boolean result = reader.nextBoolean();
    assertTrue(result);

    // Verify pathIndices incremented
    int[] updatedPathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(1, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_false() throws Exception {
    // Set stack with JsonPrimitive false
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    JsonPrimitive boolPrimitive = new JsonPrimitive(false);
    stack[0] = boolPrimitive;
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 5;
    pathIndicesField.set(reader, pathIndices);

    boolean result = reader.nextBoolean();
    assertFalse(result);

    int[] updatedPathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(6, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_stackSizeOne_pathIndexIncrement() throws Exception {
    // Set stack with a JsonPrimitive boolean true
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    JsonPrimitive boolPrimitive = new JsonPrimitive(true);
    stack[0] = boolPrimitive;
    stackField.set(reader, stack);

    // Set stackSize to 1 to satisfy expect(JsonToken.BOOLEAN)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1); // must be 1 or more for expect to pass

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 10;
    pathIndicesField.set(reader, pathIndices);

    boolean result = reader.nextBoolean();
    assertTrue(result);

    int[] updatedPathIndices = (int[]) pathIndicesField.get(reader);
    // pathIndices should be incremented because stackSize == 1
    assertEquals(11, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_stackSizeZero_noPathIndexIncrement() throws Exception {
    // Set stack with a JsonPrimitive boolean true
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    JsonPrimitive boolPrimitive = new JsonPrimitive(true);
    stack[0] = boolPrimitive;
    stackField.set(reader, stack);

    // Set stackSize to 0 to test no pathIndices increment
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 0);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndices[0] = 10;
    pathIndicesField.set(reader, pathIndices);

    // We need to push the element so expect(JsonToken.BOOLEAN) passes
    // But since stackSize is 0, nextBoolean should throw or behave differently.
    // However, nextBoolean expects stackSize > 0 to read element.
    // So this test will verify no increment happens if stackSize == 0.

    // Because expect(JsonToken.BOOLEAN) will check peekStack which returns stack[stackSize-1]
    // and stackSize == 0 means peekStack returns null, expect will throw IllegalStateException.
    // So we expect an exception here.

    assertThrows(IllegalStateException.class, () -> {
      reader.nextBoolean();
    });

    int[] updatedPathIndices = (int[]) pathIndicesField.get(reader);
    // pathIndices should not be incremented because nextBoolean threw before incrementing
    assertEquals(10, updatedPathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testNextBoolean_popStackReturnsNonPrimitive_throwsMalformedJsonException() throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = new Object();
    stackField.set(reader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    assertThrows(com.google.gson.stream.MalformedJsonException.class, () -> {
      reader.nextBoolean();
    });
  }
}