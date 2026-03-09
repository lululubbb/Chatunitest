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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonWriter_394_5Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValue_long_basic() throws IOException {
    jsonWriter.value(123L);
    assertEquals("123", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_callsWriteDeferredNameAndBeforeValue() throws Exception {
    // Spy on JsonWriter to verify internal private method calls via reflection
    JsonWriter spyWriter = spy(new JsonWriter(new StringWriter()));

    // Use reflection to make writeDeferredName accessible
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    // Use reflection to make beforeValue accessible
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Before calling value(long), begin an array to allow multiple top-level values
    spyWriter.beginArray();

    // Call value(long)
    spyWriter.value(456L);

    // Manually invoke private methods to ensure they are accessible and do not throw
    writeDeferredName.invoke(spyWriter);
    beforeValue.invoke(spyWriter);

    // End the array to complete JSON structure
    spyWriter.endArray();

    // Access private field 'out' via reflection
    Field outField = JsonWriter.class.getDeclaredField("out");
    outField.setAccessible(true);
    StringWriter outWriter = (StringWriter) outField.get(spyWriter);

    // The output should contain the long value inside an array
    String output = outWriter.toString();
    assertTrue(output.contains("456"));
    assertTrue(output.startsWith("["));
    assertTrue(output.endsWith("]"));
  }

  @Test
    @Timeout(8000)
  public void testValue_long_multipleValues() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(1L);
    jsonWriter.value(2L);
    jsonWriter.value(3L);
    jsonWriter.endArray();
    assertEquals("[1,2,3]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_afterName() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("key");
    jsonWriter.value(789L);
    jsonWriter.endObject();
    assertEquals("{\"key\":789}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_lenient() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(42L);
    assertEquals("42", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_htmlSafe() throws IOException {
    jsonWriter.setHtmlSafe(true);
    jsonWriter.value(100L);
    assertEquals("100", stringWriter.toString());
  }
}