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

class JsonReader_doPeek_Test {

  JsonReader jsonReader;
  Reader mockReader;

  @BeforeEach
  void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
    // Initialize stack and stackSize to avoid ArrayIndexOutOfBoundsException
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    // Set lenient to false by default
    Method setLenient = JsonReader.class.getDeclaredMethod("setLenient", boolean.class);
    setLenient.invoke(jsonReader, false);
  }

  private int invokeDoPeek() throws Exception {
    Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
    doPeek.setAccessible(true);
    return (int) doPeek.invoke(jsonReader);
  }

  private void setField(String name, Object value) throws Exception {
    Field f = JsonReader.class.getDeclaredField(name);
    f.setAccessible(true);
    f.set(jsonReader, value);
  }

  private Object getField(String name) throws Exception {
    Field f = JsonReader.class.getDeclaredField(name);
    f.setAccessible(true);
    return f.get(jsonReader);
  }

  private void setStackTop(int value) throws Exception {
    int[] stack = (int[]) getField("stack");
    stack[0] = value;
  }

  private void setStackSize(int size) throws Exception {
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, size);
  }

  private void setPos(int pos) throws Exception {
    Field f = JsonReader.class.getDeclaredField("pos");
    f.setAccessible(true);
    f.setInt(jsonReader, pos);
  }

  private void setLimit(int limit) throws Exception {
    Field f = JsonReader.class.getDeclaredField("limit");
    f.setAccessible(true);
    f.setInt(jsonReader, limit);
  }

  private void setBuffer(char[] buf) throws Exception {
    Field f = JsonReader.class.getDeclaredField("buffer");
    f.setAccessible(true);
    System.arraycopy(buf, 0, (char[]) f.get(jsonReader), 0, buf.length);
  }

  private void setLenient(boolean lenient) throws Exception {
    Method setLenient = JsonReader.class.getDeclaredMethod("setLenient", boolean.class);
    setLenient.invoke(jsonReader, lenient);
  }

  private void setPeeked(int value) throws Exception {
    Field f = JsonReader.class.getDeclaredField("peeked");
    f.setAccessible(true);
    f.setInt(jsonReader, value);
  }

  private int getPeeked() throws Exception {
    Field f = JsonReader.class.getDeclaredField("peeked");
    f.setAccessible(true);
    return f.getInt(jsonReader);
  }

  private void setPosAndLimitAndBuffer(char c) throws Exception {
    setPos(0);
    setLimit(1);
    setBuffer(new char[] { c });
  }

  // Mocks nextNonWhitespace(boolean) by reflection to return desired char or throw IOException
  private void mockNextNonWhitespace(int returnValue) throws Exception {
    // We cannot mock private methods easily, so we will override buffer, pos, limit and provide input accordingly
    setPosAndLimitAndBuffer((char) returnValue);
  }

  @Test
    @Timeout(8000)
  void testEmptyArray_peekStackEmptyArray_switchToNonEmptyArray_returnsPeekedNone() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    // nextNonWhitespace will be called later, so we set buffer to ']'
    setPosAndLimitAndBuffer(']');
    // We expect stack top to be changed to NONEMPTY_ARRAY and peeked to be set accordingly
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);
    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_ARRAY, stack[0]);
  }

  @Test
    @Timeout(8000)
  void testNonEmptyArray_commaAndSemicolonAndEndArray() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    // Case ']': returns PEEKED_END_ARRAY
    setPosAndLimitAndBuffer(']');
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);

    // Reset stack top to NONEMPTY_ARRAY for next test
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setPosAndLimitAndBuffer(';');
    // lenient false: checkLenient() throws if not lenient, so set lenient true
    setLenient(true);
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NULL, peeked);

    // Reset stack top and pos
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setPosAndLimitAndBuffer(',');
    setLenient(true);
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NULL, peeked);

    // Reset stack top and pos with unexpected char
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setPosAndLimitAndBuffer('x');
    setLenient(true);
    Exception ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Unterminated array"));
  }

  @Test
    @Timeout(8000)
  void testEmptyObject_nonEmptyObject() throws Exception {
    // EMPTY_OBJECT branch
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    // nextNonWhitespace(true) returns '"' => PEEKED_DOUBLE_QUOTED_NAME
    setPosAndLimitAndBuffer('"');
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, peeked);

    // NONEMPTY_OBJECT branch with '}' returns PEEKED_END_OBJECT
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setPosAndLimitAndBuffer('}');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_OBJECT, peeked);

    // NONEMPTY_OBJECT branch with ',' returns PEEKED_DOUBLE_QUOTED_NAME (fall through)
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setLenient(true);
    setPosAndLimitAndBuffer(',');
    // We need to provide nextNonWhitespace(true) after comma: simulate by buffer with '"'
    setPos(0);
    setLimit(2);
    setBuffer(new char[] { ',', '"' });
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, peeked);

    // NONEMPTY_OBJECT branch with unexpected char throws
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setLenient(true);
    setPosAndLimitAndBuffer('x');
    Exception ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Unterminated object"));
  }

  @Test
    @Timeout(8000)
  void testDanglingName_colonAndEquals() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);

    // ':' case
    setPosAndLimitAndBuffer(':');
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, peeked);

    // '=' case with '>' next char
    setLenient(true);
    setPos(0);
    setLimit(2);
    setBuffer(new char[] { '=', '>' });
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, peeked);

    // '=' case without '>' next char
    setLenient(true);
    setPos(0);
    setLimit(1);
    setBuffer(new char[] { '=' });
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, peeked);

    // unexpected char throws
    setLenient(true);
    setPosAndLimitAndBuffer('x');
    Exception ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  void testEmptyDocument_and_NonEmptyDocument() throws Exception {
    setStackTop(JsonScope.EMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);
    int peeked = invokeDoPeek();
    assertNotEquals(JsonReader.PEEKED_NONE, peeked);
    int[] stack = (int[]) getField("stack");
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, stack[0]);

    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setPosAndLimitAndBuffer((char) -1);
    setLenient(true);
    setPos(0);
    setLimit(1);
    setBuffer(new char[] { (char) -1 });
    // nextNonWhitespace(false) returns -1, so peeked = PEEKED_EOF
    Field posField = JsonReader.class.getDeclaredField("pos");
    posField.setAccessible(true);
    posField.setInt(jsonReader, 0);
    Field limitField = JsonReader.class.getDeclaredField("limit");
    limitField.setAccessible(true);
    limitField.setInt(jsonReader, 1);

    // We need to mock nextNonWhitespace(false) to return -1, but we cannot mock private methods
    // So we simulate by setting pos == limit so nextNonWhitespace returns -1
    setPos(limitField.getInt(jsonReader));
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_EOF, peeked);

    // Non -1 character triggers lenient check and pos--
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);
    setPos(0);
    setLimit(1);
    setBuffer(new char[] { 'x' });
    peeked = invokeDoPeek();
    assertNotEquals(JsonReader.PEEKED_NONE, peeked);
    int pos = (int) getField("pos");
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  void testClosed_throwsIllegalStateException() throws Exception {
    setStackTop(JsonScope.CLOSED);
    setStackSize(1);
    Exception ex = assertThrows(IllegalStateException.class, this::invokeDoPeek);
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testMainSwitchCases() throws Exception {
    // Set stack top to NONEMPTY_ARRAY for main switch coverage
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setLenient(true);

    // Case ']': returns PEEKED_END_ARRAY
    setPosAndLimitAndBuffer(']');
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);

    // Case ';' returns PEEKED_NULL
    setPosAndLimitAndBuffer(';');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NULL, peeked);

    // Case ',' returns PEEKED_NULL
    setPosAndLimitAndBuffer(',');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NULL, peeked);

    // Case '\'' returns PEEKED_SINGLE_QUOTED
    setPosAndLimitAndBuffer('\'');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED, peeked);

    // Case '"' returns PEEKED_DOUBLE_QUOTED
    setPosAndLimitAndBuffer('"');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED, peeked);

    // Case '[' returns PEEKED_BEGIN_ARRAY
    setPosAndLimitAndBuffer('[');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_BEGIN_ARRAY, peeked);

    // Case '{' returns PEEKED_BEGIN_OBJECT
    setPosAndLimitAndBuffer('{');
    peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_BEGIN_OBJECT, peeked);
  }

  @Test
    @Timeout(8000)
  void testPeekKeywordAndPeekNumberAndIsLiteralBranches() throws Exception {
    // Setup stack top to NONEMPTY_DOCUMENT for this branch to run
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);

    // Setup buffer with a literal character that is not keyword or number
    setPos(0);
    setLimit(1);
    setBuffer(new char[] { 'a' });

    // We need to mock peekKeyword() and peekNumber() to return PEEKED_NONE
    // but they are private, so we simulate by setting buffer to a non-keyword/non-number char
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_UNQUOTED, peeked);
  }

  @Test
    @Timeout(8000)
  void testPeekKeywordReturnsNonNone() throws Exception {
    // We cannot mock private methods easily, so we test with input "true"
    String json = "true";
    jsonReader = new JsonReader(new java.io.StringReader(json));
    // Initialize stack and stackSize
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[0] = JsonScope.NONEMPTY_DOCUMENT;
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    setLenient(true);
    int peeked = invokeDoPeek();
    assertEquals(JsonReader.PEEKED_TRUE, peeked);
  }

  @Test
    @Timeout(8000)
  void testPeekNumberReturnsNonNone() throws Exception {
    // We cannot mock private methods easily, so we test with input "123"
    String json = "123";
    jsonReader = new JsonReader(new java.io.StringReader(json));
    // Initialize stack and stackSize
    Field stackField = JsonReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonReader);
    stack[0] = JsonScope.NONEMPTY_DOCUMENT;
    Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonReader, 1);
    setLenient(true);
    int peeked = invokeDoPeek();
    assertTrue(peeked == JsonReader.PEEKED_NUMBER || peeked == JsonReader.PEEKED_LONG);
  }

  @Test
    @Timeout(8000)
  void testIsLiteralFalseThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);
    setPos(0);
    setLimit(1);
    // Use a character that is not literal, e.g. whitespace
    setBuffer(new char[] { ' ' });
    Exception ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected value"));
  }
}