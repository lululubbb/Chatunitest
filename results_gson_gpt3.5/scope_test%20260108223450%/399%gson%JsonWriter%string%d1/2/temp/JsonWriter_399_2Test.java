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

class JsonWriter_string_Test {

  private JsonWriter jsonWriter;
  private Writer mockWriter;
  private Method stringMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
    // Access private method string(String)
    stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void string_basic_noReplacements() throws Throwable {
    String input = "abcdefg123";
    invokeString(input);

    // Expect output: "abcdefg123"
    // So write('\"'), write(value,0,length), write('\"')
    verify(mockWriter).write('\"');
    verify(mockWriter).write(input, 0, input.length());
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_withReplacementChars() throws Throwable {
    // Characters < 128 with replacements: \b, \t, \n, \f, \r, ", \, and control chars
    String input = "a\b\t\n\f\r\"\\z";
    invokeString(input);

    verify(mockWriter).write('\"');

    verify(mockWriter).write("a", 0, 1);
    verify(mockWriter).write("\\b");
    verify(mockWriter).write("\\t");
    verify(mockWriter).write("\\n");
    verify(mockWriter).write("\\f");
    verify(mockWriter).write("\\r");
    verify(mockWriter).write("\\\"");
    verify(mockWriter).write("\\\\");
    verify(mockWriter).write("z", 8, 1);

    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_withUnicode2028And2029() throws Throwable {
    String input = "start\u2028middle\u2029end";
    invokeString(input);

    verify(mockWriter).write('\"');
    verify(mockWriter).write("start", 0, 5);
    verify(mockWriter).write("\\u2028");
    verify(mockWriter).write("middle", 6, 6);
    verify(mockWriter).write("\\u2029");
    verify(mockWriter).write("end", 13, 3);
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_emptyString() throws Throwable {
    String input = "";
    invokeString(input);

    verify(mockWriter).write('\"');
    verify(mockWriter).write("", 0, 0);
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_allReplacementsHtmlSafe() throws Throwable {
    // Enable htmlSafe to use HTML_SAFE_REPLACEMENT_CHARS
    jsonWriter.setHtmlSafe(true);

    // Construct a string containing characters that have replacements in HTML_SAFE_REPLACEMENT_CHARS
    // From Gson source, HTML_SAFE_REPLACEMENT_CHARS replaces <, >, &, =, '
    String input = "<>&='";

    invokeString(input);

    verify(mockWriter).write('\"');
    verify(mockWriter).write("\\u003c"); // <
    verify(mockWriter).write("\\u003e"); // >
    verify(mockWriter).write("\\u0026"); // &
    verify(mockWriter).write("\\u003d"); // =
    verify(mockWriter).write("\\u0027"); // '
    verify(mockWriter).write('\"');
  }

  private void invokeString(String input) throws Throwable {
    try {
      stringMethod.invoke(jsonWriter, input);
    } catch (InvocationTargetException e) {
      // Unwrap IOException or rethrow
      throw e.getCause();
    }
  }
}