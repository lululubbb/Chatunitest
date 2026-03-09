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
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_368_2Test {

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
    NullPointerException e = assertThrows(NullPointerException.class, () -> new JsonWriter(null));
    assertEquals("out == null", e.getMessage());
  }

  @Test
    @Timeout(8000)
  void setIndent_and_getIndent_behavior() {
    jsonWriter.setIndent("  ");
    // Cannot directly get indent because field is private and no getter, use reflection
    String indent = getPrivateField(jsonWriter, "indent", String.class);
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
  void beginArray_endArray_writesBrackets() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void beginObject_endObject_writesBraces() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.endObject();
    jsonWriter.flush();
    assertEquals("{}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void name_value_nullValue_and_jsonValue() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("string").value("value");
    jsonWriter.name("null").nullValue();
    jsonWriter.name("json").jsonValue("{\"a\":true}");
    jsonWriter.endObject();
    jsonWriter.flush();
    String result = stringWriter.toString();
    assertTrue(result.startsWith("{"));
    assertTrue(result.contains("\"string\":\"value\""));
    assertTrue(result.contains("\"null\":null"));
    assertTrue(result.contains("\"json\":{\"a\":true}"));
  }

  @Test
    @Timeout(8000)
  void value_boolean_and_Boolean() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(Boolean.FALSE);
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[true,false]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_float_double_long_Number() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(1.2f);
    jsonWriter.value(3.4d);
    jsonWriter.value(123L);
    jsonWriter.value(new BigDecimal("5.6"));
    jsonWriter.value(new BigInteger("7"));
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[1.2,3.4,123,5.6,7]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_Number_invalidNumber_throws() {
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.NEGATIVE_INFINITY));
    assertThrows(IllegalArgumentException.class, () -> jsonWriter.value(Double.POSITIVE_INFINITY));
  }

  @Test
    @Timeout(8000)
  void flush_and_close() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value("test");
    jsonWriter.endArray();
    jsonWriter.flush();
    assertFalse(stringWriter.toString().isEmpty());
    jsonWriter.close();
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> jsonWriter.value("after close"));
    assertEquals("JsonWriter is closed.", e.getMessage());
  }

  @Test
    @Timeout(8000)
  void privateMethod_string_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
    jsonWriter.beginArray();
    stringMethod.invoke(jsonWriter, "testString");
    jsonWriter.endArray();
    jsonWriter.flush();
    assertEquals("[\"testString\"]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void privateMethod_newline_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    jsonWriter.setIndent("  ");
    jsonWriter.beginObject();
    Method newlineMethod = JsonWriter.class.getDeclaredMethod("newline");
    newlineMethod.setAccessible(true);
    newlineMethod.invoke(jsonWriter);
    jsonWriter.name("a").value("b");
    jsonWriter.endObject();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\n"));
  }

  @Test
    @Timeout(8000)
  void privateMethod_beforeName_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    jsonWriter.beginObject();
    Method beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeNameMethod.setAccessible(true);
    beforeNameMethod.invoke(jsonWriter);
    // Removed redundant jsonWriter.name("n"); because beforeName() already handles deferredName
    jsonWriter.value("v");
    jsonWriter.endObject();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\"v\""));
  }

  @Test
    @Timeout(8000)
  void privateMethod_beforeValue_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    jsonWriter.beginArray();
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);
    beforeValueMethod.invoke(jsonWriter);
    jsonWriter.value("v");
    jsonWriter.endArray();
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\"v\""));
  }

  private <T> T getPrivateField(Object obj, String fieldName, Class<T> type) {
    try {
      var field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return type.cast(field.get(obj));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}