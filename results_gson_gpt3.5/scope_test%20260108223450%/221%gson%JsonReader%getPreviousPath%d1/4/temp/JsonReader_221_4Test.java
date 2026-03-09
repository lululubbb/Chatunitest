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

public class JsonReader_221_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void testGetPreviousPath_invokesGetPathWithTrue() throws Exception {
    // Use reflection to get private getPath method
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Spy on jsonReader to verify getPath(true) is called by getPreviousPath()
    JsonReader spyReader = spy(jsonReader);

    // Stub getPath(boolean) to return a known string when called with true
    doAnswer(invocation -> {
      boolean arg = invocation.getArgument(0);
      if (arg) {
        return "previousPathValue";
      } else {
        // call real method for false argument
        return getPathMethod.invoke(spyReader, arg);
      }
    }).when(spyReader).getPath(anyBoolean());

    // Call getPreviousPath and assert it returns stubbed value
    String result = spyReader.getPreviousPath();
    assertEquals("previousPathValue", result);

    // Verify getPath(true) was called exactly once using Mockito's verify with argument matcher
    verify(spyReader, times(1)).getPath(true);
  }

  @Test
    @Timeout(8000)
  void testGetPath_usePreviousPathTrue_returnsPathString() throws Exception {
    // Use reflection to access private getPath(boolean) method
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // Setup internal state to simulate some path
    // stackSize = 2
    // stack = {PEEKED_BEGIN_OBJECT, PEEKED_BEGIN_ARRAY}
    // pathNames = {"name1", "name2"}
    // pathIndices = {3, 4}
    // We will set these fields via reflection

    setField(jsonReader, "stackSize", 2);
    setField(jsonReader, "stack", new int[] {1, 3}); // 1 = PEEKED_BEGIN_OBJECT, 3 = PEEKED_BEGIN_ARRAY
    setField(jsonReader, "pathNames", new String[] {"name1", "name2"});
    setField(jsonReader, "pathIndices", new int[] {3, 4});

    // Invoke getPath(true)
    String path = (String) getPathMethod.invoke(jsonReader, true);

    // Expected path string: "$.name1[4]"
    // Explanation:
    // stack[0] = 1 (object) -> pathNames[0] = "name1" -> .name1
    // stack[1] = 3 (array) -> pathIndices[1] = 4 -> [4]
    // root is "$"
    assertEquals("$.name1[4]", path);
  }

  @Test
    @Timeout(8000)
  void testGetPath_usePreviousPathFalse_emptyStack_returnsRoot() throws Exception {
    Method getPathMethod = JsonReader.class.getDeclaredMethod("getPath", boolean.class);
    getPathMethod.setAccessible(true);

    // stackSize = 0
    setField(jsonReader, "stackSize", 0);

    String path = (String) getPathMethod.invoke(jsonReader, false);

    assertEquals("$", path);
  }

  // Helper method to set private fields via reflection
  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}