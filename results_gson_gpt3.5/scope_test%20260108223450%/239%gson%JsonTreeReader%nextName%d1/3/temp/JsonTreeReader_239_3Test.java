package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeReader_239_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }

  @Test
    @Timeout(8000)
  public void nextName_shouldReturnNextName() throws Exception {
    // beginObject to position on object keys
    jsonTreeReader.beginObject();

    String firstName = jsonTreeReader.nextName();
    assertEquals("key1", firstName);
    jsonTreeReader.skipValue();

    String secondName = jsonTreeReader.nextName();
    assertEquals("key2", secondName);
    jsonTreeReader.skipValue();

    jsonTreeReader.endObject();
  }

  @Test
    @Timeout(8000)
  public void nextName_skipNameTrue_shouldReturnNextName() throws Exception {
    // Use reflection to invoke private nextName(boolean)
    Method nextNameMethod = JsonTreeReader.class.getDeclaredMethod("nextName", boolean.class);
    nextNameMethod.setAccessible(true);

    jsonTreeReader.beginObject();

    String firstName = (String) nextNameMethod.invoke(jsonTreeReader, false);
    assertEquals("key1", firstName);
    jsonTreeReader.skipValue();

    String secondName = (String) nextNameMethod.invoke(jsonTreeReader, false);
    assertEquals("key2", secondName);
    jsonTreeReader.skipValue();

    jsonTreeReader.endObject();
  }

  @Test
    @Timeout(8000)
  public void nextName_shouldThrowOnEndOfObject() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    jsonTreeReader.endObject();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextName());
    assertTrue(thrown.getMessage().contains("Expected NAME but was"));
  }

  @Test
    @Timeout(8000)
  public void nextName_shouldThrowIfNotInObject() throws Exception {
    // The reader is positioned at root JsonObject, but nextName requires the current stack element to be an object.
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeReader.nextName());
    assertTrue(thrown.getMessage().contains("Expected NAME but was"));
  }

  @Test
    @Timeout(8000)
  public void nextName_shouldWorkWithEmptyObject() throws Exception {
    JsonObject emptyObject = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(emptyObject);
    reader.beginObject();
    assertFalse(reader.hasNext());
    IllegalStateException thrown = assertThrows(IllegalStateException.class, reader::nextName);
    assertTrue(thrown.getMessage().contains("Expected NAME but was"));
    reader.endObject();
  }
}