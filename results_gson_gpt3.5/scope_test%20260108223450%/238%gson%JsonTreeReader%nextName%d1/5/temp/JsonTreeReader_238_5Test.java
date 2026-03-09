package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_238_5Test {

  private JsonTreeReader jsonTreeReader;
  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  void nextName_shouldReturnNameAndPushValue_whenSkipNameFalse() throws Throwable {
    // Setup JSON object with one entry
    jsonObject.addProperty("key1", "value1");

    // Use reflection to push the internal stack manually to simulate state before nextName call
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonTreeReader, jsonObject.entrySet().iterator());

    // Set stackSize = 1 to simulate the stack having one element
    setField(jsonTreeReader, "stackSize", 1);

    // Set pathNames array to a new String[32]
    setField(jsonTreeReader, "pathNames", new String[32]);

    // Spy on jsonTreeReader
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);

    // Stub private expect(JsonToken) method to do nothing via reflection and Mockito
    doNothingOnPrivateVoidMethod(spyReader, "expect", JsonToken.class);

    // Use reflection to invoke private nextName(boolean)
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(spyReader, false);

    assertEquals("key1", result);

    // Verify pathNames updated correctly
    String[] pathNames = (String[]) getField(spyReader, "pathNames");
    assertEquals("key1", pathNames[0]);

    // Verify stackSize increased to 2
    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(2, stackSize);
  }

  @Test
    @Timeout(8000)
  void nextName_shouldReturnSkippedNameAndPushValue_whenSkipNameTrue() throws Throwable {
    // Setup JSON object with one entry
    jsonObject.addProperty("key2", "value2");

    // Use reflection to push the internal stack manually to simulate state before nextName call
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonTreeReader, jsonObject.entrySet().iterator());

    // Set stackSize = 1 to simulate the stack having one element
    setField(jsonTreeReader, "stackSize", 1);

    // Set pathNames array to a new String[32]
    setField(jsonTreeReader, "pathNames", new String[32]);

    // Spy on jsonTreeReader
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);

    // Stub private expect(JsonToken) method to do nothing via reflection and Mockito
    doNothingOnPrivateVoidMethod(spyReader, "expect", JsonToken.class);

    // Use reflection to invoke private nextName(boolean)
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String result = (String) nextNameMethod.invoke(spyReader, true);

    assertEquals("key2", result);

    // Verify pathNames updated correctly with "<skipped>"
    String[] pathNames = (String[]) getField(spyReader, "pathNames");
    assertEquals("<skipped>", pathNames[0]);

    // Verify stackSize increased to 2
    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(2, stackSize);
  }

  // Helper method to set private field via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper method to get private field via reflection
  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  // Helper method to stub private void method with one argument using reflection and Mockito
  private void doNothingOnPrivateVoidMethod(Object spy, String methodName, Class<?> paramType) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod(methodName, paramType);
    method.setAccessible(true);

    // Use Mockito to stub the private method via spy and doAnswer with reflection proxy
    // Since the method is private, we use Mockito's doAnswer on spy and invoke the method via reflection in the Answer
    doAnswer(invocation -> {
      // do nothing
      return null;
    }).when(spy).getClass()
      .getDeclaredMethod(methodName, paramType)
      .invoke(spy, any(paramType));
  }
}