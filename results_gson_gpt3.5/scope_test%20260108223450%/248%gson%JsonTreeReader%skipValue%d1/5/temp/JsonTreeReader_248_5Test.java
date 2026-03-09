package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class JsonTreeReader_248_5Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement to initialize JsonTreeReader
    JsonElement element = new JsonPrimitive("test");
    jsonTreeReader = new JsonTreeReader(element);

    // Initialize stack and stackSize properly to avoid ArrayIndexOutOfBoundsException
    // Push the root element to stack and set stackSize to 1
    setField(jsonTreeReader, "stack", new Object[32]);
    setField(jsonTreeReader, "pathNames", new String[32]);
    setField(jsonTreeReader, "pathIndices", new int[32]);
    setField(jsonTreeReader, "stackSize", 1);
    Object[] stack = (Object[]) getField(jsonTreeReader, "stack");
    stack[0] = element;
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_NAME() throws Exception {
    // Arrange
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Setup stack to contain an object with iterator for nextName to work
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    setField(spyReader, "stack", new Object[32]);
    setField(spyReader, "stackSize", 2);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);
    Object[] stack = (Object[]) getField(spyReader, "stack");
    stack[0] = jsonObject;
    stack[1] = jsonObject.entrySet().iterator();

    // Use doAnswer to return JsonToken.NAME only once, then call real method
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.NAME;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();

    // nextName(boolean) is private, verify the public nextName() was called instead
    verify(spyReader).nextName();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_ARRAY() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // Return END_ARRAY once, then call real method to avoid multiple peek() invocations returning mocked value
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.END_ARRAY;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    // Setup stack to contain an array and its iterator for endArray to work properly
    JsonArray jsonArray = new JsonArray();
    setField(spyReader, "stack", new Object[32]);
    setField(spyReader, "stackSize", 2);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);
    Object[] stack = (Object[]) getField(spyReader, "stack");
    stack[0] = jsonArray;
    stack[1] = jsonArray.iterator();

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();
    verify(spyReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_OBJECT() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // Return END_OBJECT once, then call real method
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.END_OBJECT;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    // Setup stack to contain an object and its iterator for endObject to work properly
    JsonObject jsonObject = new JsonObject();
    setField(spyReader, "stack", new Object[32]);
    setField(spyReader, "stackSize", 2);
    setField(spyReader, "pathIndices", new int[32]);
    setField(spyReader, "pathNames", new String[32]);
    Object[] stack = (Object[]) getField(spyReader, "stack");
    stack[0] = jsonObject;
    stack[1] = jsonObject.entrySet().iterator();

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();
    verify(spyReader).endObject();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_DOCUMENT() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // Return END_DOCUMENT once, then call real method
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.END_DOCUMENT;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();

    // popStack() is private, cannot verify directly, so verify no other interactions except peek()
    verify(spyReader, never()).endArray();
    verify(spyReader, never()).endObject();
    verify(spyReader, never()).nextName();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_DefaultStackSizeZero() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // Return STRING once, then call real method
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.STRING;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    // Set stackSize to 1 and stack[0] to a JsonPrimitive to avoid popStack error
    setField(spyReader, "stackSize", 1);
    Object[] stack = new Object[32];
    stack[0] = new JsonPrimitive("value");
    setField(spyReader, "stack", stack);

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();

    // popStack() is private, cannot verify directly
    // Instead, verify stackSize remains 0 after skipValue
    int stackSize = (int) getField(spyReader, "stackSize");
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_DefaultStackSizePositive() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // Return BOOLEAN once, then call real method
    doAnswer(new org.mockito.stubbing.Answer<JsonToken>() {
      private boolean first = true;
      @Override
      public JsonToken answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        if (first) {
          first = false;
          return JsonToken.BOOLEAN;
        }
        return (JsonToken) invocation.callRealMethod();
      }
    }).when(spyReader).peek();

    // Set stackSize to 2 and pathIndices array with known values
    setField(spyReader, "stackSize", 2);
    int[] pathIndices = new int[32];
    pathIndices[1] = 5;
    setField(spyReader, "pathIndices", pathIndices);

    // Set stack with JsonPrimitive elements to avoid popStack error
    Object[] stack = new Object[32];
    stack[0] = new JsonPrimitive("val1");
    stack[1] = new JsonPrimitive("val2");
    setField(spyReader, "stack", stack);

    spyReader.skipValue();

    verify(spyReader, atLeastOnce()).peek();

    int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
    assertEquals(6, updatedPathIndices[1]);
  }

  // Helper method to set private fields via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = getDeclaredFieldIncludingSuperclass(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper method to get private fields via reflection
  private static Object getField(Object target, String fieldName) throws Exception {
    java.lang.reflect.Field field = getDeclaredFieldIncludingSuperclass(target.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static java.lang.reflect.Field getDeclaredFieldIncludingSuperclass(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}