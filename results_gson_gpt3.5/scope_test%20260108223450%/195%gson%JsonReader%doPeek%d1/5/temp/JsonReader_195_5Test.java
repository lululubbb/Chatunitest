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

public class JsonReader_195_5Test {
  private JsonReader jsonReader;
  private Reader mockReader;

  @BeforeEach
  public void setUp() throws Exception {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);

    // Set stackSize to 1 and initialize stack array with default value
    setField(jsonReader, "stackSize", 1);
    int[] stack = new int[32];
    setField(jsonReader, "stack", stack);
    // Set lenient to false by default
    setField(jsonReader, "lenient", false);
    // Reset peeked to 0 (PEEKED_NONE)
    setField(jsonReader, "peeked", 0);
    // Prepare buffer, pos, limit
    char[] buffer = new char[1024];
    setField(jsonReader, "buffer", buffer);
    setField(jsonReader, "pos", 0);
    setField(jsonReader, "limit", 0);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private Object invokeDoPeek() throws Exception {
    Method method = JsonReader.class.getDeclaredMethod("doPeek");
    method.setAccessible(true);
    return method.invoke(jsonReader);
  }

  private void setStackTop(int value) throws Exception {
    int[] stack = (int[]) getField(jsonReader, "stack");
    stack[0] = value;
  }

