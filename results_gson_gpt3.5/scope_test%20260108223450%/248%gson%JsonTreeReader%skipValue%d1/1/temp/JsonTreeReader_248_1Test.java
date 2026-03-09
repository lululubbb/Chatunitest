package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_248_1Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a dummy JsonElement for constructor
    JsonElement element = new JsonNull();
    reader = new JsonTreeReader(element);

    // Initialize stack array and pathIndices array properly for all tests
    // Clear stack and pathIndices to avoid stale data
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
    }
    stackField.set(reader, stack);

    Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
    pathIndicesField.setAccessible(true);
    int[] pathIndices = (int[]) pathIndicesField.get(reader);
    for (int i = 0; i < pathIndices.length; i++) {
      pathIndices[i] = 0;
    }
    pathIndicesField.set(reader, pathIndices);

    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(reader);
    for (int i = 0; i < pathNames.length; i++) {
      pathNames[i] = null;
    }
    pathNamesField.set(reader, pathNames);

    // Reset stackSize to 0
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, 0);
  }

  @Test
    @Timeout(8000)
  public void skipValue_withName_shouldCallNextName() throws Exception {
    setStackSize(1);
    // Instead of JsonObject, use an Iterator<Map.Entry<String, JsonElement>> to avoid ClassCastException
    // Create a dummy iterator to put on the stack
    Iterator<Map.Entry<String, JsonElement>> dummyIterator = new Iterator<Map.Entry<String, JsonElement>>() {
      @Override
      public boolean hasNext() { return false; }
      @Override
      public Map.Entry<String, JsonElement> next() { return null; }
    };
    setStackTop(dummyIterator);

    // Spy on reader
    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    // We cannot mock private nextName(boolean) directly.
    // Instead, set stack and stackSize so nextName(boolean) does not throw NPE.
    // Also mock public nextName() to return dummyName for verification.

    // Set pathNames[stackSize - 1] to some non-null value to avoid NPE in nextName(boolean)
    setPathNameAt(spyReader, 0, "dummyPathName");

    doReturn("dummyName").when(spyReader).nextName();

    spyReader.skipValue();

    verify(spyReader).nextName();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndArray_shouldCallEndArray() throws Exception {
    setStackSize(1);
    setStackTop(new JsonArray());

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();
    doNothing().when(spyReader).endArray();

    spyReader.skipValue();

    verify(spyReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndObject_shouldCallEndObject() throws Exception {
    setStackSize(1);
    setStackTop(new JsonObject());

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();
    doNothing().when(spyReader).endObject();

    spyReader.skipValue();

    verify(spyReader).endObject();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndDocument_shouldDoNothing() throws Exception {
    setStackSize(1);
    setStackTop(JsonNull.INSTANCE);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    spyReader.skipValue();

    // No exception and no method calls expected beyond peek()
    verify(spyReader).peek();
    verify(spyReader, never()).endArray();
    verify(spyReader, never()).endObject();
    verify(spyReader, never()).nextName();
  }

  @Test
    @Timeout(8000)
  public void skipValue_defaultCase_stackSizeGreaterThanZero_incrementsPathIndices() throws Exception {
    setStackSize(2);
    setStackTop(new JsonPrimitive("value"));

    // Also set stack[1] to a dummy element to avoid popStack popping null and causing issues
    setStackAt(1, new JsonPrimitive("dummy"));

    // Set pathNames[1] to non-null to avoid NPE in popStack or skipValue internals
    setPathNameAt(reader, 1, "dummyPathName");

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");
    int before = pathIndices[1];

    spyReader.skipValue();

    int after = pathIndices[1];
    assertEquals(before + 1, after);
  }

  @Test
    @Timeout(8000)
  public void skipValue_defaultCase_stackSizeZero_doesNotIncrementPathIndices() throws Exception {
    setStackSize(0);

    JsonTreeReader spyReader = spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    int[] pathIndices = (int[]) getField(spyReader, "pathIndices");

    spyReader.skipValue();

    // Since stackSize == 0, no increment should happen
    // Just verify no exception and pathIndices unchanged
    for (int v : pathIndices) {
      assertEquals(0, v);
    }
  }

  // Helpers to set private fields via reflection

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(reader, size);
  }

  private void setStackTop(Object value) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    // Clear all stack elements to avoid stale references
    for (int i = 0; i < stack.length; i++) {
      stack[i] = null;
    }
    // Set first element to value
    stack[0] = value;
    stackField.set(reader, stack);
  }

  private void setStackAt(int index, Object value) throws Exception {
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(reader);
    stack[index] = value;
    stackField.set(reader, stack);
  }

  private void setPathNameAt(Object obj, int index, String value) throws Exception {
    Field pathNamesField = JsonTreeReader.class.getDeclaredField("pathNames");
    pathNamesField.setAccessible(true);
    String[] pathNames = (String[]) pathNamesField.get(obj);
    pathNames[index] = value;
    pathNamesField.set(obj, pathNames);
  }

  private Object getField(Object obj, String fieldName) throws Exception {
    Field f = JsonTreeReader.class.getDeclaredField(fieldName);
    f.setAccessible(true);
    return f.get(obj);
  }
}