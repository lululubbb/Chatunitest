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

class JsonReaderDoPeekTest {

  JsonReader jsonReader;
  Reader mockReader;

  @BeforeEach
  void setUp() {
    mockReader = mock(Reader.class);
    jsonReader = new JsonReader(mockReader);
  }

  private int invokeDoPeek() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
    doPeekMethod.setAccessible(true);
    return (int) doPeekMethod.invoke(jsonReader);
  }

  private void setField(String fieldName, Object value) throws Exception {
    var field = JsonReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(jsonReader, value);
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyArray() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    int result = invokeDoPeek();
    assertEquals(4, result); // PEEKED_END_ARRAY
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NonEmptyArray_Comma() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("lenient", true);
    // nextNonWhitespace returns ','
    Method nextNonWhitespace = JsonReader.class.getDeclaredMethod("nextNonWhitespace", boolean.class);
    nextNonWhitespace.setAccessible(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) ',').when(spyReader).nextNonWhitespace(true);
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    setField("lenient", true);
    // Use reflection to replace jsonReader with spyReader
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(0, spyReader.peeked); // PEEKED_NONE
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NonEmptyArray_EndArray() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) ']').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(4, result); // PEEKED_END_ARRAY
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NonEmptyArray_SyntaxError() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_ARRAY});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) 'x').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Unterminated array"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyObject_QuotedName() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '"').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(13, result); // PEEKED_DOUBLE_QUOTED_NAME
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyObject_SingleQuotedName() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '\'').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(12, result); // PEEKED_SINGLE_QUOTED_NAME
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyObject_EndObject() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_OBJECT});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '}').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(2, result); // PEEKED_END_OBJECT
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyObject_SyntaxErrorExpectedName() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_OBJECT});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '}').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected name"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_DanglingName_Colon() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) ':').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  void testDoPeek_DanglingName_EqualArrow() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    setField("pos", 0);
    setField("limit", 1);
    setField("buffer", new char[] {'>'});
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '=').when(spyReader).nextNonWhitespace(true);
    doReturn(true).when(spyReader).fillBuffer(1);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(0, spyReader.peeked);
    int pos = (int) JsonReader.class.getDeclaredField("pos").get(spyReader);
    assertEquals(1, pos);
  }

  @Test
    @Timeout(8000)
  void testDoPeek_DanglingName_SyntaxErrorExpectedColon() throws Throwable {
    setField("stack", new int[] {JsonScope.DANGLING_NAME});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) 'x').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected ':'"));
  }

  @Test
    @Timeout(8000)
  void testDoPeek_EmptyDocument_Lenient() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_DOCUMENT});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    JsonReader spyReader = spy(jsonReader);
    doNothing().when(spyReader).consumeNonExecutePrefix();
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(0, spyReader.peeked);
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NonEmptyDocument_EOF() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn(-1).when(spyReader).nextNonWhitespace(false);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(17, result); // PEEKED_EOF
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NonEmptyDocument_ValidChar() throws Throwable {
    setField("stack", new int[] {JsonScope.NONEMPTY_DOCUMENT});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    setField("pos", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn('x').when(spyReader).nextNonWhitespace(false);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(0, spyReader.peeked);
    int pos = (int) JsonReader.class.getDeclaredField("pos").get(spyReader);
    assertEquals(0, pos);
  }

  @Test
    @Timeout(8000)
  void testDoPeek_Closed() throws Throwable {
    setField("stack", new int[] {JsonScope.CLOSED});
    setField("stackSize", 1);
    IllegalStateException ex = assertThrows(IllegalStateException.class, this::invokeDoPeek);
    assertEquals("JsonReader is closed", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testDoPeek_LenientSingleQuoted() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '\'').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(8, result); // PEEKED_SINGLE_QUOTED
  }

  @Test
    @Timeout(8000)
  void testDoPeek_LenientDoubleQuoted() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '"').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(9, result); // PEEKED_DOUBLE_QUOTED
  }

  @Test
    @Timeout(8000)
  void testDoPeek_LenientBeginArray() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '[').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(3, result); // PEEKED_BEGIN_ARRAY
  }

  @Test
    @Timeout(8000)
  void testDoPeek_LenientBeginObject() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) '{').when(spyReader).nextNonWhitespace(true);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(1, result); // PEEKED_BEGIN_OBJECT
  }

  @Test
    @Timeout(8000)
  void testDoPeek_LiteralExpectedValue() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    setField("pos", 0);
    setField("buffer", new char[] {'a'});
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) 'a').when(spyReader).nextNonWhitespace(true);
    doReturn(0).when(spyReader).peekKeyword();
    doReturn(0).when(spyReader).peekNumber();
    Method isLiteral = JsonReader.class.getDeclaredMethod("isLiteral", char.class);
    isLiteral.setAccessible(true);
    boolean literal = (boolean) isLiteral.invoke(spyReader, 'a');
    assertTrue(literal);
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    int result = invokeDoPeek();
    assertEquals(10, result); // PEEKED_UNQUOTED
  }

  @Test
    @Timeout(8000)
  void testDoPeek_NotLiteralThrows() throws Throwable {
    setField("stack", new int[] {JsonScope.EMPTY_ARRAY});
    setField("stackSize", 1);
    jsonReader.setLenient(true);
    setField("pos", 0);
    setField("buffer", new char[] {' '});
    JsonReader spyReader = spy(jsonReader);
    doReturn((int) ' ').when(spyReader).nextNonWhitespace(true);
    doReturn(0).when(spyReader).peekKeyword();
    doReturn(0).when(spyReader).peekNumber();
    var fieldInTest = this.getClass().getDeclaredField("jsonReader");
    fieldInTest.setAccessible(true);
    fieldInTest.set(this, spyReader);
    IOException ex = assertThrows(IOException.class, this::invokeDoPeek);
    assertTrue(ex.getMessage().contains("Expected value"));
  }
}