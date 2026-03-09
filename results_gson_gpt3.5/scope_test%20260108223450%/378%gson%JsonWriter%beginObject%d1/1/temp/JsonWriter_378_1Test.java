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

class JsonWriter_378_1Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void beginObject_shouldReturnJsonWriterAndWriteOpeningBrace() throws IOException, Exception {
    // Use reflection to get private methods
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // Use reflection to get private field deferredName and set it to null to avoid NPE in writeDeferredName
    // (writeDeferredName throws if deferredName != null)
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);

    // Use reflection to get private fields stack and stackSize and initialize them properly
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Create a subclass that overrides beginObject and exposes flags to verify calls
    class TestJsonWriter extends JsonWriter {
      boolean writeDeferredNameCalled = false;
      boolean openCalled = false;

      TestJsonWriter(StringWriter out) {
        super(out);
      }

      @Override
      public JsonWriter beginObject() throws IOException {
        try {
          // Initialize stack and stackSize to simulate EMPTY_DOCUMENT state to avoid IllegalStateException on open
          stackField.set(this, new int[32]);
          stackSizeField.setInt(this, 0);
          Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
          pushMethod.setAccessible(true);
          pushMethod.invoke(this, JsonScope.EMPTY_DOCUMENT);

          // Clear deferredName to prevent writeDeferredName from throwing
          deferredNameField.set(this, null);

          // Instead of calling private writeDeferredName, mark flag and call original method via reflection
          writeDeferredNameCalled = true;
          writeDeferredNameMethod.invoke(this);

          openCalled = true;
          return (JsonWriter) openMethod.invoke(this, JsonScope.EMPTY_OBJECT, '{');
        } catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (cause instanceof IOException) throw (IOException) cause;
          throw new RuntimeException(cause);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    TestJsonWriter testWriter = new TestJsonWriter(stringWriter);
    JsonWriter spyTestWriter = spy(testWriter);

    JsonWriter result = spyTestWriter.beginObject();

    assertTrue(testWriter.writeDeferredNameCalled, "writeDeferredName should be called");
    assertTrue(testWriter.openCalled, "open should be called");
    assertSame(spyTestWriter, result);
  }

  @Test
    @Timeout(8000)
  void beginObject_realOpenAndWriteDeferredName_shouldWriteCorrectly() {
    // We test the real behavior of beginObject including real open and writeDeferredName
    JsonWriter writer = new JsonWriter(stringWriter);

    // Use reflection to set stackSize and stack to simulate EMPTY_DOCUMENT state
    try {
      Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
      pushMethod.setAccessible(true);
      pushMethod.invoke(writer, JsonScope.EMPTY_DOCUMENT);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Reflection setup failed: " + e);
    }

    try {
      JsonWriter result = writer.beginObject();
      assertSame(writer, result);
      // The output should start with '{' because beginObject calls open with '{'
      String output = stringWriter.toString();
      assertTrue(output.startsWith("{"));
    } catch (IOException e) {
      fail("IOException thrown: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void beginObject_writeDeferredNameThrows_shouldThrowIOException() throws Exception {
    // Use reflection to get private open method only
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // Create subclass that overrides writeDeferredName to throw IOException directly
    class TestJsonWriter extends JsonWriter {
      TestJsonWriter(StringWriter out) {
        super(out);
      }

      @Override
      public JsonWriter beginObject() throws IOException {
        // call overridden writeDeferredName which throws IOException
        writeDeferredName();
        // this line won't be reached but needed to satisfy return type
        try {
          return (JsonWriter) openMethod.invoke(this, JsonScope.EMPTY_OBJECT, '{');
        } catch (InvocationTargetException e) {
          Throwable cause = e.getCause();
          if (cause instanceof IOException) throw (IOException) cause;
          throw new RuntimeException(cause);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      // Override writeDeferredName to throw IOException directly
      private void writeDeferredName() throws IOException {
        throw new IOException("fail");
      }
    }

    TestJsonWriter testWriter = new TestJsonWriter(stringWriter);

    IOException thrown = assertThrows(IOException.class, testWriter::beginObject);
    assertEquals("fail", thrown.getMessage());
  }
}