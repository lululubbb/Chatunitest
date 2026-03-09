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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_399_3Test {

  private JsonWriter jsonWriter;
  private Writer mockWriter;

  private Method stringMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
    // Access private method string(String)
    stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testString_noSpecialChars_writesQuotedString() throws Throwable {
    String input = "simpleString";
    invokeString(input);
    // Expect writes: '"' + input + '"'
    verify(mockWriter, times(2)).write('\"');
    verify(mockWriter).write(input, 0, input.length());
    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testString_withAsciiReplacementChars() throws Throwable {
    // Characters < 128 that have replacements:
    // From Gson source, REPLACEMENT_CHARS for control chars and quotes/backslash:
    // We'll test with a string containing '"' (quote), '\\' (backslash), '\n' (newline)
    String input = "a\"b\\c\nd";
    invokeString(input);

    // Verify writes:
    // '"' at start and end
    verify(mockWriter, times(2)).write('\"');
    // Portions of string and replacements:
    // 'a' no replacement
    verify(mockWriter).write(input, 0, 1);
    // '"' replaced by \"
    verify(mockWriter).write("\\\"", 0, 2);
    // 'b' no replacement
    verify(mockWriter).write(input, 3, 1);
    // '\' replaced by \\
    verify(mockWriter).write("\\\\", 0, 2);
    // 'c' no replacement
    verify(mockWriter).write(input, 5, 1);
    // '\n' replaced by \n
    verify(mockWriter).write("\\n", 0, 2);
    // 'd' no replacement
    verify(mockWriter).write(input, 7, 1);

    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testString_withUnicode2028And2029_replacements() throws Throwable {
    // \u2028 and \u2029 must be replaced by their unicode escapes
    String input = "line\u2028separator\u2029end";
    invokeString(input);

    // Only two writes of '"' expected, so verify exactly twice
    verify(mockWriter, times(2)).write('\"');

    // "line" no replacement
    verify(mockWriter).write(input, 0, 4);
    // \u2028 replaced by "\\u2028"
    verify(mockWriter).write("\\u2028");
    // "separator" no replacement
    verify(mockWriter).write(input, 5, 9);
    // \u2029 replaced by "\\u2029"
    verify(mockWriter).write("\\u2029");
    // "end" no replacement
    verify(mockWriter).write(input, 15, 3);

    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testString_emptyString_writesQuotesOnly() throws Throwable {
    String input = "";
    invokeString(input);
    verify(mockWriter, times(2)).write('\"');
    verify(mockWriter, never()).write(anyString(), anyInt(), anyInt());
    verifyNoMoreInteractions(mockWriter);
  }

  @Test
    @Timeout(8000)
  public void testString_htmlSafeTrue_usesHtmlSafeReplacements() throws Throwable {
    // Enable htmlSafe
    jsonWriter.setHtmlSafe(true);
    // input with characters that differ in htmlSafe replacements: < > & = '
    String input = "<>&='";
    invokeString(input);

    verify(mockWriter, times(2)).write('\"');

    // Expected replacements from HTML_SAFE_REPLACEMENT_CHARS:
    // '<' -> \u003c
    verify(mockWriter).write("\\u003c", 0, 6);
    // '>' -> \u003e
    verify(mockWriter).write("\\u003e", 0, 6);
    // '&' -> \u0026
    verify(mockWriter).write("\\u0026", 0, 6);
    // '=' -> \u003d
    verify(mockWriter).write("\\u003d", 0, 6);
    // '\'' -> \u0027
    verify(mockWriter).write("\\u0027", 0, 6);

    verifyNoMoreInteractions(mockWriter);
  }

  // Helper to invoke private string method and unwrap exceptions
  private void invokeString(String input) throws Throwable {
    try {
      stringMethod.invoke(jsonWriter, input);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}