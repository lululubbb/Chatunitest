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

class JsonReader_195_6Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokeDoPeek() throws Throwable {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    try {
      return (int) doPeekMethod.invoke(jsonReader);
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
  void testDoPeek_emptyArray() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);
    int result = invokeDoPeek();
    assertEquals(4, result); // PEEKED_END_ARRAY
  }

  @Test
    @Timeout(8000)
  void testDoPeek_nonEmptyArray_comma() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);

    // nextNonWhitespace returns ','
    Method nextNonWhitespace = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    nextNonWhitespace.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);
    // Replace jsonReader with spyReader for this test
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_nonEmptyArray_endArray() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(']').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(4, result); // PEEKED_END_ARRAY
  }

  @Test
    @Timeout(8000)
  void testDoPeek_nonEmptyArray_unterminatedArray() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Unterminated array"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_emptyObject_doubleQuotedName() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(13, result); // PEEKED_DOUBLE_QUOTED_NAME
  }

  @Test
    @Timeout(8000)
  void testDoPeek_emptyObject_singleQuotedName_lenient() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\'').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(12, result); // PEEKED_SINGLE_QUOTED_NAME
  }

  @Test
    @Timeout(8000)
  void testDoPeek_emptyObject_endObject() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(2, result); // PEEKED_END_OBJECT
  }

  @Test
    @Timeout(8000)
  void testDoPeek_emptyObject_expectedNameException() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_OBJECT});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected name"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_danglingName_colon() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(':').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_danglingName_equalsArrow_lenient() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    // Setup buffer and pos to simulate '=' followed by '>'
    char[] buffer = new char[1024];
    buffer[0] = '>';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('=').when(spyReader).nextNonWhitespace(true);
    doReturn(true).when(spyReader).fillBuffer(1);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_danglingName_unexpectedChar() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_emptyDocument_lenient() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_DOCUMENT});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).consumeNonExecutePrefix();
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_nonEmptyDocument_eof() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn(-1).when(spyReader).nextNonWhitespace(false);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(17, result); // PEEKED_EOF
  }

  @Test
    @Timeout(8000)
  void testDoPeek_nonEmptyDocument_nonEof() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    setField("peeked", 0);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(false);
    doNothing().when(spyReader).checkLenient();
    jsonReader = spyReader;

    setField("pos", 1);

    int result = invokeDoPeek();
    assertEquals(0, result); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_closed() throws Throwable {
    setField("stack", new int[] {JsonScope.CLOSED});
    setField("stackSize", 1);
    setField("peeked", 0);

    IllegalStateException ex = assertThrows(IllegalStateException.class, this::invokeDoPeek);
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testDoPeek_arrayValues() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();
    setField("pos", 1);
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(7, result); // PEEKED_NULL
  }

  @Test
    @Timeout(8000)
  void testDoPeek_valuesVarious() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    char[] buffer = new char[1024];
    buffer[0] = 'x';
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();
    doReturn(0).when(spyReader).peekKeyword();
    doReturn(0).when(spyReader).peekNumber();
    doReturn(true).when(spyReader).isLiteral('x');
    jsonReader = spyReader;

    int result = invokeDoPeek();
    assertEquals(10, result); // PEEKED_UNQUOTED
  }

  @Test
    @Timeout(8000)
  void testDoPeek_valuesVarious_notLiteral() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    setField("peeked", 0);
    jsonReader.setLenient(true);

    char[] buffer = new char[1024];
    buffer[0] = '\u0000'; // non-literal
    setField("buffer", buffer);
    setField("pos", 0);
    setField("limit", 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\u0000').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();
    doReturn(0).when(spyReader).peekKeyword();
    doReturn(0).when(spyReader).peekNumber();
    doReturn(false).when(spyReader).isLiteral('\u0000');
    jsonReader = spyReader;

    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected value"));
  }
}