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

class JsonReader_doPeekTest {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Initialize stack and stackSize to avoid ArrayIndexOutOfBoundsException
    var stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[0] = JsonScope.EMPTY_ARRAY; // default initial state
    var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);

    // Set pos and limit so nextNonWhitespace can work if called
    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, 0);

    // lenient false by default
    var lenientField = JsonReader.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(jsonReader, false);
  }

  private int invokeDoPeek() throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    return (int) doPeek.invoke(jsonReader);
  }

  private void setStackTop(int value) throws Exception {
    var stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[0] = value;
  }

  private void setStackSize(int size) throws Exception {
    var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
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

  private void setBuffer(char[] content, int length) throws Exception {
    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(jsonReader);
    System.arraycopy(content, 0, buffer, 0, length);
    setLimit(length);
  }

  private void setPeeked(int value) throws Exception {
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    peekedField.setInt(jsonReader, value);
  }

  @Test
    @Timeout(8000)
  void testEmptyArray_returnsPeekedEndArray() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    int result = invokeDoPeek();
    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_END_ARRAY, peekedField.getInt(jsonReader));
    assertEquals(JsonReader.PEEKED_END_ARRAY, result);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyArray_nextNonWhitespaceReturnsClosingBracket() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    // Mock nextNonWhitespace(true) to return ']'
    JsonReader spyReader = spy(jsonReader);
    doReturn(']').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_END_ARRAY, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_END_ARRAY, result);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyArray_nextNonWhitespaceReturnsComma() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyArray_nextNonWhitespaceReturnsSemicolonLenientThrowsOtherwise() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(';').when(spyReader).nextNonWhitespace(true);

    // lenient = false throws syntaxError
    setLenient(false);
    Exception e = assertThrows(Exception.class, () -> {
      Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
      doPeek.setAccessible(true);
      doPeek.invoke(spyReader);
    });
    assertTrue(e.getCause().getMessage().contains("Unterminated array"));

    // lenient = true calls checkLenient and does not throw
    setLenient(true);
    doNothing().when(spyReader).checkLenient();
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void testEmptyObjectNextNonWhitespaceDoubleQuote() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, result);
  }

  @Test
    @Timeout(8000)
  void testEmptyObjectNextNonWhitespaceSingleQuoteLenient() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\'').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED_NAME, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED_NAME, result);
  }

  @Test
    @Timeout(8000)
  void testEmptyObjectNextNonWhitespaceClosingBraceEmptyObject() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_END_OBJECT, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_END_OBJECT, result);
  }

  @Test
    @Timeout(8000)
  void testEmptyObjectNextNonWhitespaceClosingBraceNonEmptyObjectThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);

    Exception e = assertThrows(Exception.class, () -> {
      Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
      doPeek.setAccessible(true);
      doPeek.invoke(spyReader);
    });
    assertTrue(e.getCause().getMessage().contains("Expected name"));
  }

  @Test
    @Timeout(8000)
  void testEmptyObjectNextNonWhitespaceDefaultIsLiteralLenient() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[0] = 'x';

    Method isLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteral.setAccessible(true);
    assertTrue((boolean) isLiteral.invoke(spyReader, 'x'));

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_UNQUOTED_NAME, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_UNQUOTED_NAME, result);
  }

  @Test
    @Timeout(8000)
  void testDanglingNameColon() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(':').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void testDanglingNameEqualsLenient() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('=').when(spyReader).nextNonWhitespace(true);

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 0);

    var limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(spyReader, 2);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[0] = '=';
    buffer[1] = '>';

    doNothing().when(spyReader).checkLenient();

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var posAfter = JsonReader.class.getDeclaredField("pos");
    posAfter.setAccessible(true);
    int posValue = posAfter.getInt(spyReader);

    assertEquals(0, result);
    assertEquals(1, posValue);
  }

  @Test
    @Timeout(8000)
  void testDanglingNameUnexpectedCharThrows() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);

    Exception e = assertThrows(Exception.class, () -> {
      Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
      doPeek.setAccessible(true);
      doPeek.invoke(spyReader);
    });
    assertTrue(e.getCause().getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  void testEmptyDocumentLenientConsumesPrefix() throws Exception {
    setStackTop(JsonScope.EMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).consumeNonExecutePrefix();

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(spyReader);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyDocumentEofReturnsPeekedEof() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(-1).when(spyReader).nextNonWhitespace(false);
    doNothing().when(spyReader).checkLenient();

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_EOF, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_EOF, result);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyDocumentNonEofLenientPosDecrement() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(false);
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var posAfter = JsonReader.class.getDeclaredField("pos");
    posAfter.setAccessible(true);
    assertEquals(0, posAfter.getInt(spyReader));
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  void testClosedThrowsIllegalState() throws Exception {
    setStackTop(JsonScope.CLOSED);
    setStackSize(1);

    Exception e = assertThrows(IllegalStateException.class, () -> {
      invokeDoPeek();
    });
    assertEquals("JsonReader is closed", e.getMessage());
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceClosingBracketEmptyArray() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(']').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_END_ARRAY, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_END_ARRAY, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceCommaLenientEmptyArrayReturnsNull() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var posAfter = JsonReader.class.getDeclaredField("pos");
    posAfter.setAccessible(true);
    assertEquals(0, posAfter.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_NULL, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceUnexpectedValueThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setStackSize(1);
    setLenient(false);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);

    Exception e = assertThrows(Exception.class, () -> {
      Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
      doPeek.setAccessible(true);
      doPeek.invoke(spyReader);
    });
    assertTrue(e.getCause().getMessage().contains("Unexpected value"));
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceSingleQuoteLenient() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\'').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceDoubleQuote() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceBeginArray() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('[').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_BEGIN_ARRAY, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_BEGIN_ARRAY, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNextNonWhitespaceBeginObject() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('{').when(spyReader).nextNonWhitespace(true);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_BEGIN_OBJECT, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_BEGIN_OBJECT, result);
  }

  @Test
    @Timeout(8000)
  void testPeekKeywordReturnsNonNone() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doReturn(JsonReader.PEEKED_TRUE).when(spyReader).peekKeyword();
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_TRUE, result);
  }

  @Test
    @Timeout(8000)
  void testPeekNumberReturnsNonNone() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekKeyword();
    doReturn(JsonReader.PEEKED_NUMBER).when(spyReader).peekNumber();
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_NUMBER, result);
  }

  @Test
    @Timeout(8000)
  void testIsLiteralFalseThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekKeyword();
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekNumber();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[0] = '#'; // non-literal char

    Method isLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteral.setAccessible(true);
    assertFalse((boolean) isLiteral.invoke(spyReader, '#'));

    Exception e = assertThrows(Exception.class, () -> {
      Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
      doPeek.setAccessible(true);
      doPeek.invoke(spyReader);
    });
    assertTrue(e.getCause().getMessage().contains("Expected value"));
  }

  @Test
    @Timeout(8000)
  void testIsLiteralTrueLenientReturnsUnquoted() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekKeyword();
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekNumber();
    doNothing().when(spyReader).checkLenient();

    var posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(spyReader, 1);

    var bufferField = JsonReader.class.getDeclaredField("buffer");
    bufferField.setAccessible(true);
    char[] buffer = (char[]) bufferField.get(spyReader);
    buffer[0] = 'x'; // literal char

    Method isLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteral.setAccessible(true);
    assertTrue((boolean) isLiteral.invoke(spyReader, 'x'));

    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    int result = (int) doPeek.invoke(spyReader);

    var peekedField = JsonReader.class.getDeclaredField("peeked");
    peekedField.setAccessible(true);
    assertEquals(JsonReader.PEEKED_UNQUOTED, peekedField.getInt(spyReader));
    assertEquals(JsonReader.PEEKED_UNQUOTED, result);
  }
}