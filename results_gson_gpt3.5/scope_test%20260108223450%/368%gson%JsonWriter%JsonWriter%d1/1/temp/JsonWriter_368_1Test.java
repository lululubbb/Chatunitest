package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_368_1Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void constructor_nullWriter_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> new JsonWriter(null));
    assertEquals("out == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void constructor_validWriter_initializesFields() {
    assertNotNull(jsonWriter);
    // Using reflection to check private fields stack and stackSize
    try {
      var stackField = JsonWriter.class.getDeclaredField("stack");
      stackField.setAccessible(true);
      int[] stack = (int[]) stackField.get(jsonWriter);
      assertEquals(32, stack.length);

      var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      int stackSize = (int) stackSizeField.get(jsonWriter);
      assertEquals(0, stackSize);

      var outField = JsonWriter.class.getDeclaredField("out");
      outField.setAccessible(true);
      Writer out = (Writer) outField.get(jsonWriter);
      assertSame(stringWriter, out);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void setIndent_and_getIndent() {
    jsonWriter.setIndent("  ");
    try {
      var indentField = JsonWriter.class.getDeclaredField("indent");
      indentField.setAccessible(true);
      String indent = (String) indentField.get(jsonWriter);
      assertEquals("  ", indent);
    } catch (Exception e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void setLenient_and_isLenient() {
    jsonWriter.setLenient(true);
    assertTrue(jsonWriter.isLenient());
    jsonWriter.setLenient(false);
    assertFalse(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  public void setHtmlSafe_and_isHtmlSafe() {
    jsonWriter.setHtmlSafe(true);
    assertTrue(jsonWriter.isHtmlSafe());
    jsonWriter.setHtmlSafe(false);
    assertFalse(jsonWriter.isHtmlSafe());
  }

  @Test
    @Timeout(8000)
  public void setSerializeNulls_and_getSerializeNulls() {
    jsonWriter.setSerializeNulls(false);
    assertFalse(jsonWriter.getSerializeNulls());
    jsonWriter.setSerializeNulls(true);
    assertTrue(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  public void name_and_writeDeferredName_writesName() throws Exception {
    jsonWriter.beginObject();
    jsonWriter.name("key");

    // Invoke private writeDeferredName method via reflection
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    writeDeferredName.invoke(jsonWriter);

    jsonWriter.value("value");
    jsonWriter.endObject();
    jsonWriter.flush();

    assertEquals("{\"key\":\"value\"}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void beginArray_and_endArray_writeBrackets() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void beginObject_and_endObject_writeBraces() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_string_writesQuotedString() throws IOException {
    jsonWriter.value("test");
    jsonWriter.flush();
    assertEquals("\"test\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_nullValue_writesNull() throws IOException {
    jsonWriter.nullValue();
    jsonWriter.flush();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_boolean_writesBoolean() throws IOException {
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.flush();
    assertEquals("truefalse", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_double_writesNumber() throws IOException {
    jsonWriter.value(1.5);
    jsonWriter.flush();
    assertEquals("1.5", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_float_writesNumber() throws IOException {
    jsonWriter.value(2.5f);
    jsonWriter.flush();
    assertEquals("2.5", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_long_writesNumber() throws IOException {
    jsonWriter.value(10L);
    jsonWriter.flush();
    assertEquals("10", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_Number_writesNumber() throws IOException {
    jsonWriter.value(new java.math.BigDecimal("123.456"));
    jsonWriter.flush();
    assertEquals("123.456", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_Number_null_writesNull() throws IOException {
    jsonWriter.value((Number) null);
    jsonWriter.flush();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void jsonValue_writesRawJson() throws IOException {
    jsonWriter.jsonValue("[1,2,3]");
    jsonWriter.flush();
    assertEquals("[1,2,3]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void flush_callsOutFlush() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter writer = new JsonWriter(mockWriter);
    writer.flush();
    verify(mockWriter).flush();
  }

  @Test
    @Timeout(8000)
  public void close_callsOutClose() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter writer = new JsonWriter(mockWriter);
    writer.close();
    verify(mockWriter).close();
  }

  @Test
    @Timeout(8000)
  public void private_push_peek_replaceTop_methods() throws Exception {
    Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
    Method peek = JsonWriter.class.getDeclaredMethod("peek");
    Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    push.setAccessible(true);
    peek.setAccessible(true);
    replaceTop.setAccessible(true);

    push.invoke(jsonWriter, 5);
    int top = (int) peek.invoke(jsonWriter);
    assertEquals(5, top);

    replaceTop.invoke(jsonWriter, 10);
    int replacedTop = (int) peek.invoke(jsonWriter);
    assertEquals(10, replacedTop);
  }

  @Test
    @Timeout(8000)
  public void private_open_and_close_methods() throws Exception {
    Method open = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    Method close = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    open.setAccessible(true);
    close.setAccessible(true);

    JsonWriter returnedOpen = (JsonWriter) open.invoke(jsonWriter, 6, '[');
    assertSame(jsonWriter, returnedOpen);

    JsonWriter returnedClose = (JsonWriter) close.invoke(jsonWriter, 6, 7, ']');
    assertSame(jsonWriter, returnedClose);
  }

  @Test
    @Timeout(8000)
  public void private_string_writesEscapedString() throws Exception {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
    stringMethod.invoke(jsonWriter, "abc\"\\\b\f\n\r\t\u2028\u2029");
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.startsWith("\""));
    assertTrue(output.endsWith("\""));
    // Contains escaped characters
    assertTrue(output.contains("\\\""));
    assertTrue(output.contains("\\\\"));
    assertTrue(output.contains("\\b"));
    assertTrue(output.contains("\\f"));
    assertTrue(output.contains("\\n"));
    assertTrue(output.contains("\\r"));
    assertTrue(output.contains("\\t"));
    assertTrue(output.contains("\\u2028"));
    assertTrue(output.contains("\\u2029"));
  }

  @Test
    @Timeout(8000)
  public void private_newline_writesIndentation() throws Exception {
    jsonWriter.setIndent("  ");
    jsonWriter.beginObject();
    Method newline = JsonWriter.class.getDeclaredMethod("newline");
    newline.setAccessible(true);
    newline.invoke(jsonWriter);
    jsonWriter.name("a");
    jsonWriter.value("b");
    jsonWriter.endObject();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\n  "));
  }

  @Test
    @Timeout(8000)
  public void private_beforeName_and_beforeValue() throws Exception {
    jsonWriter.beginObject();
    Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeName.setAccessible(true);
    beforeValue.setAccessible(true);

    beforeName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);
  }
}