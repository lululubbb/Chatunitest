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

public class JsonWriter_368_5Test {
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
  public void constructor_initialStackAndStackSize() throws Exception {
    // Use reflection to check private fields
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    assertNotNull(stack);
    assertEquals(32, stack.length);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonWriter);
    // stackSize is 0 initially
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void setIndent_and_getIndent() throws Exception {
    jsonWriter.setIndent("  ");
    var indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    assertEquals("  ", indentField.get(jsonWriter));
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
  public void beginArray_and_endArray_writesBrackets() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void beginObject_and_endObject_writesBraces() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void name_and_value_writesJsonNameAndValue() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("key").value("value");
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{\"key\":\"value\"}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void nullValue_writesNull() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.nullValue();
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[null]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_boolean_and_Value_Boolean() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(Boolean.FALSE);
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[true,false]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_float_and_value_double() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(1.5f);
    jsonWriter.value(2.5d);
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[1.5,2.5]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_long() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(1234567890123L);
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[1234567890123]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_Number_validAndInvalid() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(new Integer(10));
    jsonWriter.value(new BigDecimal("3.14"));
    jsonWriter.value(new BigInteger("1234567890"));
    jsonWriter.value(null);
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[10,3.14,1234567890,null]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_Number_invalidNumber_throws() {
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.POSITIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Float.NaN));
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Float.POSITIVE_INFINITY));
  }

  @Test
    @Timeout(8000)
  public void jsonValue_writesRawJson() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.jsonValue("true");
    jsonWriter.jsonValue("null");
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[true,null]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void close_flushesAndClosesWriter() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter jw = new JsonWriter(mockWriter);
    jw.close();
    verify(mockWriter).flush();
    verify(mockWriter).close();
  }

  @Test
    @Timeout(8000)
  public void flush_callsFlushOnWriter() throws IOException {
    Writer mockWriter = mock(Writer.class);
    JsonWriter jw = new JsonWriter(mockWriter);
    jw.flush();
    verify(mockWriter).flush();
  }

  @Test
    @Timeout(8000)
  public void open_pushAndPeekStack() throws Exception {
    Method open = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    open.setAccessible(true);
    JsonWriter returned = (JsonWriter) open.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');
    assertSame(jsonWriter, returned);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonWriter);
    assertEquals(1, stackSize);

    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void close_pushAndPeekStack() throws Exception {
    Method open = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    open.setAccessible(true);
    open.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');

    Method close = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    close.setAccessible(true);
    JsonWriter returned = (JsonWriter) close.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');
    assertSame(jsonWriter, returned);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = stackSizeField.getInt(jsonWriter);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void push_and_peek_and_replaceTop() throws Exception {
    Method push = JsonWriter.class.getDeclaredMethod("push", int.class);
    push.setAccessible(true);
    push.invoke(jsonWriter, JsonScope.EMPTY_OBJECT);

    Method peek = JsonWriter.class.getDeclaredMethod("peek");
    peek.setAccessible(true);
    int top = (int) peek.invoke(jsonWriter);
    assertEquals(JsonScope.EMPTY_OBJECT, top);

    Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTop.setAccessible(true);
    replaceTop.invoke(jsonWriter, JsonScope.NONEMPTY_OBJECT);
    int replacedTop = (int) peek.invoke(jsonWriter);
    assertEquals(JsonScope.NONEMPTY_OBJECT, replacedTop);
  }

  @Test
    @Timeout(8000)
  public void name_deferredName_and_writeDeferredName() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    jsonWriter.beginObject();
    jsonWriter.name("a");
    jsonWriter.value("b");
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{\"a\":\"b\"}", stringWriter.toString());

    // test private writeDeferredName via reflection
    jsonWriter.beginObject();
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "foo");

    writeDeferredName.invoke(jsonWriter);
    jsonWriter.value("bar");
    jsonWriter.endObject();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\"foo\":\"bar\""));
  }

  @Test
    @Timeout(8000)
  public void string_privateMethod_writesEscapedString() throws Exception {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
    stringMethod.invoke(jsonWriter, "Hello\n\"World\"");
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertEquals("\"Hello\\n\\\"World\\\"\"", output);
  }

  @Test
    @Timeout(8000)
  public void newline_privateMethod_writesIndentation() throws Exception {
    jsonWriter.setIndent("  ");
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    jsonWriter.beginObject();
    newlineMethod.invoke(jsonWriter);
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\n  "));
  }

  @Test
    @Timeout(8000)
  public void beforeName_and_beforeValue_privateMethods() throws Exception {
    jsonWriter.beginObject();
    Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeName.setAccessible(true);
    beforeName.invoke(jsonWriter);

    jsonWriter.name("test");
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);
    beforeValue.invoke(jsonWriter);

    jsonWriter.value("val");
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{\"test\":\"val\"}", stringWriter.toString());
  }
}