package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_192_6Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void endObject_peekedIsPeekedEndObject_decrementsStackAndResetsPeeked() throws Exception {
    setField(jsonReader, "peeked", getStaticIntField(JsonReader.class, "PEEKED_END_OBJECT"));
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    jsonReader.endObject();

    assertEquals(1, ((Integer) getField(jsonReader, "stackSize")).intValue());
    assertNull(pathNames[1]);
    assertEquals(1, ((Integer) pathIndices[0]).intValue());
    assertEquals(getStaticIntField(JsonReader.class, "PEEKED_NONE"), ((Integer) getField(jsonReader, "peeked")).intValue());
  }

  @Test
    @Timeout(8000)
  public void endObject_peekedIsPeekedNone_callsDoPeekAndProcessesEndObject() throws Exception {
    setField(jsonReader, "peeked", getStaticIntField(JsonReader.class, "PEEKED_NONE"));
    setField(jsonReader, "stackSize", 2);
    String[] pathNames = new String[32];
    pathNames[1] = "name";
    setField(jsonReader, "pathNames", pathNames);
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    setField(jsonReader, "pathIndices", pathIndices);

    // Spy to override doPeek to return PEEKED_END_OBJECT
    JsonReader spyReader = spy(jsonReader);
    doReturn(getStaticIntField(JsonReader.class, "PEEKED_END_OBJECT")).when(spyReader).doPeek();

    spyReader.endObject();

    assertEquals(1, ((Integer) getField(spyReader, "stackSize")).intValue());
    assertNull(pathNames[1]);
    assertEquals(1, ((Integer) pathIndices[0]).intValue());
    assertEquals(getStaticIntField(JsonReader.class, "PEEKED_NONE"), ((Integer) getField(spyReader, "peeked")).intValue());
  }

  @Test
    @Timeout(8000)
  public void endObject_invalidPeeked_throwsIllegalStateException() throws Exception {
    setField(jsonReader, "peeked", getStaticIntField(JsonReader.class, "PEEKED_BEGIN_OBJECT"));

    // Spy to override peek() and locationString()
    JsonReader spyReader = spy(jsonReader);
    doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();
    doReturn(": at path $").when(spyReader).locationString();

    IllegalStateException ex = assertThrows(IllegalStateException.class, spyReader::endObject);
    assertTrue(ex.getMessage().contains("Expected END_OBJECT but was BEGIN_OBJECT"));
    assertTrue(ex.getMessage().contains(": at path $"));
  }

  // Helper to set private fields via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Helper to get private fields via reflection
  private static <T> T getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    T value = (T) field.get(target);
    return value;
  }

  // Helper to get static int fields via reflection
  private static int getStaticIntField(Class<?> clazz, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(null);
  }
}