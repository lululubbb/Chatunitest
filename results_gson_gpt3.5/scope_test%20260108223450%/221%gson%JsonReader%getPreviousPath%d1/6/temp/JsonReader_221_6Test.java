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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_221_6Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_invokesGetPathWithTrue() throws Throwable {
    // Use reflection to get the private getPath method
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Invoke getPreviousPath()
    String pathFromGetPreviousPath = jsonReader.getPreviousPath();

    // Invoke getPath(true) directly via reflection
    String pathFromGetPath = (String) getPathMethod.invoke(jsonReader, true);

    // Both should be equal
    assertEquals(pathFromGetPath, pathFromGetPreviousPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPath_usePreviousPathTrueAndFalse() throws Throwable {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Test with usePreviousPath = true
    String pathTrue = (String) getPathMethod.invoke(jsonReader, true);
    assertNotNull(pathTrue);

    // Test with usePreviousPath = false
    String pathFalse = (String) getPathMethod.invoke(jsonReader, false);
    assertNotNull(pathFalse);

    // They may be equal or not depending on internal state, but both should be strings
    assertTrue(pathTrue instanceof String);
    assertTrue(pathFalse instanceof String);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_emptyStack() throws Throwable {
    // Clear stackSize to 0 to simulate no path
    setField(jsonReader, "stackSize", 0);

    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(jsonReader, true);
    // Should at least return "$"
    assertEquals("$", path);

    String previousPath = jsonReader.getPreviousPath();
    assertEquals("$", previousPath);
  }

  @Test
    @Timeout(8000)
  public void testGetPreviousPath_withStackAndPathNamesAndPathIndices() throws Throwable {
    // Setup stackSize = 3
    setField(jsonReader, "stackSize", 3);

    // Setup stack array with arbitrary values (e.g. PEEKED_BEGIN_OBJECT, PEEKED_BEGIN_ARRAY, etc)
    setField(jsonReader, "stack", new int[] {
      1, // PEEKED_BEGIN_OBJECT
      3, // PEEKED_BEGIN_ARRAY
      3  // PEEKED_BEGIN_ARRAY (changed from 4 to 3 to reflect array context)
    });

    // Setup pathNames with some names
    String[] pathNames = new String[32];
    pathNames[0] = "obj";
    pathNames[1] = "arr";
    pathNames[2] = "nestedArr";
    setField(jsonReader, "pathNames", pathNames);

    // Setup pathIndices with some indices
    int[] pathIndices = new int[32];
    pathIndices[0] = 0;
    pathIndices[1] = 2;
    pathIndices[2] = 1;
    setField(jsonReader, "pathIndices", pathIndices);

    // Set pathIndices[1] and pathIndices[2] > 0 to ensure array indices are rendered as [index]
    // (Already done above)

    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    String path = (String) getPathMethod.invoke(jsonReader, true);

    // The path should start with "$" and contain the names and indices appropriately
    assertTrue(path.startsWith("$"));

    // Check that each pathName appears in the path
    assertTrue(path.contains("obj"));
    assertTrue(path.contains("arr"));
    assertTrue(path.contains("nestedArr"));

    // Check that the indices are properly reflected in the path
    // For arrays, path should contain [2] and [1]
    // The first path element "obj" is an object, so no index expected there
    // The second and third are array elements, so indices should be reflected
    assertTrue(path.contains("[2]"));
    assertTrue(path.contains("[1]"));
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}