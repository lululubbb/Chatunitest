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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonReader_208_4Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_stopsAtSpecialCharacters() throws Exception {
    // Setup buffer with characters including special chars that cause stop
    // buffer[pos + i]: 'a', 'b', 'c', ',', 'd'
    // pos=0, limit=5
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 5);
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = 'c';
    buffer[3] = ',';
    buffer[4] = 'd';
    setField(jsonReader, "buffer", buffer);

    // lenient false to avoid lenient checkLenient() throwing
    jsonReader.setLenient(true);

    // Invoke private method skipUnquotedValue
    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(jsonReader);

    // pos should be advanced to index of ',' which is 3
    int pos = getField(jsonReader, "pos");
    assertEquals(3, pos);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_callsCheckLenientOnSpecialChars() throws Exception {
    // Setup buffer with a '/' which triggers checkLenient and fallthrough to stop
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 2);
    char[] buffer = new char[1024];
    buffer[0] = '/';
    buffer[1] = 'a';
    setField(jsonReader, "buffer", buffer);

    // Spy jsonReader to verify checkLenient call via reflection
    JsonReader spyReader = Mockito.spy(jsonReader);
    spyReader.setLenient(true);

    // We can't verify private method directly, so we use reflection to replace checkLenient with a spy method
    // Instead, we track calls by overriding checkLenient via a subclass or use a flag via reflection

    // Use a flag to track checkLenient call
    Field checkLenientCalledField = JsonReaderTest.class.getDeclaredField("checkLenientCalled");
    checkLenientCalledField.setAccessible(true);
    checkLenientCalledField.setBoolean(this, false);

    // Replace checkLenient with a proxy that sets flag (via reflection)
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // Use a proxy spy: we can't override private method directly, so use Mockito.doAnswer on spyReader for checkLenient
    // But Mockito cannot mock private methods directly, so we use reflection to call skipUnquotedValue and catch exception if any

    // Instead, we invoke skipUnquotedValue and catch if checkLenient throws; to track call count, we use a subclass with overridden method

    // So let's create a subclass that overrides checkLenient and tracks calls
    class JsonReaderWithCheckLenientCounter extends JsonReader {
      int checkLenientCallCount = 0;

      JsonReaderWithCheckLenientCounter(Reader in) {
        super(in);
      }

      @Override
      void checkLenient() {
        checkLenientCallCount++;
      }
    }

    JsonReaderWithCheckLenientCounter countingReader = new JsonReaderWithCheckLenientCounter(mockReader);
    setField(countingReader, "pos", 0);
    setField(countingReader, "limit", 2);
    setField(countingReader, "buffer", buffer);
    countingReader.setLenient(true);

    Method skipMethod = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    skipMethod.setAccessible(true);
    skipMethod.invoke(countingReader);

    int pos = getField(countingReader, "pos");
    assertEquals(0, pos);

    assertEquals(1, countingReader.checkLenientCallCount);
  }

  @Test
    @Timeout(8000)
  void skipUnquotedValue_advancesPosAndRefillsBuffer() throws Exception {
    // Setup buffer with no special characters, so fillBuffer(1) is called
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 2);
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    setField(jsonReader, "buffer", buffer);

    // Spy jsonReader and mock fillBuffer via reflection
    JsonReader spyReader = Mockito.spy(jsonReader);
    spyReader.setLenient(true);

    // Mock private method fillBuffer(int) via reflection and Mockito
    // Use doAnswer to mock private method via reflection

    // Get fillBuffer method
    Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
    fillBufferMethod.setAccessible(true);

    // Use Mockito doAnswer to mock fillBuffer calls on spyReader
    // But Mockito can't mock private methods directly, so use a helper subclass to override fillBuffer

    class JsonReaderWithFillBufferMock extends JsonReader {
      int callCount = 0;

      JsonReaderWithFillBufferMock(Reader in) {
        super(in);
      }

      @Override
      protected boolean fillBuffer(int minimum) throws IOException {
        callCount++;
        if (callCount == 1) {
          return true;
        }
        return false;
      }
    }

    JsonReaderWithFillBufferMock mockFillBufferReader = new JsonReaderWithFillBufferMock(mockReader);
    setField(mockFillBufferReader, "pos", 0);
    setField(mockFillBufferReader, "limit", 2);
    setField(mockFillBufferReader, "buffer", buffer);
    mockFillBufferReader.setLenient(true);

    Method method = JsonReader.class.getDeclaredMethod("skipUnquotedValue");
    method.setAccessible(true);
    method.invoke(mockFillBufferReader);

    int pos = getField(mockFillBufferReader, "pos");
    assertEquals(2, pos);

    assertTrue(mockFillBufferReader.callCount >= 1);
  }

  // Utility method to set private field via reflection
  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  // Utility method to get private int field via reflection
  private static int getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getInt(target);
  }
}