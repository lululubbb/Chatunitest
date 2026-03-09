package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonReader_221_1Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_invokesGetPathWithTrue() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Spy on jsonReader
    JsonReader spyReader = spy(jsonReader);

    // Stub private getPath(boolean) directly via doAnswer on getPath(boolean)
    // Because getPreviousPath calls getPath(true) internally
    doAnswer(invocation -> {
      Boolean arg = invocation.getArgument(0);
      if (arg) {
        return "previousPathValue";
      } else {
        return getPathMethod.invoke(invocation.getMock(), arg);
      }
    }).when(spyReader, "getPath", boolean.class).withArguments(anyBoolean());

    // Call getPreviousPath which calls getPath(true)
    String result = spyReader.getPreviousPath();

    assertEquals("previousPathValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_true_returnsPathString() throws Exception {
    // We will set fields to simulate a path and invoke private getPath(true)
    // Setup stackSize and pathNames/pathIndices
    setField(jsonReader, "stackSize", 3);
    setField(jsonReader, "pathNames", new String[] {"", "name1", "name2"});
    setField(jsonReader, "pathIndices", new int[] {0, 1, 2});
    setField(jsonReader, "stack", new int[] {
      getPrivateStaticIntField("PEEKED_BEGIN_OBJECT"),
      getPrivateStaticIntField("PEEKED_BEGIN_ARRAY"),
      getPrivateStaticIntField("PEEKED_BEGIN_OBJECT")
    });

    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(jsonReader, true);

    // The path should start with $ and include names and indices according to stack
    // Expected: "$.name1[1].name2"
    assertTrue(path.startsWith("$"));
    assertTrue(path.contains("name1"));
    assertTrue(path.contains("name2"));
    assertTrue(path.contains("[1]"));
  }

  @Test
    @Timeout(8000)
  public void testGetPath_false_returnsPathString() throws Exception {
    // Setup stackSize and pathNames/pathIndices
    setField(jsonReader, "stackSize", 2);
    setField(jsonReader, "pathNames", new String[] {"", "name1"});
    setField(jsonReader, "pathIndices", new int[] {0, 5});
    setField(jsonReader, "stack", new int[] {
      getPrivateStaticIntField("PEEKED_BEGIN_OBJECT"),
      getPrivateStaticIntField("PEEKED_BEGIN_ARRAY")
    });

    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(jsonReader, false);

    // The path should start with $ and include names and indices according to stack
    // Expected: "$.name1[5]"
    assertTrue(path.startsWith("$"));
    assertTrue(path.contains("name1"));
    assertTrue(path.contains("[5]"));
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private int getPrivateStaticIntField(String fieldName) {
    try {
      Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}