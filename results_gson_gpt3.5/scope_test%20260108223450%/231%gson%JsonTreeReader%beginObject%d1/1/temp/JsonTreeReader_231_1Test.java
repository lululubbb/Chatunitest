package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_231_1Test {

  private JsonTreeReader jsonTreeReader;
  private JsonObject jsonObject;

  @BeforeEach
  void setUp() throws Exception {
    // Create a JsonObject with some entries to use in the test
    jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");

    // Instantiate JsonTreeReader with the JsonObject
    jsonTreeReader = new JsonTreeReader(jsonObject);

    // Use reflection to set stack and stackSize to simulate internal state
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = new Object[32];
    stack[0] = jsonObject; // push JsonObject on stack to pass expect check
    stackField.set(jsonTreeReader, stack);

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);
  }

  @Test
    @Timeout(8000)
  void beginObject_correctlyPushesIterator() throws Exception {
    // Use reflection to invoke private peekStack method and verify it returns jsonObject
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);
    Object peeked = peekStackMethod.invoke(jsonTreeReader);
    assertSame(jsonObject, peeked);

    // Call beginObject - should not throw and should push the iterator of jsonObject.entrySet()
    jsonTreeReader.beginObject();

    // Verify stackSize incremented by 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonTreeReader);
    assertEquals(2, stackSize);

    // Verify top of stack is an Iterator of the jsonObject.entrySet()
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof Iterator);

    // Verify iterator iterates over the jsonObject's entrySet
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<String, JsonElement>> iterator = (Iterator<Map.Entry<String, JsonElement>>) top;
    assertTrue(iterator.hasNext());
    Map.Entry<String, JsonElement> entry = iterator.next();
    assertTrue(jsonObject.entrySet().contains(entry));
  }

  @Test
    @Timeout(8000)
  void beginObject_expectThrowsIfTopNotBeginObject() throws Exception {
    // Change top of stack to something other than JsonObject to cause expect failure
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = "NotAJsonObject"; // wrong object to trigger expect failure

    // Reset stackSize to 1 to be consistent
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // beginObject should throw IOException due to expect failure
    IOException thrown = assertThrows(IOException.class, () -> jsonTreeReader.beginObject());
    assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT"));
  }
}