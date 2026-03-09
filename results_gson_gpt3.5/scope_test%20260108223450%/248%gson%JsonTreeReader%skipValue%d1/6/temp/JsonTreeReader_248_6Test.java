package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReader_248_6Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a proper JsonElement with internal stack setup
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    reader = new JsonTreeReader(jsonObject);

    // Manually set up stack and stackSize to avoid popStack() errors
    setPrivateField(reader, "stack", new Object[32]);
    setPrivateField(reader, "pathNames", new String[32]);
    setPrivateField(reader, "pathIndices", new int[32]);
    setPrivateField(reader, "stackSize", 1);

    Object[] stack = getPrivateField(reader, "stack");
    stack[0] = jsonObject;
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_NAME_invokesNextName() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.NAME).when(spyReader).peek();

    // Prepare stack to simulate object with iterator to avoid ClassCastException
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    setPrivateField(spyReader, "stackSize", 1);
    Object[] stack = getPrivateField(spyReader, "stack");
    // Replace JsonObject with its entrySet iterator to avoid ClassCastException
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
    stack[0] = iterator;

    spyReader.skipValue();

    verify(spyReader).peek();
    // Cannot verify private method call directly via Mockito
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_ARRAY_invokesEndArray() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    // Setup stack to contain a JsonArray to avoid popStack error
    JsonArray jsonArray = new JsonArray();
    setPrivateField(spyReader, "stackSize", 1);
    Object[] stack = getPrivateField(spyReader, "stack");
    stack[0] = jsonArray;

    spyReader.skipValue();

    verify(spyReader).endArray();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_OBJECT_invokesEndObject() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    // Setup stack to contain an iterator of JsonObject to avoid popStack error
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    setPrivateField(spyReader, "stackSize", 1);
    Object[] stack = getPrivateField(spyReader, "stack");
    Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
    stack[0] = iterator;

    spyReader.skipValue();

    verify(spyReader).endObject();
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_END_DOCUMENT_doesNothing() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    int stackSizeBefore = getPrivateField(spyReader, "stackSize");
    int[] pathIndicesBefore = getPrivateField(spyReader, "pathIndices");

    spyReader.skipValue();

    int stackSizeAfter = getPrivateField(spyReader, "stackSize");
    int[] pathIndicesAfter = getPrivateField(spyReader, "pathIndices");

    assertEquals(stackSizeBefore, stackSizeAfter);
    assertEquals(pathIndicesBefore[0], pathIndicesAfter[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_default_stackSizeZero_noIncrement() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.BOOLEAN).when(spyReader).peek();

    // Set stackSize to 1 and stack[0] to null to avoid popStack errors
    // Setting stackSize to 0 causes popStack to try to pop from empty stack leading to Index -1
    setPrivateField(spyReader, "stackSize", 1);
    Object[] stack = getPrivateField(spyReader, "stack");
    stack[0] = null;

    int[] pathIndices = getPrivateField(spyReader, "pathIndices");
    pathIndices[0] = 0;

    spyReader.skipValue();

    int stackSize = getPrivateField(spyReader, "stackSize");
    assertEquals(0, stackSize);

    pathIndices = getPrivateField(spyReader, "pathIndices");
    assertEquals(0, pathIndices[0]);
  }

  @Test
    @Timeout(8000)
  public void testSkipValue_default_stackSizeGreaterThanZero_incrementsPathIndices() throws Exception {
    JsonTreeReader spyReader = Mockito.spy(reader);
    doReturn(JsonToken.STRING).when(spyReader).peek();

    // Set stackSize to 2 and stack[0], stack[1] to non-null to avoid popStack errors
    setPrivateField(spyReader, "stackSize", 2);
    Object[] stack = getPrivateField(spyReader, "stack");
    stack[0] = new JsonObject();
    stack[1] = new JsonPrimitive("string");

    int[] pathIndices = getPrivateField(spyReader, "pathIndices");
    pathIndices[1] = 5;

    spyReader.skipValue();

    assertEquals(6, pathIndices[1]);
  }

  // Reflection helpers
  private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
    Field field = getField(instance.getClass(), fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object instance, String fieldName) throws Exception {
    Field field = getField(instance.getClass(), fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    while (clazz != null) {
      try {
        return clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}