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
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonWriter_380_2Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyArray() throws Exception {
    invokeOpenAndVerify(EMPTY_ARRAY, '[');
  }

  @Test
    @Timeout(8000)
  public void testOpen_emptyObject() throws Exception {
    invokeOpenAndVerify(EMPTY_OBJECT, '{');
  }

  // Helper to invoke private open(int, char) and verify behavior
  private void invokeOpenAndVerify(int empty, char openBracket)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
    // Use reflection to get private open method
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // Instead of spying and mocking private methods (which is not allowed),
    // just invoke the open method directly on the real jsonWriter instance.
    JsonWriter result = (JsonWriter) openMethod.invoke(jsonWriter, empty, openBracket);

    // Verify output written contains the openBracket character
    String output = stringWriter.toString();
    assertTrue(output.indexOf(openBracket) >= 0);

    // Result should be the jsonWriter itself (this)
    assertSame(jsonWriter, result);
  }

  // Constants from JsonScope to use in tests
  private static final int EMPTY_ARRAY = 1;
  private static final int EMPTY_OBJECT = 3;
}