  private Object getField(Object target, String fieldName) throws Exception {
    Field field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void setLenient(boolean lenient) throws Exception {
    setField(jsonReader, "lenient", lenient);
  }

  private void setBuffer(String content) throws Exception {
    char[] buffer = (char[]) getField(jsonReader, "buffer");
    int len = content.length();
    for (int i = 0; i < len; i++) {
      buffer[i] = content.charAt(i);
    }
    setField(jsonReader, "limit", len);
    setField(jsonReader, "pos", 0);
  }

  private void setPos(int pos) throws Exception {
    setField(jsonReader, "pos", pos);
  }

  private void setPeeked(int val) throws Exception {
    setField(jsonReader, "peeked", val);
  }

  private void setStackSize(int size) throws Exception {
    setField(jsonReader, "stackSize", size);
  }

  private void setPeekedString(String s) throws Exception {
    setField(jsonReader, "peekedString", s);
  }

  private void setPeekedLong(long l) throws Exception {
    setField(jsonReader, "peekedLong", l);
  }

  private void setPeekedNumberLength(int l) throws Exception {
    setField(jsonReader, "peekedNumberLength", l);
  }

  // Mock nextNonWhitespace to simulate different chars for testing
  private void mockNextNonWhitespace(int[] chars, boolean[] throwOnEofFlags) throws Exception {
    // We will override nextNonWhitespace using a spy and reflection
    JsonReader spyReader = spy(jsonReader);
    final int[] index = {0};
    doAnswer(invocation -> {
      if (index[0] >= chars.length) {
        return -1;
      }
      int c = chars[index[0]];
      index[0]++;
      return c;
    }).when(spyReader).getClass().getDeclaredMethod("nextNonWhitespace", boolean.class).invoke(spyReader, anyBoolean());
    // But reflection mock is complicated, so instead, we create a subclass with overridden method

    // This approach is complicated, so instead we set pos and buffer to simulate nextNonWhitespace behavior
  }

  /*
   * Because nextNonWhitespace, checkLenient, peekKeyword, peekNumber, isLiteral, syntaxError,
   * consumeNonExecutePrefix are private, and some used in doPeek,
   * we will simulate behavior by setting buffer, pos, limit, lenient and stack accordingly.
   */

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyArray() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    int result = (int) invokeDoPeek();
    int peeked = (int) getField(jsonReader, "peeked");
    assertEquals(JsonReader.PEEKED_END_ARRAY, peeked);
    assertEquals(JsonReader.PEEKED_NONE, result, "Returned value should be 0 because doPeek returns peeked after setting");
    // Actually doPeek returns peeked after setting, so result == peeked
    // But in code, for EMPTY_ARRAY, it sets stack top to NONEMPTY_ARRAY and continues, so re-check:
    // Actually code sets stack top to NONEMPTY_ARRAY and continues, so nextNonWhitespace is called.
    // So here we must simulate nextNonWhitespace returning ']'
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    setBuffer("]");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_ARRAY, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_Comma() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setBuffer(",");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_SemicolonLenient() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setBuffer(";");
    setPos(0);
    setLenient(true);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyArray_UnterminatedArray() throws Exception {
    setStackTop(JsonScope.NONEMPTY_ARRAY);
    setStackSize(1);
    setBuffer("x");
    setPos(0);
    setLenient(false);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause().getMessage().contains("Unterminated array"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_QuotedName() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setBuffer("\"");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED_NAME, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_SingleQuotedNameLenient() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setBuffer("'");
    setPos(0);
    setLenient(true);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED_NAME, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_EndObject() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setBuffer("}");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_END_OBJECT, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyObject_ExpectedNameThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_OBJECT);
    setStackSize(1);
    setBuffer("}");
    setPos(0);
    setLenient(false);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause().getMessage().contains("Expected name"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_Colon() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);
    setBuffer(":");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_EqualArrowLenient() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);
    setBuffer("=>");
    setPos(0);
    setLenient(true);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, res);
    int pos = (int) getField(jsonReader, "pos");
    assertEquals(2, pos);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_DanglingName_ExpectedColonThrows() throws Exception {
    setStackTop(JsonScope.DANGLING_NAME);
    setStackSize(1);
    setBuffer("x");
    setPos(0);
    setLenient(false);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause().getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_EmptyDocument_LenientConsumesPrefix() throws Exception {
    setStackTop(JsonScope.EMPTY_DOCUMENT);
    setStackSize(1);
    setLenient(true);
    // We will spy and verify consumeNonExecutePrefix called by setting a subclass with override
    JsonReader spyReader = new JsonReader(mockReader) {
      boolean consumed = false;

      @Override
      void consumeNonExecutePrefix() {
        consumed = true;
      }
    };
    setField(spyReader, "stack", new int[32]);
    setField(spyReader, "stackSize", 1);
    int[] stack = (int[]) getField(spyReader, "stack");
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    setField(spyReader, "lenient", true);
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    int res = (int) doPeekMethod.invoke(spyReader);
    int peeked = (int) getField(spyReader, "peeked");
    assertEquals(JsonReader.PEEKED_NONE, res);
    assertEquals(JsonScope.NONEMPTY_DOCUMENT, ((int[]) getField(spyReader, "stack"))[0]);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyDocument_Eof() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_EOF, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_NonEmptyDocument_NonEofLenient() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("x");
    setPos(0);
    setLenient(true);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NONE, res);
    int pos = (int) getField(jsonReader, "pos");
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_ClosedThrows() throws Exception {
    setStackTop(JsonScope.CLOSED);
    setStackSize(1);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause() instanceof IllegalStateException);
    assertTrue(ex.getCause().getMessage().contains("JsonReader is closed"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_ArrayCommaNullLenient() throws Exception {
    setStackTop(JsonScope.EMPTY_ARRAY);
    setStackSize(1);
    setBuffer(",");
    setPos(0);
    setLenient(true);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_NULL, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_ArrayCommaUnexpectedValueThrows() throws Exception {
    setStackTop(JsonScope.EMPTY_OBJECT);
    setStackSize(1);
    setBuffer(",");
    setPos(0);
    setLenient(false);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause().getMessage().contains("Unexpected value"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralSingleQuoteLenient() throws Exception {
    setStackTop(JsonScope.EMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("'");
    setPos(0);
    setLenient(true);
    // We simulate the flow to reach case '\''
    // Actually EMPTY_DOCUMENT will move to NONEMPTY_DOCUMENT and then nextNonWhitespace(true) will be called
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    int res = (int) invokeDoPeek();
    // Because buffer is "'", it should return PEEKED_SINGLE_QUOTED
    assertEquals(JsonReader.PEEKED_SINGLE_QUOTED, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralDoubleQuote() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("\"");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_DOUBLE_QUOTED, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralBeginArray() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("[");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_BEGIN_ARRAY, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralBeginObject() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("{");
    setPos(0);
    setLenient(false);
    int res = (int) invokeDoPeek();
    assertEquals(JsonReader.PEEKED_BEGIN_OBJECT, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_LiteralUnrecognizedThrows() throws Exception {
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("\u0000");
    setPos(0);
    setLenient(false);
    Exception ex = assertThrows(Exception.class, () -> invokeDoPeek());
    assertTrue(ex.getCause().getMessage().contains("Expected value"));
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_PeekKeywordReturns() throws Exception {
    // We simulate peekKeyword returns PEEKED_TRUE (5)
    JsonReader spyReader = spy(jsonReader);
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("t");
    setPos(0);
    setLenient(false);
    doReturn(JsonReader.PEEKED_TRUE).when(spyReader).getClass().getDeclaredMethod("peekKeyword").invoke(spyReader);
    // Because mocking private method via reflection is complicated, we use doReturn for public methods only
    // So instead, we override peekKeyword by subclass for this test
    JsonReader reader = new JsonReader(mockReader) {
      {
        setStackTop(JsonScope.NONEMPTY_DOCUMENT);
        setStackSize(1);
        setBuffer("t");
        setPos(0);
        setLenient(false);
      }

      @Override
      private int peekKeyword() {
        return PEEKED_TRUE;
      }

      @Override
      private int peekNumber() {
        return PEEKED_NONE;
      }
    };
    setStackTop(JsonScope.NONEMPTY_DOCUMENT);
    setStackSize(1);
    setBuffer("t");
    setPos(0);
    setLenient(false);
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    int res = (int) doPeekMethod.invoke(reader);
    assertEquals(JsonReader.PEEKED_TRUE, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_PeekNumberReturns() throws Exception {
    JsonReader reader = new JsonReader(mockReader) {
      {
        setStackTop(JsonScope.NONEMPTY_DOCUMENT);
        setStackSize(1);
        setBuffer("1");
        setPos(0);
        setLenient(false);
      }

      @Override
      private int peekKeyword() {
        return PEEKED_NONE;
      }

      @Override
      private int peekNumber() {
        return PEEKED_NUMBER;
      }
    };
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    int res = (int) doPeekMethod.invoke(reader);
    assertEquals(JsonReader.PEEKED_NUMBER, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_IsLiteralTrueReturnsUnquoted() throws Exception {
    JsonReader reader = new JsonReader(mockReader) {
      {
        setStackTop(JsonScope.NONEMPTY_DOCUMENT);
        setStackSize(1);
        setBuffer("a");
        setPos(0);
        setLenient(true);
      }

      @Override
      private int peekKeyword() {
        return PEEKED_NONE;
      }

      @Override
      private int peekNumber() {
        return PEEKED_NONE;
      }

      @Override
      private boolean isLiteral(char c) {
        return true;
      }
    };
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    int res = (int) doPeekMethod.invoke(reader);
    assertEquals(JsonReader.PEEKED_UNQUOTED, res);
  }

  @Test
    @Timeout(8000)
  public void testDoPeek_IsLiteralFalseThrows() throws Exception {
    JsonReader reader = new JsonReader(mockReader) {
      {
        setStackTop(JsonScope.NONEMPTY_DOCUMENT);
        setStackSize(1);
        setBuffer("a");
        setPos(0);
        setLenient(true);
      }

      @Override
      private int peekKeyword() {
        return PEEKED_NONE;
      }

      @Override
      private int peekNumber() {
        return PEEKED_NONE;
      }

      @Override
      private boolean isLiteral(char c) {
        return false;
      }
    };
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    Exception ex = assertThrows(Exception.class, () -> doPeekMethod.invoke(reader));
    assertTrue(ex.getCause().getMessage().contains("Expected value"));
  }
}