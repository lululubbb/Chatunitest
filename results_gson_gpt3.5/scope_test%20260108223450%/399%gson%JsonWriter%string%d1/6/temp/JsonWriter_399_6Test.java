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
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_string_Test {

  private JsonWriter jsonWriter;
  private Writer out;

  private String[] replacementChars;
  private String[] htmlSafeReplacementChars;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    out = mock(Writer.class);
    jsonWriter = new JsonWriter(out);

    // Access private static final String[] REPLACEMENT_CHARS via reflection
    Field replacementCharsField = JsonWriter.class.getDeclaredField("REPLACEMENT_CHARS");
    replacementCharsField.setAccessible(true);
    replacementChars = (String[]) replacementCharsField.get(null);

    // Access private static final String[] HTML_SAFE_REPLACEMENT_CHARS via reflection
    Field htmlSafeReplacementCharsField = JsonWriter.class.getDeclaredField("HTML_SAFE_REPLACEMENT_CHARS");
    htmlSafeReplacementCharsField.setAccessible(true);
    htmlSafeReplacementChars = (String[]) htmlSafeReplacementCharsField.get(null);
  }

  private void invokeString(String value) throws Throwable {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
    try {
      stringMethod.invoke(jsonWriter, value);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void string_writesSimpleString() throws Throwable {
    String input = "simple";
    invokeString(input);
    verify(out).write('\"');
    verify(out).write(input, 0, input.length());
    verify(out).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_escapesAsciiControlCharacters() throws Throwable {
    String input = "a\u0000b\u0001c\u001Fb";
    // '\u0000' -> replacement chars at 0
    // '\u0001' -> replacement chars at 1
    // '\u001f' -> replacement chars at 31
    invokeString(input);
    verify(out).write('\"');
    // The method writes parts of the string and replacements separately
    verify(out).write(input, 0, 1); // 'a'
    verify(out).write(replacementChars[0]);
    verify(out).write(input, 2, 1); // 'b'
    verify(out).write(replacementChars[1]);
    verify(out).write(input, 4, 1); // 'c'
    verify(out).write(replacementChars[31]);
    verify(out).write(input, 5, 1); // 'b'
    verify(out).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_escapesUnicode2028And2029() throws Throwable {
    String input = "a\u2028b\u2029c";
    invokeString(input);
    verify(out).write('\"');
    verify(out).write(input, 0, 1); // 'a'
    verify(out).write("\\u2028");
    verify(out).write(input, 2, 1); // 'b'
    verify(out).write("\\u2029");
    verify(out).write(input, 4, 1); // 'c'
    verify(out).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_htmlSafeReplacements() throws Throwable {
    // Enable htmlSafe to use HTML_SAFE_REPLACEMENT_CHARS
    jsonWriter.setHtmlSafe(true);
    String input = "<>&\"'";
    // These characters have htmlSafe replacements
    invokeString(input);
    verify(out).write('\"');
    verify(out).write(input, 0, 1); // '<'
    verify(out).write(htmlSafeReplacementChars['<']);
    verify(out).write(input, 1, 1); // '>'
    verify(out).write(htmlSafeReplacementChars['>']);
    verify(out).write(input, 2, 1); // '&'
    verify(out).write(htmlSafeReplacementChars['&']);
    verify(out).write(input, 3, 1); // '"'
    verify(out).write(htmlSafeReplacementChars['"']);
    verify(out).write(input, 4, 1); // '\''
    verify(out).write(htmlSafeReplacementChars['\'']);
    verify(out).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_emptyString() throws Throwable {
    String input = "";
    invokeString(input);
    verify(out).write('\"');
    verify(out).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_allUnescapedChars() throws Throwable {
    // String with no chars needing escaping
    String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
    invokeString(input);
    verify(out).write('\"');
    verify(out).write(input, 0, input.length());
    verify(out).write('\"');
  }
}