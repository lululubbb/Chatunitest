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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void value_booleanTrue_writesTrue() throws IOException {
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_booleanFalse_writesFalse() throws IOException {
    jsonWriter.value(false);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_callsWriteDeferredNameAndBeforeValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Use reflection to invoke private writeDeferredName and beforeValue to verify no exceptions
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);
    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Setup deferredName to test writeDeferredName path
    // Use reflection to set deferredName field
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    // Setup stack and stackSize to a valid state for beforeValue
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = JsonScope.EMPTY_OBJECT;
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Call writeDeferredName and beforeValue directly
    writeDeferredName.invoke(jsonWriter);
    beforeValue.invoke(jsonWriter);

    // Now call value(true) which calls these methods internally and writes "true"
    jsonWriter.value(true);

    assertEquals("{\"name\":true}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_withLenientTrue_writesBoolean() throws IOException {
    jsonWriter.setLenient(true);
    jsonWriter.value(false);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_withHtmlSafeTrue_writesBoolean() throws IOException {
    jsonWriter.setHtmlSafe(true);
    jsonWriter.value(true);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_multipleCalls_writesMultipleValues() throws IOException {
    jsonWriter.value(true);
    jsonWriter.value(false);
    assertEquals("truefalse", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_afterBeginArray_writesWithComma() throws IOException {
    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.endArray();
    assertEquals("[true,false]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_boolean_afterBeginObjectWithName_writesNameAndValue() throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("bool");
    jsonWriter.value(true);
    jsonWriter.endObject();
    assertEquals("{\"bool\":true}", stringWriter.toString());
  }
}