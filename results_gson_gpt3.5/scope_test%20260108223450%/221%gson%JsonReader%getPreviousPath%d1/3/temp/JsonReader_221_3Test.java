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

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_221_3Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_reflectsUsePreviousPathTrue() throws Exception {
    // Access private getPath(boolean) method via reflection
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup internal state to simulate a path
    // stackSize = 2
    // stack = {JsonReader.PEEKED_BEGIN_OBJECT, JsonReader.PEEKED_BEGIN_ARRAY}
    // pathNames = {"name1", null}
    // pathIndices = {0, 2}
    setField(jsonReader, "stackSize", 2);
    setField(jsonReader, "stack", new int[] {
        1, // PEEKED_BEGIN_OBJECT
        3, // PEEKED_BEGIN_ARRAY
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    });
    setField(jsonReader, "pathNames", new String[] {
        "name1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
    });
    setField(jsonReader, "pathIndices", new int[] {
        0, 1, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    });

    // Set peeked to PEEKED_BEGIN_ARRAY (3) to simulate correct state
    setField(jsonReader, "peeked", 3);

    // Call getPreviousPath(), which calls getPath(true)
    String previousPath = jsonReader.getPreviousPath();
    assertEquals("$name1[1]", previousPath);

    // Directly call getPath(true) via reflection and verify same result
    String pathFromReflection = (String) getPathMethod.invoke(jsonReader, true);
    assertEquals("$name1[1]", pathFromReflection);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_emptyStack_returnsRootPath() {
    // stackSize = 0 means path is "$"
    setField(jsonReader, "stackSize", 0);

    String path = jsonReader.getPreviousPath();
    assertEquals("$", path);
  }

  // Helper method to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper method to get private fields via reflection
  private Object getField(Object target, String fieldName) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}