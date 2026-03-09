package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

public class JsonTreeReader_229_1Test {

  private JsonTreeReader reader;
  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() throws Exception {
    jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    reader = new JsonTreeReader(jsonArray);

    // initialize stack and stackSize to simulate internal state before beginArray call
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[0] = jsonArray;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 1);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    pathIndices[0] = -1; // arbitrary initial value
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldPushIteratorAndSetPathIndex() throws Exception {
    // invoke beginArray
    reader.beginArray();

    // verify that stackSize increased by 1
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(reader);
    assertEquals(2, stackSize);

    // verify top of stack is an Iterator of jsonArray elements
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    Object top = stack[stackSize - 1];
    assertTrue(top instanceof Iterator);

    @SuppressWarnings("unchecked")
    Iterator<JsonElement> iterator = (Iterator<JsonElement>) top;
    assertTrue(iterator.hasNext());
    assertEquals(jsonArray.get(0), iterator.next());

    // verify pathIndices[stackSize - 1] == 0
    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    assertEquals(0, pathIndices[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldThrowIfTopStackIsNotBeginArray() throws Exception {
    JsonTreeReader spyReader = spy(reader);

    // Use reflection to get the private peekStack method
    Method peekStackMethod = JsonTreeReader.class.getDeclaredMethod("peekStack");
    peekStackMethod.setAccessible(true);

    // Use doAnswer to mock the private peekStack method via reflection
    doAnswer(invocation -> "not a JsonArray").when(spyReader).peekStack();

    IOException thrown = assertThrows(IOException.class, spyReader::beginArray);
    assertTrue(thrown.getMessage().contains("Expected BEGIN_ARRAY"));
  }
}