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

class JsonWriter_368_3Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void constructor_nullWriter_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> new JsonWriter(null));
    assertEquals("out == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void constructor_initialState_stackSizeIs1() throws Exception {
    // Use reflection to check private field stackSize is 0 or initial state
    var field = JsonWriter.class.getDeclaredField("stackSize");
    field.setAccessible(true);
    int stackSize = (int) field.get(jsonWriter);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  void setIndent_and_getIndent_behavior() {
    jsonWriter.setIndent("  ");
    // Using reflection to get private field indent
    String indent = getPrivateField(jsonWriter, "indent");
    assertEquals("  ", indent);
  }

  @Test
    @Timeout(8000)
  void setLenient_and_isLenient() {
    jsonWriter.setLenient(true);
    assertTrue(jsonWriter.isLenient());
    jsonWriter.setLenient(false);
    assertFalse(jsonWriter.isLenient());
  }

  @Test
    @Timeout(8000)
  void setHtmlSafe_and_isHtmlSafe() {
    jsonWriter.setHtmlSafe(true);
    assertTrue(jsonWriter.isHtmlSafe());
    jsonWriter.setHtmlSafe(false);
    assertFalse(jsonWriter.isHtmlSafe());
  }

  @Test
    @Timeout(8000)
  void setSerializeNulls_and_getSerializeNulls() {
    jsonWriter.setSerializeNulls(false);
    assertFalse(jsonWriter.getSerializeNulls());
    jsonWriter.setSerializeNulls(true);
    assertTrue(jsonWriter.getSerializeNulls());
  }

  @Test
    @Timeout(8000)
  void beginArray_and_endArray_writeCorrectJson() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value("hello");
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[\"hello\"]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void beginObject_and_endObject_writeCorrectJson() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("key").value("value");
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{\"key\":\"value\"}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void name_withoutObject_throwsIOException() throws IOException {
    IOException ex = assertThrows(IOException.class, () -> jsonWriter.name("name"));
    assertTrue(ex.getMessage().contains("Nesting problem"));
  }

  @Test
    @Timeout(8000)
  void value_nullValue_writesNull() throws IOException {
    jsonWriter.nullValue();
    jsonWriter.flush();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_writesBoolean() throws IOException {
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.flush();
    assertEquals("truefalse", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_Number_writesNumber() throws IOException {
    jsonWriter.value(123);
    jsonWriter.value(45.67);
    jsonWriter.value(new BigDecimal("89.01"));
    jsonWriter.flush();
    assertEquals("12345.6789.01", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void jsonValue_writesRawJson() throws IOException {
    jsonWriter.jsonValue("{\"raw\":true}");
    jsonWriter.flush();
    assertEquals("{\"raw\":true}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void close_flushesAndClosesWriter() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter writer = new JsonWriter(mockWriter);
    writer.close();
    verify(mockWriter).flush();
    verify(mockWriter).close();
  }

  @Test
    @Timeout(8000)
  void flush_callsWriterFlush() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter writer = new JsonWriter(mockWriter);
    writer.flush();
    verify(mockWriter).flush();
  }

  @Test
    @Timeout(8000)
  void private_string_method_escapesCorrectly() throws Throwable {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);

    jsonWriter.beginArray();
    stringMethod.invoke(jsonWriter, "a\"b\\c\n");
    jsonWriter.endArray();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\"a\\\"b\\\\c\\n\""));
  }

  @Test
    @Timeout(8000)
  void private_newline_method_writesIndentAndNewline() throws Throwable {
    jsonWriter.setIndent("  ");
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);

    jsonWriter.beginObject();
    newlineMethod.invoke(jsonWriter);
    jsonWriter.name("a").value("b");
    jsonWriter.endObject();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\n  "));
  }

  @Test
    @Timeout(8000)
  void private_beforeName_and_beforeValue_methods_work() throws Throwable {
    Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeName.setAccessible(true);
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    jsonWriter.beginObject();
    beforeName.invoke(jsonWriter);
    jsonWriter.name("key");
    beforeValue.invoke(jsonWriter);
    jsonWriter.value("val");
    jsonWriter.endObject();
    jsonWriter.flush();

    String output = stringWriter.toString();
    assertEquals("{\"key\":\"val\"}", output);
  }

  @Test
    @Timeout(8000)
  void push_peek_replaceTop_methods_work_asExpected() throws Exception {
    Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
    push.setAccessible(true);
    Method peek = JsonWriter.class.getDeclaredMethod("peek");
    peek.setAccessible(true);
    Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTop.setAccessible(true);

    push.invoke(jsonWriter, 1);
    int top = (int) peek.invoke(jsonWriter);
    assertEquals(1, top);

    replaceTop.invoke(jsonWriter, 2);
    top = (int) peek.invoke(jsonWriter);
    assertEquals(2, top);
  }

  @Test
    @Timeout(8000)
  void open_and_close_methods_work_correctly() throws Throwable {
    Method open = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    open.setAccessible(true);
    Method close = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    close.setAccessible(true);

    open.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
    jsonWriter.value("x");
    close.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
    jsonWriter.flush();

    assertEquals("[\"x\"]", stringWriter.toString());
  }

  private String getPrivateField(JsonWriter writer, String fieldName) {
    try {
      var field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (String) field.get(writer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}