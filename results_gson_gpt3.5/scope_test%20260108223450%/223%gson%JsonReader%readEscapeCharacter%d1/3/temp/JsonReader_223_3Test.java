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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_223_3Test {

  private JsonReader jsonReader;
  private Method readEscapeCharacterMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    // Use a dummy Reader since we won't read from it in these tests
    jsonReader = new JsonReader(new java.io.StringReader(""));

    // Access the private method readEscapeCharacter using reflection
    readEscapeCharacterMethod = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    readEscapeCharacterMethod.setAccessible(true);
  }

  private char invokeReadEscapeCharacter() throws Throwable {
    try {
      return (char) readEscapeCharacterMethod.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_ThrowsOnUnterminatedEscapeSequence() throws Exception {
    setField("pos", 0);
    setField("limit", 0);
    // fillBuffer(1) will be called and should return false to simulate EOF
    // We mock fillBuffer using a spy on jsonReader

    JsonReader spyReader = spy(jsonReader);
    doReturn(false).when(spyReader).fillBuffer(1);

    // Replace jsonReader with spyReader in this test
    readEscapeCharacterMethod.setAccessible(true);
    var method = readEscapeCharacterMethod;

    try {
      method.invoke(spyReader);
      fail("Expected IOException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IOException);
      assertEquals("Unterminated escape sequence", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_UnicodeEscape_ValidLowerCase() throws Throwable {
    // Setup buffer with 'u' + 4 hex digits "abcd"
    char[] buffer = new char[1024];
    buffer[0] = 'u';
    buffer[1] = 'a';
    buffer[2] = 'b';
    buffer[3] = 'c';
    buffer[4] = 'd';

    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 5);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(4);

    // Use reflection on spyReader
    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    char result = (char) method.invoke(spyReader);

    int expected = Integer.parseInt("abcd", 16);
    assertEquals((char) expected, result);
    assertEquals(5, spyReader.pos);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_UnicodeEscape_ValidUpperCase() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'u';
    buffer[1] = 'A';
    buffer[2] = 'B';
    buffer[3] = 'C';
    buffer[4] = 'D';

    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 5);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(4);

    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);
    char result = (char) method.invoke(spyReader);

    int expected = Integer.parseInt("ABCD", 16);
    assertEquals((char) expected, result);
    assertEquals(5, spyReader.pos);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_UnicodeEscape_InvalidHex_ThrowsNumberFormatException() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'u';
    buffer[1] = 'g'; // invalid hex char
    buffer[2] = 'h';
    buffer[3] = 'i';
    buffer[4] = 'j';

    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 5);

    JsonReader spyReader = spy(jsonReader);
    doReturn(true).when(spyReader).fillBuffer(4);

    Method method = JsonReader.class.getDeclaredMethod("readEscapeCharacter");
    method.setAccessible(true);

    try {
      method.invoke(spyReader);
      fail("Expected NumberFormatException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof NumberFormatException);
      assertEquals("\\ughij", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_SingleCharacterEscapes() throws Throwable {
    char[] escapes = {'t', 'b', 'n', 'r', 'f'};
    char[] expected = {'\t', '\b', '\n', '\r', '\f'};

    for (int i = 0; i < escapes.length; i++) {
      char esc = escapes[i];
      char exp = expected[i];

      char[] buffer = new char[1024];
      buffer[0] = esc;

      setField("buffer", buffer);
      setField("pos", 0);
      setField("limit", 1);

      char result = invokeReadEscapeCharacter();
      assertEquals(exp, result);
      assertEquals(1, jsonReader.pos);
    }
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_SpecialEscape_NewlineIncrementsLineNumber() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = '\n';

    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);
    setField("lineNumber", 5);
    setField("lineStart", 3);

    char result = invokeReadEscapeCharacter();

    assertEquals('\n', result);
    assertEquals(1, jsonReader.pos);
    assertEquals(6, jsonReader.lineNumber);
    assertEquals(1, jsonReader.lineStart);
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_SpecialEscape_OtherChars() throws Throwable {
    char[] specialChars = {'\'', '"', '\\', '/'};

    for (char c : specialChars) {
      char[] buffer = new char[1024];
      buffer[0] = c;

      setField("buffer", buffer);
      setField("pos", 0);
      setField("limit", 1);

      char result = invokeReadEscapeCharacter();

      assertEquals(c, result);
      assertEquals(1, jsonReader.pos);
    }
  }

  @Test
    @Timeout(8000)
  public void testReadEscapeCharacter_InvalidEscapeSequence_ThrowsIOException() throws Throwable {
    char[] buffer = new char[1024];
    buffer[0] = 'z';

    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);

    try {
      invokeReadEscapeCharacter();
      fail("Expected IOException");
    } catch (IOException e) {
      assertEquals("Invalid escape sequence", e.getMessage());
    }
  }
}