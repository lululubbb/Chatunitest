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

class JsonWriter_string_Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  private Method stringMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Access private method string(String)
    stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void string_writesSimpleStringWithoutReplacements() throws Throwable {
    String input = "simple text";
    invokeString(input);
    assertEquals("\"simple text\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_escapesControlCharacters() throws Throwable {
    // Control chars < 128 with replacements in REPLACEMENT_CHARS
    // We test a string with quotes, backslash, tab, newline
    String input = "a\"b\\c\td\ne\rf";
    invokeString(input);
    // Expected output should escape ", \, \t, \n, \r as per REPLACEMENT_CHARS rules
    // REPLACEMENT_CHARS escapes: \b, \t, \n, \f, \r, " and \\
    String expected = "\"a\\\"b\\\\c\\td\\ne\\rf\"";
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_escapesUnicode2028And2029() throws Throwable {
    String input = "line separator\u2028paragraph separator\u2029end";
    invokeString(input);
    String expected = "\"line separator\\u2028paragraph separator\\u2029end\"";
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_writesHtmlSafeEscapes() throws Throwable {
    // Enable htmlSafe to use HTML_SAFE_REPLACEMENT_CHARS
    jsonWriter.setHtmlSafe(true);
    String input = "<script>";
    invokeString(input);
    // HTML_SAFE_REPLACEMENT_CHARS escapes < and > among others
    // So < becomes \u003c and > becomes \u003e
    String expected = "\"\\u003cscript\\u003e\"";
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_writesEmptyString() throws Throwable {
    String input = "";
    invokeString(input);
    assertEquals("\"\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_writesStringWithMultipleReplacements() throws Throwable {
    // String with multiple chars that require replacements
    String input = "a\"b\\c\u2028\u2029<>";
    jsonWriter.setHtmlSafe(true);
    invokeString(input);
    String expected = "\"a\\\"b\\\\c\\u2028\\u2029\\u003c\\u003e\"";
    assertEquals(expected, stringWriter.toString());
  }

  private void invokeString(String input) throws Throwable {
    try {
      stringMethod.invoke(jsonWriter, input);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}