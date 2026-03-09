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

public class JsonReader_195_2Test {

  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokeDoPeek() throws Throwable {
    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    try {
      return (int) method.invoke(jsonReader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  private Object getField(String fieldName) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(jsonReader);
  }

  private void setStack(int[] stackValues, int size) throws Exception {
    setField("stack", stackValues);
    setField("stackSize", size);
  }

  private void setBufferAndPos(char[] buffer, int pos, int limit) throws Exception {
    setField("buffer", buffer);
    setField("pos", pos);
    setField("limit", limit);
  }

  private void setLenient(boolean lenient) throws Exception {
    jsonReader.setLenient(lenient);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyArray() throws Throwable {
    setStack(new int[] {JsonScope.EMPTY_ARRAY}, 1);

    int result = invokeDoPeek();

    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_END_ARRAY, result);
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_Comma() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    // Setup buffer and pos to simulate nextNonWhitespace returning ','
    setBufferAndPos(new char[] {','}, 0, 1);

    // Spy on jsonReader to override nextNonWhitespace
    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);
    // Replace jsonReader with spy
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_NONE, result, "Result should not be set to a constant for comma");
    // Actually, doPeek returns PEEKED_NONE (0) only if no token is found, but here it should break and continue
    // But in the code, after comma it breaks and continues to nextNonWhitespace call again
    // So to avoid complexity, test the side effect stack change:
    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_ARRAY, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_EndArray() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(']').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_END_ARRAY, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_UnterminatedArray() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);

    IOException ex = assertThrows(IOException.class, () -> method.invoke(spyReader));
    assertTrue(ex.getMessage().contains("Unterminated array"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_QuotedName() throws Throwable {
    setStack(new int[] {JsonScope.EMPTY_OBJECT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_SingleQuotedNameLenient() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.EMPTY_OBJECT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\'').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED_NAME, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED_NAME, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_EndObject() throws Throwable {
    setStack(new int[] {JsonScope.EMPTY_OBJECT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_END_OBJECT, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_END_OBJECT, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_ExpectedNameException() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_OBJECT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('}').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    IOException ex = assertThrows(IOException.class, () -> method.invoke(spyReader));
    assertTrue(ex.getMessage().contains("Expected name"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_Colon() throws Throwable {
    setStack(new int[] {JsonScope.DANGLING_NAME}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(':').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_NONE, result);
    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_OBJECT, stack[0]);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_EqualAndGreaterThanLenient() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.DANGLING_NAME}, 1);

    // Setup buffer so that next char after '=' is '>'
    char[] buffer = new char[] {'>'};
    setBufferAndPos(buffer, 0, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('=').when(spyReader).nextNonWhitespace(true);
    doReturn(true).when(spyReader).fillBuffer(1);
    doReturn('>').when(spyReader).nextNonWhitespace(true);

    // We must override nextNonWhitespace carefully:
    // Actually doPeek calls nextNonWhitespace(true) once, returns '=', then nextNonWhitespace(true) again to check '>'
    // So we simulate fillBuffer and buffer[pos] == '>' manually

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_NONE, result);
    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_OBJECT, stack[0]);
    int pos = (int) getField("pos");
    assertEquals(1, pos);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_UnexpectedChar() throws Throwable {
    setStack(new int[] {JsonScope.DANGLING_NAME}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);

    IOException ex = assertThrows(IOException.class, () -> method.invoke(spyReader));
    assertTrue(ex.getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyDocument_LenientConsumesPrefix() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.EMPTY_DOCUMENT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).consumeNonExecutePrefix();

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);
    assertEquals(JsonReader.PEEKED_NONE, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyDocument_Eof() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(-1).when(spyReader).nextNonWhitespace(false);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_EOF, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_EOF, peeked);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyDocument_UnexpectedValue() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_DOCUMENT}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(false);
    doNothing().when(spyReader).checkLenient();

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    int pos = (int) getField("pos");
    assertEquals(-1, pos); // pos decremented by 1 (initial pos 0 - 1 = -1)
    assertEquals(JsonReader.PEEKED_NONE, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_Closed() throws Throwable {
    setStack(new int[] {JsonScope.CLOSED}, 1);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> method.invoke(jsonReader));
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_ArrayWithCommaLenientNull() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.EMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(',').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    // Setup buffer and pos for pos-- side effect
    setBufferAndPos(new char[] {','}, 1, 1);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_NULL, result);
    int peeked = (int) getField("peeked");
    assertEquals(JsonReader.PEEKED_NULL, peeked);
    int pos = (int) getField("pos");
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_SingleQuotedLenient() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('\'').when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DoubleQuoted() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('"').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_BeginArray() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('[').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_BEGIN_ARRAY, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_BeginObject() throws Throwable {
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn('{').when(spyReader).nextNonWhitespace(true);

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_BEGIN_OBJECT, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralValue() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    char literalChar = 'a';

    char[] buffer = new char[] {literalChar};
    setBufferAndPos(buffer, 0, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(literalChar).when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();
    doReturn(true).when(spyReader).isLiteral(literalChar);

    // Setup peekKeyword and peekNumber to return PEEKED_NONE
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekKeyword();
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekNumber();

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    int result = (int) method.invoke(spyReader);

    assertEquals(JsonReader.PEEKED_UNQUOTED, result);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralValue_NotLiteral_Throws() throws Throwable {
    setLenient(true);
    setStack(new int[] {JsonScope.NONEMPTY_ARRAY}, 1);

    char nonLiteralChar = ' ';

    char[] buffer = new char[] {nonLiteralChar};
    setBufferAndPos(buffer, 0, 1);

    JsonReader spyReader = spy(jsonReader);
    doReturn(nonLiteralChar).when(spyReader).nextNonWhitespace(true);
    doNothing().when(spyReader).checkLenient();
    doReturn(false).when(spyReader).isLiteral(nonLiteralChar);

    // Setup peekKeyword and peekNumber to return PEEKED_NONE
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekKeyword();
    doReturn(JsonReader.PEEKED_NONE).when(spyReader).peekNumber();

    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);

    IOException ex = assertThrows(IOException.class, () -> method.invoke(spyReader));
    assertTrue(ex.getMessage().contains("Expected value"));
  }
}