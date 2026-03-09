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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_223_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;
  private Method readEscapeCharacterMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);
  }

  private char invokeReadEscapeCharacter() throws IOException, InvocationTargetException, IllegalAccessException {
    try {
      return (char) readEscapeCharacterMethod.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    }
  }

  private void setBufferAndPos(char[] buffer, int pos, int limit) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    System.arraycopy(buffer, 0, (char[]) bufferField.get(jsonReader), 0, buffer.length);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setLineNumberAndLineStart(int lineNumber, int lineStart) throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(jsonReader, lineNumber);

    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(jsonReader, lineStart);
  }

  private void setPos(int pos) throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, limit);
  }

  private void setLenient(boolean lenient) throws Exception {
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, lenient);
  }

  private void setLineNumber(int lineNumber) throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    lineNumberField.setInt(jsonReader, lineNumber);
  }

  private void setLineStart(int lineStart) throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    lineStartField.setInt(jsonReader, lineStart);
  }

  private void setPosLimit(int pos, int limit) throws Exception {
    setPos(pos);
    setLimit(limit);
  }

  private void setBufferContent(String content) throws Exception {
    char[] buffer = new char[1024];
    Arrays.fill(buffer, '\0');
    char[] contentChars = content.toCharArray();
    System.arraycopy(contentChars, 0, buffer, 0, contentChars.length);
    setBufferAndPos(buffer, 0, contentChars.length);
  }

  private void setBufferContentAndPosLimit(String content, int pos, int limit) throws Exception {
    char[] buffer = new char[1024];
    Arrays.fill(buffer, '\0');
    char[] contentChars = content.toCharArray();
    System.arraycopy(contentChars, 0, buffer, 0, contentChars.length);
    setBufferAndPos(buffer, pos, limit);
  }

  private void mockFillBufferReturn(boolean returnValue) throws Exception {
    // fillBuffer is private, so we use spy and doReturn on it
    JsonReader spyReader = spy(jsonReader);
    doReturn(returnValue).when(spyReader).fillBuffer(anyInt());

    // Replace jsonReader with spyReader for testing
    jsonReader = spyReader;
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeValidLowercaseHex() throws Exception {
    // buffer = "u0041" at pos=0, limit=5
    // 'u' + 4 hex digits for 'A' (0x41)
    char[] buffer = new char[] {'u', '0', '0', '4', '1'};
    setBufferAndPos(buffer, 0, 5);

    char result = invokeReadEscapeCharacter();
    assertEquals('A', result);
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeValidUppercaseHex() throws Exception {
    // buffer = "u0041" at pos=0, limit=5 but with uppercase hex digits
    char[] buffer = new char[] {'u', '0', '0', '4', 'A'};
    setBufferAndPos(buffer, 0, 5);

    char result = invokeReadEscapeCharacter();
    assertEquals('A', result);
    assertEquals(5, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_unicodeInvalidHex_throwsNumberFormatException() throws Exception {
    char[] buffer = new char[] {'u', '0', '0', 'G', '1'};
    setBufferAndPos(buffer, 0, 5);

    NumberFormatException ex = assertThrows(NumberFormatException.class, () -> invokeReadEscapeCharacter());
    assertTrue(ex.getMessage().contains("\\u"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeTab() throws Exception {
    char[] buffer = new char[] {'t'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();
    assertEquals('\t', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeBackspace() throws Exception {
    char[] buffer = new char[] {'b'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();
    assertEquals('\b', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewline() throws Exception {
    char[] buffer = new char[] {'n'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();
    assertEquals('\n', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeCarriageReturn() throws Exception {
    char[] buffer = new char[] {'r'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();
    assertEquals('\r', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeFormFeed() throws Exception {
    char[] buffer = new char[] {'f'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();
    assertEquals('\f', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeNewlineCharInBufferIncrementsLineNumber() throws Exception {
    char[] buffer = new char[] {'\n'};
    setBufferAndPos(buffer, 0, 1);
    setLineNumber(5);
    setLineStart(3);

    char result = invokeReadEscapeCharacter();

    assertEquals('\n', result);
    assertEquals(1, getPos());
    assertEquals(6, getLineNumber());
    assertEquals(1, getLineStart());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeSingleQuote() throws Exception {
    char[] buffer = new char[] {'\''};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();

    assertEquals('\'', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeDoubleQuote() throws Exception {
    char[] buffer = new char[] {'"'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();

    assertEquals('"', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeBackslash() throws Exception {
    char[] buffer = new char[] {'\\'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();

    assertEquals('\\', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_escapeSlash() throws Exception {
    char[] buffer = new char[] {'/'};
    setBufferAndPos(buffer, 0, 1);

    char result = invokeReadEscapeCharacter();

    assertEquals('/', result);
    assertEquals(1, getPos());
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_posEqualsLimit_fillBufferFalse_throwsIOException() throws Exception {
    setPosLimit(10, 10);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, () -> {
      readEscapeCharacterMethod.invoke(jsonReader);
    });

    assertTrue(ex.getCause().getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_posPlus4GreaterThanLimit_fillBufferFalse_throwsIOException() throws Exception {
    // For unicode escape, pos + 4 > limit and fillBuffer returns false
    char[] buffer = new char[] {'u', '0', '0'};
    setBufferAndPos(buffer, 0, 3);

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(4);
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, () -> {
      readEscapeCharacterMethod.invoke(jsonReader);
    });

    assertTrue(ex.getCause().getMessage().contains("Unterminated escape sequence"));
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_invalidEscapeSequence_throwsIOException() throws Exception {
    char[] buffer = new char[] {'z'};
    setBufferAndPos(buffer, 0, 1);

    IOException ex = assertThrows(IOException.class, () -> {
      readEscapeCharacterMethod.invoke(jsonReader);
    });

    assertTrue(ex.getCause().getMessage().contains("Invalid escape sequence"));
  }

  private int getPos() throws Exception {
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    return posField.getInt(jsonReader);
  }

  private int getLineNumber() throws Exception {
    var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
    lineNumberField.setAccessible(true);
    return lineNumberField.getInt(jsonReader);
  }

  private int getLineStart() throws Exception {
    var lineStartField = JsonReader.class.getDeclaredField("lineStart");
    lineStartField.setAccessible(true);
    return lineStartField.getInt(jsonReader);
  }
}