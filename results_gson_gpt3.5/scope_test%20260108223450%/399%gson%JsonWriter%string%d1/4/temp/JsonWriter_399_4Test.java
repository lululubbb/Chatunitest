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
  void string_writesQuotedStringWithoutReplacements() throws IOException, InvocationTargetException, IllegalAccessException {
    String input = "simpleText123";
    // htmlSafe = false by default
    setField(jsonWriter, "htmlSafe", false);

    stringMethod.invoke(jsonWriter, input);

    // Verify output: starts and ends with quote, writes the entire string once
    verify(mockWriter).write('\"');
    verify(mockWriter).write(input, 0, input.length());
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_writesQuotedStringWithReplacementChars() throws IOException, InvocationTargetException, IllegalAccessException {
    // Prepare a string with characters that need replacement (e.g. control chars)
    // According to REPLACEMENT_CHARS, chars < 128 with non-null replacement cause replacement
    // We'll test with char 0x00 (null char), which is replaced by "\\u0000" in REPLACEMENT_CHARS
    String input = "a\u0000b";
    setField(jsonWriter, "htmlSafe", false);

    stringMethod.invoke(jsonWriter, input);

    // Should write: "a\u0000b"
    // So: write('"'), write("a",0,1), write("\\u0000"), write("b",2,1), write('"')
    verify(mockWriter).write('\"');
    verify(mockWriter).write(input, 0, 1);
    verify(mockWriter).write("\\u0000");
    verify(mockWriter).write(input, 2, 1);
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_writesQuotedStringWithHtmlSafeReplacementChars() throws IOException, InvocationTargetException, IllegalAccessException {
    // Enable htmlSafe to use HTML_SAFE_REPLACEMENT_CHARS array
    setField(jsonWriter, "htmlSafe", true);

    // Use character that is replaced in HTML_SAFE_REPLACEMENT_CHARS but not in REPLACEMENT_CHARS
    // For example, '<' character (char code 60) replaced by "\\u003c"
    String input = "x<y";

    stringMethod.invoke(jsonWriter, input);

    verify(mockWriter).write('\"');
    verify(mockWriter).write(input, 0, 1);
    verify(mockWriter).write("\\u003c");
    verify(mockWriter).write(input, 2, 1);
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_writesQuotedStringWithUnicode2028And2029() throws IOException, InvocationTargetException, IllegalAccessException {
    // Test with \u2028 and \u2029 characters which have special replacement strings
    String input = "a\u2028b\u2029c";
    setField(jsonWriter, "htmlSafe", false);

    stringMethod.invoke(jsonWriter, input);

    verify(mockWriter).write('\"');
    verify(mockWriter).write(input, 0, 1); // "a"
    verify(mockWriter).write("\\u2028");
    verify(mockWriter).write(input, 2, 1); // "b"
    verify(mockWriter).write("\\u2029");
    verify(mockWriter).write(input, 4, 1); // "c"
    verify(mockWriter).write('\"');
  }

  @Test
    @Timeout(8000)
  void string_writesEmptyString() throws IOException, InvocationTargetException, IllegalAccessException {
    String input = "";
    setField(jsonWriter, "htmlSafe", false);

    stringMethod.invoke(jsonWriter, input);

    verify(mockWriter).write('\"');
    verify(mockWriter, never()).write(any(String.class), anyInt(), anyInt());
    verify(mockWriter).write('\"');
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      var field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}