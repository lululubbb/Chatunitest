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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
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
    stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void string_basicNoReplacement() throws Throwable {
    String input = "simpletext123";
    invokeString(input);
    String expected = "\"" + input + "\"";
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_withControlCharsReplaced() throws Throwable {
    // The REPLACEMENT_CHARS array replaces control chars < 0x20 with escape sequences.
    // We test a string containing some control chars (\b \t \n \f \r and quotes, backslash)
    String input = "a\b\t\n\f\r\"\\z";
    // Expected output: quotes around string and escaped control chars replaced
    // According to Gson source REPLACEMENT_CHARS:
    // \b -> \\b
    // \t -> \\t
    // \n -> \\n
    // \f -> \\f
    // \r -> \\r
    // " -> \"
    // \ -> \\
    String expected = "\"a\\b\\t\\n\\f\\r\\\"\\\\z\"";
    invokeString(input);
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_withUnicode2028and2029() throws Throwable {
    String input = "line\u2028separator\u2029end";
    // \u2028 and \u2029 replaced with unicode escapes
    String expected = "\"line\\u2028separator\\u2029end\"";
    invokeString(input);
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_withHtmlSafeTrue_usesHtmlSafeReplacements() throws Throwable {
    // Set htmlSafe to true via reflection
    setField(jsonWriter, "htmlSafe", true);
    // The HTML_SAFE_REPLACEMENT_CHARS escapes &, <, >, =, and '
    // Let's test a string containing these
    String input = "a&<>=\'b";
    // Expected output escapes these chars:
    // & -> \u0026
    // < -> \u003c
    // > -> \u003e
    // = -> \u003d
    // ' -> \u0027
    String expected = "\"a\\u0026\\u003c\\u003e\\u003d\\u0027b\"";
    invokeString(input);
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_emptyString() throws Throwable {
    String input = "";
    String expected = "\"\"";
    invokeString(input);
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void string_allCharsReplaced() throws Throwable {
    // Compose a string containing all control chars 0-31 and special html safe chars
    StringBuilder sb = new StringBuilder();
    for (char c = 0; c < 32; c++) {
      sb.append(c);
    }
    sb.append("&<>='\"");
    String input = sb.toString();

    // Set htmlSafe true to test HTML_SAFE_REPLACEMENT_CHARS
    setField(jsonWriter, "htmlSafe", true);

    // We can't hardcode expected easily, so we test that output starts and ends with quotes
    // and contains replacement sequences for control chars and html safe chars
    invokeString(input);
    String output = stringWriter.toString();
    assertTrue(output.startsWith("\""));
    assertTrue(output.endsWith("\""));
    // Check that control chars replaced by backslash escapes or unicode escapes
    for (char c = 0; c < 32; c++) {
      String replacement = getReplacementChar(c, true);
      if (replacement != null) {
        assertTrue(output.contains(replacement), "Output missing replacement for char code " + (int)c);
      }
    }
    // Check html safe replacements
    assertTrue(output.contains("\\u0026")); // &
    assertTrue(output.contains("\\u003c")); // <
    assertTrue(output.contains("\\u003e")); // >
    assertTrue(output.contains("\\u003d")); // =
    assertTrue(output.contains("\\u0027")); // '
  }

  // Helper to invoke private string method and rethrow IOException as Throwable
  private void invokeString(String value) throws Throwable {
    try {
      stringMethod.invoke(jsonWriter, value);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  // Helper to set private field via reflection
  private void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get replacement char from REPLACEMENT_CHARS or HTML_SAFE_REPLACEMENT_CHARS array
  private String getReplacementChar(char c, boolean htmlSafe) {
    try {
      var field = JsonWriter.class.getDeclaredField(htmlSafe ? "HTML_SAFE_REPLACEMENT_CHARS" : "REPLACEMENT_CHARS");
      field.setAccessible(true);
      String[] replacements = (String[]) field.get(null);
      if (c < replacements.length) {
        return replacements[c];
      }
      return null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}