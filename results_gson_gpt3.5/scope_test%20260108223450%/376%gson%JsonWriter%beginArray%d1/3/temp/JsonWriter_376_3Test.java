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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_376_3Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldOpenEmptyArray() throws IOException {
    JsonWriter writer = jsonWriter.beginArray();
    assertSame(jsonWriter, writer);
    assertEquals('[', stringWriter.toString().charAt(0));
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldWriteDeferredNameIfSet() throws Exception {
    // Use reflection to set deferredName field
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    // Set stack and stackSize to valid state to avoid nesting problem
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = JsonScope.EMPTY_OBJECT;
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Also set the separator field to ":" (default) to ensure correct output
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);
    separatorField.set(jsonWriter, ":");

    // Now call beginArray, which internally calls writeDeferredName
    JsonWriter result = jsonWriter.beginArray();
    assertSame(jsonWriter, result);

    // deferredName should be null after beginArray
    Object deferredNameAfter = deferredNameField.get(jsonWriter);
    assertNull(deferredNameAfter);

    // The output should start with {"name":[
    String output = stringWriter.toString();
    assertTrue(output.startsWith("{\"name\":["), "Output should start with {\"name\":[ but was: " + output);
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldThrowIOExceptionIfUnderlyingWriterThrows() throws IOException {
    Writer failingWriter = mock(Writer.class);
    doThrow(new IOException("fail")).when(failingWriter).write(anyInt());
    JsonWriter failingJsonWriter = new JsonWriter(failingWriter);

    IOException thrown = assertThrows(IOException.class, failingJsonWriter::beginArray);
    assertEquals("fail", thrown.getMessage());
  }
}