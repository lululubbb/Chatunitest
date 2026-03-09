package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class JsonTreeReader_239_2Test {

  private JsonTreeReader reader;

  @BeforeEach
  public void setUp() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));
    reader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  public void testNextName_returnsFirstName() throws Exception {
    // beginObject to position reader to object start
    reader.beginObject();

    // invoke private nextName(boolean) with skipName = false
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);
    String name = (String) nextNameMethod.invoke(reader, false);

    assertEquals("key1", name);

    // Consume the value to move the reader forward to allow nextName calls later
    reader.nextString();
  }

  @Test
    @Timeout(8000)
  public void testNextName_skipNameTrue_returnsNameWithoutAdvancing() throws Exception {
    reader.beginObject();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    // Call with skipName = true, should return same name but not advance pathNames
    String name1 = (String) nextNameMethod.invoke(reader, true);
    // Do not call nextName again with skipName=true without consuming the value first.
    // Instead, call nextName with skipName=false to advance.
    reader.nextString();
    String name2 = (String) nextNameMethod.invoke(reader, true);

    assertEquals("key1", name1);
    assertEquals("key2", name2);

    // Consume the value to advance for cleanup
    reader.nextString();
  }

  @Test
    @Timeout(8000)
  public void testNextName_afterNextNameAdvancesToNextName() throws Exception {
    reader.beginObject();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    String firstName = (String) nextNameMethod.invoke(reader, false);
    // Consume the value to advance the reader position
    reader.nextString();

    String secondName = (String) nextNameMethod.invoke(reader, false);

    assertEquals("key1", firstName);
    assertEquals("key2", secondName);
  }

  @Test
    @Timeout(8000)
  public void testNextName_throwsIfNotObject() throws Exception {
    // The root element is an object, so to test beginArray failure, use an array element.
    JsonObject root = new JsonObject();
    root.add("array", new JsonArray());
    JsonTreeReader arrayReader = new JsonTreeReader(root.get("array"));

    arrayReader.beginArray();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      nextNameMethod.invoke(arrayReader, false);
    });

    // InvocationTargetException wraps IllegalStateException in this case
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testNextName_emptyObjectThrows() throws Exception {
    JsonObject emptyObject = new JsonObject();
    JsonTreeReader emptyReader = new JsonTreeReader(emptyObject);
    emptyReader.beginObject();

    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      nextNameMethod.invoke(emptyReader, false);
    });

    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NoSuchElementException);
  }
}