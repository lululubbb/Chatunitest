package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_248_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create real JsonTreeReader with a non-null JsonElement to avoid NPE in peek()
    // Use JsonObject with one entry so that nextName() can work without ClassCastException
    JsonObject element = new JsonObject();
    element.add("key", new JsonPrimitive("value"));
    jsonTreeReader = Mockito.spy(new JsonTreeReader(element));

    // Initialize stack and stackSize properly to avoid IndexOutOfBounds in popStack
    // We push the root element to stack and set stackSize accordingly
    setField(jsonTreeReader, "stack", new Object[32]);
    setField(jsonTreeReader, "stackSize", 1);
    Object[] stack = (Object[]) getField(jsonTreeReader, "stack");
    stack[0] = element;

    // Initialize pathIndices array
    setField(jsonTreeReader, "pathIndices", new int[32]);

    // Initialize pathNames array to avoid NPE in nextName()
    setField(jsonTreeReader, "pathNames", new String[32]);
  }

  private void invokeSkipValue() throws Throwable {
    Method skipValueMethod = JsonTreeReader.class.getDeclaredMethod("skipValue");
    skipValueMethod.setAccessible(true);
    try {
      skipValueMethod.invoke(jsonTreeReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private String invokeNextName(boolean skipName) throws Throwable {
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);
    try {
      return (String) nextNameMethod.invoke(jsonTreeReader, skipName);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private Object invokePopStack() throws Throwable {
    Method popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
    popStackMethod.setAccessible(true);
    try {
      return popStackMethod.invoke(jsonTreeReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  @Test
    @Timeout(8000)
  public void skipValue_withName_invokesNextName() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.NAME);

    // Setup stack and stackSize to simulate object with iterator at top of stack
    Object[] stack = new Object[32];
    JsonObject obj = new JsonObject();
    obj.add("mockKey", new JsonPrimitive("mockValue"));
    stack[0] = obj;
    // Put an iterator at stack[1] to simulate internal state for nextName()
    Iterator<Map.Entry<String, JsonElement>> iterator = obj.entrySet().iterator();
    stack[1] = iterator;

    setField(jsonTreeReader, "stack", stack);
    setField(jsonTreeReader, "stackSize", 2);

    // Initialize pathNames to avoid NPE
    setField(jsonTreeReader, "pathNames", new String[32]);
    setField(jsonTreeReader, "pathIndices", new int[32]);

    // Spy on jsonTreeReader to mock nextName() (public)
    JsonTreeReader spy = jsonTreeReader;
    doReturn("mockName").when(spy).nextName();

    invokeSkipValue();

    verify(spy).nextName();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndArray_invokesEndArray() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.END_ARRAY);
    doNothing().when(jsonTreeReader).endArray();

    invokeSkipValue();

    verify(jsonTreeReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndObject_invokesEndObject() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.END_OBJECT);
    doNothing().when(jsonTreeReader).endObject();

    invokeSkipValue();

    verify(jsonTreeReader).endObject();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withEndDocument_doesNothing() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    invokeSkipValue();

    verify(jsonTreeReader, never()).nextName();
    verify(jsonTreeReader, never()).endArray();
    verify(jsonTreeReader, never()).endObject();
  }

  @Test
    @Timeout(8000)
  public void skipValue_withDefault_popStackAndIncrementPathIndices() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.BOOLEAN);

    // Setup stackSize and stack properly
    setField(jsonTreeReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    setField(jsonTreeReader, "pathIndices", pathIndices);

    Object[] stack = new Object[32];
    stack[0] = new JsonNull();
    stack[1] = new JsonNull();
    setField(jsonTreeReader, "stack", stack);

    invokeSkipValue();

    assertEquals(1, pathIndices[1], "pathIndices at stackSize-1 should be incremented");
  }

  @Test
    @Timeout(8000)
  public void skipValue_withDefault_popStackAndNoPathIndicesIncrementWhenStackSizeZero() throws Throwable {
    when(jsonTreeReader.peek()).thenReturn(JsonToken.NUMBER);

    setField(jsonTreeReader, "stackSize", 1); // set to 1 to avoid IndexOutOfBounds in popStack
    int[] pathIndices = new int[32];
    setField(jsonTreeReader, "pathIndices", pathIndices);

    Object[] stack = new Object[32];
    stack[0] = new JsonNull();
    setField(jsonTreeReader, "stack", stack);

    invokeSkipValue();

    // pathIndices should be incremented at index 0 because stackSize == 1
    assertEquals(1, pathIndices[0], "pathIndices at stackSize-1 should be incremented");

    // Now test with stackSize=0 and verify no increment and no exception
    setField(jsonTreeReader, "stackSize", 0);
    for (int i = 0; i < pathIndices.length; i++) {
      pathIndices[i] = 0;
    }

    invokeSkipValue();

    for (int i : pathIndices) {
      assertEquals(0, i);
    }
  }
}