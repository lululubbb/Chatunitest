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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

class JsonTreeReader_238_3Test {

  private JsonTreeReader jsonTreeReader;
  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  void nextName_skipNameFalse_returnsKeyAndUpdatesPathNames() throws Exception {
    // Prepare the stack to simulate the internal state for nextName
    // We need to push an iterator of the JsonObject entrySet to the stack
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();

    // Use reflection to access private fields and methods
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    // Clear stack and push iterator
    setPrivateField(jsonTreeReader, "stackSize", 0);
    setPrivateField(jsonTreeReader, "stack", new Object[32]);
    setPrivateField(jsonTreeReader, "pathNames", new String[32]);

    pushMethod.invoke(jsonTreeReader, iterator);
    setPrivateField(jsonTreeReader, "stackSize", 1);

    // Mock peek() to return JsonToken.NAME so expect(JsonToken.NAME) won't throw
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    // Call nextName with skipName = false
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);
    String result = (String) nextNameMethod.invoke(spyReader, false);

    // Verify the returned key is the first key in the JsonObject
    assertEquals("key1", result);

    // Verify pathNames updated correctly
    String[] pathNames = getPrivateField(spyReader, "pathNames", String[].class);
    assertEquals("key1", pathNames[0]);

    // Verify stackSize incremented (iterator replaced by the value)
    int stackSize = getPrivateField(spyReader, "stackSize", int.class);
    assertEquals(2, stackSize);

    // Verify the top of the stack is the value of the entry
    Object[] stack = getPrivateField(spyReader, "stack", Object[].class);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof JsonElement);
    assertEquals("value1", ((JsonElement) top).getAsString());
  }

  @Test
    @Timeout(8000)
  void nextName_skipNameTrue_returnsKeyAndSetsSkippedInPathNames() throws Exception {
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    setPrivateField(jsonTreeReader, "stackSize", 0);
    setPrivateField(jsonTreeReader, "stack", new Object[32]);
    setPrivateField(jsonTreeReader, "pathNames", new String[32]);

    pushMethod.invoke(jsonTreeReader, iterator);
    setPrivateField(jsonTreeReader, "stackSize", 1);

    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);
    String result = (String) nextNameMethod.invoke(spyReader, true);

    assertEquals("key1", result);

    String[] pathNames = getPrivateField(spyReader, "pathNames", String[].class);
    assertEquals("<skipped>", pathNames[0]);

    int stackSize = getPrivateField(spyReader, "stackSize", int.class);
    assertEquals(2, stackSize);

    Object[] stack = getPrivateField(spyReader, "stack", Object[].class);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof JsonElement);
    assertEquals("value1", ((JsonElement) top).getAsString());
  }

  @Test
    @Timeout(8000)
  void nextName_expectThrowsIOException() throws Exception {
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    setPrivateField(jsonTreeReader, "stackSize", 0);
    setPrivateField(jsonTreeReader, "stack", new Object[32]);
    setPrivateField(jsonTreeReader, "pathNames", new String[32]);

    pushMethod.invoke(jsonTreeReader, iterator);
    setPrivateField(jsonTreeReader, "stackSize", 1);

    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);

    // Return a token that will cause expect(JsonToken.NAME) to throw IOException
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> nextNameMethod.invoke(spyReader, false));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IOException);
    assertEquals("Expected a NAME but was END_DOCUMENT", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void nextName_iteratorNoNext_throwsNoSuchElementException() throws Exception {
    // Create an empty iterator mock that returns false on hasNext and throws on next()
    Iterator<Map.Entry<String, JsonElement>> emptyIterator = mock(Iterator.class);
    when(emptyIterator.hasNext()).thenReturn(false);
    when(emptyIterator.next()).thenThrow(new NoSuchElementException());

    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);

    setPrivateField(jsonTreeReader, "stackSize", 0);
    setPrivateField(jsonTreeReader, "stack", new Object[32]);
    setPrivateField(jsonTreeReader, "pathNames", new String[32]);

    pushMethod.invoke(jsonTreeReader, emptyIterator);
    setPrivateField(jsonTreeReader, "stackSize", 1);

    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
      () -> nextNameMethod.invoke(spyReader, false));
    assertTrue(thrown.getCause() instanceof NoSuchElementException);
  }

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object target, String fieldName, Class<T> clazz) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }
}