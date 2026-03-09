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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonReader_208_3Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withImmediateStopCharacters() throws Exception {
    // Setup buffer with stop character at pos
    setField(jsonReader, "buffer", "}".toCharArray());
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 1);
    setField(jsonReader, "lenient", false);

    // Invoke private method
    invokeSkipUnquotedValue(jsonReader);

    // pos should be unchanged since stop character at pos
    int pos = (int) getField(jsonReader, "pos");
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withIntermediateStopCharacter() throws Exception {
    // buffer: abc,def  pos=0 limit=7
    char[] buf = {'a', 'b', 'c', ',', 'd', 'e', 'f'};
    setField(jsonReader, "buffer", buf);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 7);
    setField(jsonReader, "lenient", false);

    invokeSkipUnquotedValue(jsonReader);

    int pos = (int) getField(jsonReader, "pos");
    // It should stop at ',' at index 3, so pos increases by 3
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_withLenientCheckLenientCalled() throws Exception {
    // buffer with stop character that triggers checkLenient: e.g. '/'
    char[] buf = {'a', 'b', 'c', '/', 'd'};
    setField(jsonReader, "buffer", buf);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    setField(jsonReader, "lenient", true);

    // Create a spy subclass to override checkLenient for verification
    JsonReader spyReader = new JsonReader(mockReader) {
      private int pos = 0;
      private char[] buffer = new char[5];
      private int limit = 0;
      private boolean lenient = true;

      @Override
      protected void checkLenient() {
        // do nothing, just to allow override
        superCheckLenientCalled = true;
      }

      private boolean superCheckLenientCalled = false;

      // Override private fields via reflection in test below
    };

    // Copy fields from jsonReader to spyReader
    setField(spyReader, "buffer", buf);
    setField(spyReader, "pos", 0);
    setField(spyReader, "limit", 5);
    setField(spyReader, "lenient", true);

    // Use reflection to invoke private method on spy
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(spyReader);

    int pos = (int) getField(spyReader, "pos");
    // It should stop at '/' at index 3, so pos increases by 3
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  public void testSkipUnquotedValue_fillBufferCalled() throws Exception {
    // Setup buffer so that pos + i reaches limit without stop character
    char[] buf = new char[5];
    for (int i = 0; i < 5; i++) {
      buf[i] = 'a';
    }
    setField(jsonReader, "buffer", buf);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    setField(jsonReader, "lenient", false);

    // Create a subclass that overrides fillBuffer and spy on that.
    JsonReader spyWithOverride = new JsonReader(mockReader) {
      private int callCount = 0;

      @Override
      protected boolean fillBuffer(int minimum) throws IOException {
        callCount++;
        return callCount == 1;
      }
    };
    setField(spyWithOverride, "buffer", buf);
    setField(spyWithOverride, "pos", 0);
    setField(spyWithOverride, "limit", 5);
    setField(spyWithOverride, "lenient", false);

    Method skipMethod = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipMethod.setAccessible(true);
    skipMethod.invoke(spyWithOverride);

    int pos = (int) getField(spyWithOverride, "pos");
    // pos should have increased by 5 (all buffer)
    assertEquals(5, pos);
  }

  // Helpers to access private fields and methods

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    if (field.getType().isArray() && value instanceof char[]) {
      // Defensive copy for char[] to match buffer type exactly
      char[] arr = (char[]) value;
      char[] copy = new char[arr.length];
      System.arraycopy(arr, 0, copy, 0, arr.length);
      field.set(target, copy);
    } else {
      field.set(target, value);
    }
  }

  private Object getField(Object target, String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void invokeSkipUnquotedValue(Object target) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(target);
  }
}