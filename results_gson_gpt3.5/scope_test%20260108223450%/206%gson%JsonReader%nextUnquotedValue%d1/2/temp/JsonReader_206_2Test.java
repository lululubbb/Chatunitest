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

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class JsonReaderNextUnquotedValueTest {
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    Reader mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private String invokeNextUnquotedValue() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    method.setAccessible(true);
    try {
      return (String) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      // Unwrap IOException thrown by nextUnquotedValue
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_simpleLiteral_noLenient() throws Exception {
    // Setup buffer with unquoted value "abc"
    setBuffer("abc ");
    jsonReader.setLenient(false);

    String result = invokeNextUnquotedValue();

    assertEquals("abc", result);
    // pos should have advanced by 3
    assertEquals(3, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_literalStopsAtSpecialChar_lenient() throws Exception {
    // Setup buffer with "abc/def"
    setBuffer("abc/def");
    jsonReader.setLenient(true);

    // We expect checkLenient() to be called when '/' is found.
    // Because checkLenient() is private and no exception is thrown by default, no exception expected.

    String result = invokeNextUnquotedValue();

    assertEquals("abc", result);
    // pos should be at index after 'c' (pos + 3)
    assertEquals(3, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_longUnquotedValueWithBuilder() throws Exception {
    // Create a long string larger than buffer length to force StringBuilder usage
    int bufferLen = getBufferLength();
    String longLiteral = "a".repeat(bufferLen + 10) + " ";

    setBuffer(longLiteral);

    String result = invokeNextUnquotedValue();

    assertTrue(result.startsWith("a".repeat(bufferLen)));
    assertTrue(result.length() > bufferLen);
    // pos should be advanced by length of literal without trailing space
    assertEquals(longLiteral.length() - 1, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_bufferEndsWithoutFillBuffer() throws Exception {
    // Setup buffer with literal at end of buffer, no fillBuffer possible
    setBuffer("abc");
    // Set limit less than buffer length so fillBuffer returns false
    setField("limit", 3);
    setField("pos", 0);

    String result = invokeNextUnquotedValue();

    assertEquals("abc", result);
    assertEquals(3, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_emptyBuffer_returnsEmptyString() throws Exception {
    setField("limit", 0);
    setField("pos", 0);

    String result = invokeNextUnquotedValue();

    assertEquals("", result);
    assertEquals(0, (int) getField("pos"));
  }

  @Test
    @Timeout(8000)
  void nextUnquotedValue_checkLenientThrowsException() throws Exception {
    // Setup buffer with '/' which triggers checkLenient()
    // Use reflection to replace private method checkLenient with a proxy that throws IOException

    JsonReader spyReader = Mockito.spy(jsonReader);
    setField(spyReader, "buffer", "a/b".toCharArray());
    setField(spyReader, "limit", 3);
    setField(spyReader, "pos", 0);
    spyReader.setLenient(false);

    // Use reflection to replace checkLenient with a Method object that throws IOException
    Method checkLenientMethod = JsonReader.class.getDeclaredMethod("checkLenient");
    checkLenientMethod.setAccessible(true);

    // Instead of mocking private method (which Mockito cannot do), use a spy and override behavior by subclassing
    // But since we cannot subclass here easily, we replace checkLenient by a proxy via reflection

    // Use a dynamic proxy via reflection is complicated here, so instead we use a workaround:
    // Use a subclass of JsonReader overriding checkLenient to throw IOException

    JsonReader throwingLenientReader = new JsonReader(jsonReader.in) {
      @Override
      private void checkLenient() throws IOException {
        throw new IOException("Lenient check failed");
      }
    };

    // But private methods cannot be overridden, so we cannot do that.

    // Alternative approach: Use reflection to replace the method checkLenient with a method that throws IOException is not possible in Java.

    // So, workaround: Use reflection to invoke nextUnquotedValue on a subclass that overrides checkLenient.

    // Create a subclass of JsonReader to override checkLenient
    class JsonReaderWithThrowingCheckLenient extends JsonReader {
      public JsonReaderWithThrowingCheckLenient(Reader in) {
        super(in);
      }
      @Override
      void checkLenient() throws IOException {
        throw new IOException("Lenient check failed");
      }
    }

    // Create instance of subclass with spy buffer and fields set accordingly
    JsonReaderWithThrowingCheckLenient readerWithThrow = new JsonReaderWithThrowingCheckLenient(jsonReader.in);

    setField(readerWithThrow, "buffer", "a/b".toCharArray());
    setField(readerWithThrow, "limit", 3);
    setField(readerWithThrow, "pos", 0);
    readerWithThrow.setLenient(false);

    Method nextUnquotedValueMethod = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
    nextUnquotedValueMethod.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        nextUnquotedValueMethod.invoke(readerWithThrow);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IOException) {
          throw (IOException) e.getCause();
        }
        throw e;
      }
    });
    assertEquals("Lenient check failed", thrown.getMessage());
  }

  // Helper to set the buffer and initialize pos and limit accordingly
  private void setBuffer(String content) throws Exception {
    char[] buffer = content.toCharArray();
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", buffer.length);
  }

  // Generic helper to set a private field on jsonReader instance
  private void setField(String fieldName, Object value) throws Exception {
    setField(jsonReader, fieldName, value);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }

  // Generic helper to get a private field value from jsonReader instance
  private <T> T getField(String fieldName) throws Exception {
    Field field = jsonReader.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(jsonReader);
  }

  // Get buffer length via reflection
  private int getBufferLength() throws Exception {
    char[] buffer = getField("buffer");
    return buffer.length;
  }
}