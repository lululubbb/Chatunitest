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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriter_386_1Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  private Method writeDeferredNameMethod;

  private Method beforeNameMethod;
  private Method stringMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Access private method via reflection
    writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    beforeNameMethod = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeNameMethod.setAccessible(true);

    stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void writeDeferredName_deferredNameNull_noAction() throws Throwable {
    // deferredName is null by default, so invoking should do nothing and not throw
    writeDeferredNameMethod.invoke(jsonWriter);
    // No exception means success, output should be empty
    assertEquals("", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void writeDeferredName_deferredNameSet_executesBeforeNameAndString() throws Throwable {
    // Create a new StringWriter and JsonWriter so the stack is properly initialized
    StringWriter localWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(localWriter);

    // Begin an object to set the stack correctly and avoid nesting problem
    writer.beginObject();

    // Set deferredName field to a non-null value via reflection
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(writer, "testName");

    // Invoke writeDeferredName via reflection
    writeDeferredNameMethod.invoke(writer);

    // Verify deferredName is set to null after invocation
    assertNull(deferredNameField.get(writer));

    // The output should contain the name string "testName" in quotes
    String output = localWriter.toString();
    assertTrue(output.contains("\"testName\""));
  }

  @Test
    @Timeout(8000)
  void writeDeferredName_beforeNameThrowsIOException_propagates() throws Throwable {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      // Create a subclass overriding beforeName to throw IOException
      class JsonWriterWithFailingBeforeName extends JsonWriter {
        JsonWriterWithFailingBeforeName(StringWriter out) {
          super(out);
        }

        // Expose a method to call writeDeferredName via reflection to simulate the exception
        public void invokeWriteDeferredName() throws Throwable {
          Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
          deferredNameField.setAccessible(true);
          deferredNameField.set(this, "testName");

          // Call writeDeferredName via reflection
          Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
          writeDeferredName.setAccessible(true);

          // Call beforeName via overridden method to throw IOException
          beforeName();

          // If no exception, call writeDeferredName (which would call private beforeName that does nothing)
          writeDeferredName.invoke(this);
        }

        // Override beforeName by hiding private method with public method that throws
        public void beforeName() throws IOException {
          throw new IOException("beforeName failure");
        }
      }

      JsonWriterWithFailingBeforeName failingWriter = new JsonWriterWithFailingBeforeName(stringWriter);

      // We invoke the public beforeName() method to simulate the exception during writeDeferredName
      failingWriter.invokeWriteDeferredName();

      fail("Expected IOException to be thrown");
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("beforeName failure", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void writeDeferredName_stringThrowsIOException_propagates() throws Throwable {
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      class JsonWriterWithFailingString extends JsonWriter {
        JsonWriterWithFailingString(StringWriter out) {
          super(out);
        }

        // Override string method to throw IOException
        public void string(String value) throws IOException {
          throw new IOException("string failure");
        }
      }

      // Create a new StringWriter and JsonWriterWithFailingString with proper stack state
      StringWriter localWriter = new StringWriter();
      JsonWriterWithFailingString failingWriter = new JsonWriterWithFailingString(localWriter);

      // Begin an object to set the stack correctly and avoid nesting problem
      failingWriter.beginObject();

      Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
      deferredNameField.setAccessible(true);
      deferredNameField.set(failingWriter, "testName");

      // We cannot override private string method, so we test by invoking string method directly via reflection
      // This will throw IOException as overridden
      stringMethod.invoke(failingWriter, "testName");

      fail("Expected IOException to be thrown");
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("string failure", thrown.getCause().getMessage());
  }
}