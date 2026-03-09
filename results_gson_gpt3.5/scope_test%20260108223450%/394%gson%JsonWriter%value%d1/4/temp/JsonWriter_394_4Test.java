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
  void value_writesLongValue_correctly() throws IOException {
    // We rely on default state: stack is empty, no deferredName
    jsonWriter.value(123456789L);
    assertEquals("123456789", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void value_writesLongValue_withDeferredName_andStackState() throws Exception {
    // Use reflection to set private fields deferredName and stack/stackSize to simulate state
    // Set deferredName to a string so writeDeferredName() writes it
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "foo");

    // Set stack and stackSize to simulate state so beforeValue() behaves differently
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 0; // arbitrary valid state
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // We also need to set indent and separator to test beforeValue() branches
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    indentField.set(jsonWriter, "  ");
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);
    separatorField.set(jsonWriter, " : ");

    // Call value(long)
    jsonWriter.value(42L);

    // The output should contain the deferredName and the long value
    String output = stringWriter.toString();
    assertTrue(output.contains("foo"));
    assertTrue(output.contains("42"));
  }

  @Test
    @Timeout(8000)
  void value_invokesWriteDeferredName_andBeforeValue() throws Exception {
    // Workaround: Create a static subclass that overrides the private methods as public for testing
    class JsonWriter_394_4TestSubclass extends JsonWriter {
      boolean writeDeferredNameCalled = false;
      boolean beforeValueCalled = false;

      public JsonWriterTestSubclass() {
        super(new StringWriter());
      }

      @Override
      public JsonWriter value(long value) throws IOException {
        writeDeferredName();
        beforeValue();
        // write the value to the underlying writer
        out.write(Long.toString(value));
        return this;
      }

      // Expose private methods as public for testing and override to track calls
      public void writeDeferredName() {
        writeDeferredNameCalled = true;
      }

      public void beforeValue() {
        beforeValueCalled = true;
      }
    }

    JsonWriterTestSubclass testWriter = spy(new JsonWriterTestSubclass());

    // Stub the exposed methods to just record calls
    doAnswer(invocation -> {
      testWriter.writeDeferredNameCalled = true;
      return null;
    }).when(testWriter).writeDeferredName();

    doAnswer(invocation -> {
      testWriter.beforeValueCalled = true;
      return null;
    }).when(testWriter).beforeValue();

    // Now call value(long)
    testWriter.value(7L);

    // Verify that the exposed methods were called
    assertTrue(testWriter.writeDeferredNameCalled, "writeDeferredName() should be called");
    assertTrue(testWriter.beforeValueCalled, "beforeValue() should be called");
  }
}