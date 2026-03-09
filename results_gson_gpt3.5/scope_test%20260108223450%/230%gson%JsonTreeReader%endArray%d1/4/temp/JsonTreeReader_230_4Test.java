package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;

public class JsonTreeReader_230_4Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Provide a proper JsonArray as root element to avoid MalformedJsonException
    JsonArray rootArray = new JsonArray();
    reader = new JsonTreeReader(rootArray);

    // Setup internal state for testing endArray
    // stackSize = 2 (to allow stack pops and pathIndices increment)
    setField(reader, "stackSize", 2);

    // Initialize stack with proper objects to avoid MalformedJsonException
    Object[] stack = new Object[32];
    // stack[0] = iterator over rootArray (empty iterator)
    stack[0] = Collections.emptyIterator();
    // stack[1] = rootArray itself (Array)
    stack[1] = rootArray;
    setField(reader, "stack", stack);

    // Initialize pathIndices with zeroes
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    setField(reader, "pathIndices", pathIndices);
  }

  @Test
    @Timeout(8000)
  public void testEndArray_normal() throws Exception {
    // Setup stack and stackSize again to ensure popStack works
    JsonArray rootArray = new JsonArray();
    Object[] stack = new Object[32];
    // Use empty iterator to make peek() return END_ARRAY
    stack[0] = Collections.emptyIterator();
    stack[1] = rootArray;
    setField(reader, "stack", stack);
    setField(reader, "stackSize", 2);

    // Initialize pathIndices array
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 0;
    setField(reader, "pathIndices", pathIndices);

    // No need to exhaust iterator since it's empty

    // Call endArray via reflection
    invokePrivateVoidMethod(reader, "endArray");

    // Verify pathIndices[stackSize - 1] incremented by 1
    int[] updatedPathIndices = getField(reader, "pathIndices");
    int stackSize = getField(reader, "stackSize");
    assertEquals(1, updatedPathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testEndArray_stackSizeZero_noIncrement() throws Exception {
    // Set stackSize to 0 to test no increment branch
    setField(reader, "stackSize", 0);

    // Setup stack with dummy objects so expect does not fail immediately
    Object[] stack = new Object[32];
    JsonArray ja = new JsonArray();
    stack[0] = ja.iterator();
    stack[1] = ja;
    setField(reader, "stack", stack);

    // Initialize pathIndices with zeros
    int[] pathIndices = new int[32];
    setField(reader, "pathIndices", pathIndices);

    // We expect IllegalStateException because stackSize=0 means no array to end
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      invokePrivateVoidMethod(reader, "endArray");
    });

    assertNotNull(thrown);

    // After exception, pathIndices should still be zeros
    int[] updatedPathIndices = getField(reader, "pathIndices");
    for (int i = 0; i < updatedPathIndices.length; i++) {
      assertEquals(0, updatedPathIndices[i]);
    }
  }

  @Test
    @Timeout(8000)
  public void testEndArray_expectThrows() throws Exception {
    // Create a new JsonTreeReader with empty JsonArray to avoid MalformedJsonException
    JsonTreeReader throwingReader = new JsonTreeReader(new JsonArray());

    // Setup stack and stackSize so popStack would work if called
    JsonArray rootArray = new JsonArray();
    Object[] stack = new Object[32];
    stack[0] = Collections.emptyIterator();
    stack[1] = new Object(); // corrupted stack element to cause expect to fail
    setField(throwingReader, "stack", stack);
    setField(throwingReader, "stackSize", 2);

    // expect should throw when called with wrong token
    Exception thrown = assertThrows(IllegalStateException.class, () -> {
      invokePrivateVoidMethod(throwingReader, "expect", JsonToken.BEGIN_OBJECT);
    });

    assertNotNull(thrown);

    // Now test that endArray throws as well by invoking endArray with corrupted stack
    IllegalStateException thrown2 = assertThrows(IllegalStateException.class, () -> {
      invokePrivateVoidMethod(throwingReader, "endArray");
    });

    assertNotNull(thrown2);
  }

  // Helper to get private field via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String name) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(name);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  // Helper to set private field via reflection
  private void setField(Object target, String name, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(name);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to invoke private void method with one parameter
  private void invokePrivateVoidMethod(Object target, String methodName, Object param) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod(methodName, param.getClass());
    method.setAccessible(true);
    method.invoke(target, param);
  }

  // Helper to invoke private void method with no parameters
  private void invokePrivateVoidMethod(Object target, String methodName) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod(methodName);
    method.setAccessible(true);
    method.invoke(target);
  }
}