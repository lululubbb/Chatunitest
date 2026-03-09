package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_207_4Test {

  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    // Create JsonReader with a dummy Reader, since we will manipulate buffer directly
    jsonReader = new JsonReader(null);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(target, value);
  }

  private Object getField(Object target, String fieldName) throws Exception {
    var field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void invokeSkipQuotedValue(Object target, char quote) throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    try {
      method.invoke(target, quote);
    } catch (InvocationTargetException ite) {
      throw ite.getCause();
    }
  }

  // Helper to mock private methods using reflection and spy
  private void mockPrivateMethod(Object spy, String methodName, Class<?>[] paramTypes, Supplier<?> answer) throws Exception {
    Method method = JsonReader.class.getDeclaredMethod(methodName, paramTypes);
    method.setAccessible(true);

    // Use Mockito's doAnswer on a spy with reflection proxy
    // Because Mockito cannot mock private methods directly, we override the method via a dynamic proxy
    // Instead, we replace the private method with a public one temporarily for testing via subclassing is complex,
    // so we use a workaround: we create a subclass that overrides the private methods as public and then spy that.

    // But here, simpler is to use reflection to override the method behavior by subclassing JsonReader in the test

    // This method is not used in current tests, kept for extensibility.
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_closesOnQuote() throws Throwable {
    // Setup buffer with a quoted string ending with quote char '"'
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = '"';
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 0);
    setField(jsonReader, "lineStart", 0);

    invokeSkipQuotedValue(jsonReader, '"');

    int pos = (int) getField(jsonReader, "pos");
    assertEquals(3, pos, "pos should be just after closing quote");
  }

  // Subclass to override private methods as protected for testing
  private static class JsonReaderForTest extends JsonReader {
    private JsonReaderForTest() {
      super(null);
    }

    @Override
    protected char readEscapeCharacter() throws IOException {
      // This method will be mocked by Mockito spy
      return super.readEscapeCharacter();
    }

    @Override
    protected boolean fillBuffer(int minimum) throws IOException {
      // This method will be mocked by Mockito spy
      return super.fillBuffer(minimum);
    }
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_handlesEscapedCharacters() throws Throwable {
    // Setup buffer with escaped quote: "abc\"def"
    // We simulate buffer: a b \ " d e f "
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';
    buffer[2] = '\\';
    buffer[3] = '"';
    buffer[4] = 'd';
    buffer[5] = 'e';
    buffer[6] = 'f';
    buffer[7] = '"';

    JsonReaderForTest readerForTest = new JsonReaderForTest();
    setField(readerForTest, "buffer", buffer);
    setField(readerForTest, "pos", 0);
    setField(readerForTest, "limit", 8);
    setField(readerForTest, "lineNumber", 0);
    setField(readerForTest, "lineStart", 0);

    JsonReaderForTest spyReader = spy(readerForTest);

    doAnswer(invocation -> {
      int pos = (int) getField(spyReader, "pos");
      setField(spyReader, "pos", pos + 1);
      return '\0';
    }).when(spyReader).readEscapeCharacter();

    doReturn(false).when(spyReader).fillBuffer(1);

    invokeSkipQuotedValue(spyReader, '"');

    int pos = (int) getField(spyReader, "pos");
    assertEquals(8, pos, "pos should be after closing quote");
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_incrementsLineNumberOnNewline() throws Throwable {
    // Setup buffer with a newline inside quotes: a \n "
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = '\n';
    buffer[2] = '"';

    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 3);
    setField(jsonReader, "lineNumber", 1);
    setField(jsonReader, "lineStart", 0);

    invokeSkipQuotedValue(jsonReader, '"');

    int pos = (int) getField(jsonReader, "pos");
    int lineNumber = (int) getField(jsonReader, "lineNumber");
    int lineStart = (int) getField(jsonReader, "lineStart");

    assertEquals(3, pos, "pos should be after closing quote");
    assertEquals(2, lineNumber, "lineNumber should increment by 1");
    assertEquals(2, lineStart, "lineStart should be updated to pos after newline");
  }

  @Test
    @Timeout(8000)
  public void skipQuotedValue_throwsOnUnterminatedString() throws Throwable {
    // Setup buffer with no closing quote and fillBuffer returns false
    char[] buffer = new char[1024];
    buffer[0] = 'a';
    buffer[1] = 'b';

    JsonReaderForTest readerForTest = new JsonReaderForTest();
    setField(readerForTest, "buffer", buffer);
    setField(readerForTest, "pos", 0);
    setField(readerForTest, "limit", 2);
    setField(readerForTest, "lineNumber", 0);
    setField(readerForTest, "lineStart", 0);

    JsonReaderForTest spyReader = spy(readerForTest);
    doReturn(false).when(spyReader).fillBuffer(1);

    Method method = JsonReader.class.getDeclaredMethod("skipQuotedValue", char.class);
    method.setAccessible(true);
    Throwable thrown = assertThrows(Throwable.class, () -> {
      try {
        method.invoke(spyReader, '"');
      } catch (InvocationTargetException ite) {
        throw ite.getCause();
      }
    });
    assertTrue(thrown instanceof IOException);
    assertTrue(thrown.getMessage().contains("Unterminated string"));
  }
}