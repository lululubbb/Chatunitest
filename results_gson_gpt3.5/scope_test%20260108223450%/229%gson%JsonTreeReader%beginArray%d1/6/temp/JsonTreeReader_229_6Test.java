package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
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
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

class JsonTreeReader_229_6Test {

  private JsonTreeReader jsonTreeReader;
  private JsonArray jsonArray;

  @BeforeEach
  void setUp() throws Exception {
    jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    jsonTreeReader = new JsonTreeReader(jsonArray);

    // Set stack and stackSize manually to simulate internal state before beginArray
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(jsonTreeReader, pathIndices);
  }

  @Test
    @Timeout(8000)
  void beginArray_success_pushesIteratorAndSetsIndex() throws Exception {
    JsonTreeReader spyReader = new JsonTreeReader(jsonArray);

    // Set internal fields as in setUp
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stackField.set(spyReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(spyReader, pathIndices);

    // Use reflection to invoke private expect method to do nothing (skip expect)
    // We do this by temporarily replacing expect with a no-op via reflection on beginArray
    // Since expect is private and cannot be mocked, we invoke beginArray with expect replaced by a no-op:
    // So we invoke expect manually here and skip it in beginArray by temporarily modifying stack to simulate expect success

    // Instead, call beginArray but first modify stack so that peekStack returns the JsonArray and expect won't fail

    // Actually, expect checks the top of stack, so if stack is set correctly, expect won't throw
    // So just call beginArray directly without mocking expect

    spyReader.beginArray();

    // Verify stackSize incremented
    int stackSize = stackSizeField.getInt(spyReader);
    assertEquals(2, stackSize);

    // Verify top of stack is iterator of jsonArray
    stack = (Object[]) stackField.get(spyReader);
    assertNotNull(stack[1]);
    assertTrue(stack[1] instanceof Iterator);

    // Verify pathIndices[stackSize - 1] == 0
    pathIndices = (int[]) pathIndicesField.get(spyReader);
    assertEquals(0, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  void beginArray_expectThrows_throwsIOException() throws Exception {
    JsonTreeReader spyReader = new JsonTreeReader(jsonArray);

    // Set internal fields as in setUp
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = jsonArray;
    stackField.set(spyReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(spyReader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = new int[32];
    pathIndicesField.set(spyReader, pathIndices);

    // Use reflection to get the private expect method
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Use a proxy approach: temporarily replace the top of stack with an invalid element so expect throws
    // Forcing expect to throw by setting top of stack to a wrong type
    stack[0] = "invalid type";

    IOException thrown = assertThrows(IOException.class, spyReader::beginArray);
    assertTrue(thrown.getMessage().contains("Expected BEGIN_ARRAY but was"));
  }
}