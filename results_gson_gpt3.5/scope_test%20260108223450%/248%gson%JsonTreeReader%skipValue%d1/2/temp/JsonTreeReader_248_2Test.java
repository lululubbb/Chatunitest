package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_248_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // We create a JsonElement to pass to the constructor.
    JsonObject obj = new JsonObject();
    obj.add("key", new JsonPrimitive("value"));
    reader = new JsonTreeReader(obj);

    // Initialize stack and related fields to avoid ArrayIndexOutOfBoundsException
    setPrivateField(reader, "stack", new Object[32]);
    setPrivateField(reader, "pathIndices", new int[32]);
    setPrivateField(reader, "pathNames", new String[32]);
    setPrivateField(reader, "stackSize", 0);
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsName_shouldCallNextName() throws Exception {
    // Arrange
    setStackAndStackSize(JsonToken.NAME);

    // Spy on reader to verify nextName(true) call (with skipName)
    JsonTreeReader spyReader = spy(reader);

    doReturn(JsonToken.NAME).when(spyReader).peek();

    // To fix ClassCastException, we must set the top of the stack to an Iterator for NAME
    // Because nextName(true) expects the top of the stack to be an Iterator over Map.Entry
    setStackTopToIterator(spyReader);

    // nextName(boolean) is private, so we verify via skipValue calling it by spying on skipValue and verifying call to nextName(true)
    // Instead of mocking nextName, we can spy on skipValue and verify nextName(true) is called by spying on skipValue and using doCallRealMethod
    // But since nextName(boolean) is private, we cannot mock it directly.
    // So we verify skipValue calls nextName(true) by spying on skipValue and verifying nextName() (public) is called.
    // But public nextName() calls nextName(true) internally.
    // So we mock nextName() to call real method, and verify it is called.

    doCallRealMethod().when(spyReader).nextName();

    // Act
    spyReader.skipValue();

    // Assert
    verify(spyReader, times(1)).nextName();
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsEndArray_shouldCallEndArray() throws Exception {
    // Arrange
    setStackAndStackSize(JsonToken.END_ARRAY);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    doNothing().when(spyReader).endArray();

    // Act
    spyReader.skipValue();

    // Assert
    verify(spyReader, times(1)).endArray();
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsEndObject_shouldCallEndObject() throws Exception {
    // Arrange
    setStackAndStackSize(JsonToken.END_OBJECT);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    doNothing().when(spyReader).endObject();

    // Act
    spyReader.skipValue();

    // Assert
    verify(spyReader, times(1)).endObject();
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsEndDocument_shouldDoNothing() throws Exception {
    // Arrange
    setStackAndStackSize(JsonToken.END_DOCUMENT);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    // Act
    spyReader.skipValue();

    // Assert no exception and no method calls
    verify(spyReader, never()).endArray();
    verify(spyReader, never()).endObject();
    verify(spyReader, never()).nextName();
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsOther_shouldPopStackAndIncrementPathIndices() throws Exception {
    // Arrange
    // Push a dummy element so stackSize > 0 to allow incrementing pathIndices
    setStackAndStackSize(JsonToken.STRING, 2);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    int[] pathIndicesBefore = getPathIndices(spyReader);
    int stackSizeBefore = getStackSize(spyReader);
    int originalIndex = pathIndicesBefore[stackSizeBefore - 1];

    // Act
    spyReader.skipValue();

    // Assert pathIndices[stackSize - 1] incremented by 1
    int[] pathIndicesAfter = getPathIndices(spyReader);
    int stackSizeAfter = getStackSize(spyReader);
    assertEquals(stackSizeBefore - 1, stackSizeAfter);
    assertEquals(originalIndex + 1, pathIndicesAfter[stackSizeAfter - 1]);
  }

  @Test
    @Timeout(8000)
  public void skipValue_whenPeekIsOtherAndStackSizeZero_shouldPopStackButNotIncrement() throws Exception {
    // Arrange
    // Push one element so popStack() can pop without error
    setStackAndStackSize(JsonToken.STRING, 1);

    // Then forcibly set stackSize to 0 to simulate empty stack before skipValue
    setPrivateField(reader, "stackSize", 0);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    int[] pathIndicesBefore = getPathIndices(spyReader);

    // Act
    spyReader.skipValue();

    // Assert pathIndices not incremented because stackSize == 0
    int[] pathIndicesAfter = getPathIndices(spyReader);
    for (int i = 0; i < pathIndicesAfter.length; i++) {
      assertEquals(pathIndicesBefore[i], pathIndicesAfter[i]);
    }
  }

  // Helper to set stack and stackSize with a peeked token on top
  private void setStackAndStackSize(JsonToken token) throws Exception {
    setStackAndStackSize(token, 1);
  }

  private void setStackAndStackSize(JsonToken token, int size) throws Exception {
    // Use reflection to set private fields stack and stackSize
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    // Clear stack first
    setPrivateField(reader, "stackSize", 0);
    setPrivateField(reader, "stack", new Object[32]);
    setPrivateField(reader, "pathIndices", new int[32]);
    setPrivateField(reader, "pathNames", new String[32]);

    for (int i = 0; i < size - 1; i++) {
      pushMethod.invoke(reader, JsonNull.INSTANCE);
    }
    if (size > 0) {
      pushMethod.invoke(reader, token);
    }
    setPrivateField(reader, "stackSize", size);
  }

  // Helper to replace top of stack with an Iterator to fix ClassCastException for NAME token
  private void setStackTopToIterator(JsonTreeReader spyReader) throws Exception {
    // Create a Map.Entry iterator corresponding to the "key" in the JsonObject
    JsonObject obj = new JsonObject();
    obj.add("key", new JsonPrimitive("value"));
    Iterator<Map.Entry<String, com.google.gson.JsonElement>> iterator = obj.entrySet().iterator();

    // Replace the top of the stack with this iterator
    Object[] stack = (Object[]) getPrivateField(spyReader, "stack");
    int stackSize = (int) getPrivateField(spyReader, "stackSize");
    if (stackSize > 0) {
      stack[stackSize - 1] = iterator;
    } else {
      // If stackSize == 0, push the iterator to stack and set stackSize to 1
      stack[0] = iterator;
      setPrivateField(spyReader, "stackSize", 1);
    }
  }

  private int[] getPathIndices(Object obj) throws Exception {
    return (int[]) getPrivateField(obj, "pathIndices");
  }

  private int getStackSize(Object obj) throws Exception {
    return (int) getPrivateField(obj, "stackSize");
  }

  private Object getPrivateField(Object obj, String fieldName) throws Exception {
    var field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }

  private void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
    var field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }
}