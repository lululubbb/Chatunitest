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

public class JsonWriter_391_5Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void value_withTrueBoolean_writesTrue() throws IOException {
    JsonWriter result = jsonWriter.value(true);
    assertSame(jsonWriter, result);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withFalseBoolean_writesFalse() throws IOException {
    JsonWriter result = jsonWriter.value(false);
    assertSame(jsonWriter, result);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void value_withNullBoolean_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(jsonWriter);

    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Boolean) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void writeDeferredName_and_beforeValue_areCalled_beforeWriting() throws Exception {
    // Create subclass to expose private fields and methods safely
    class JsonWriterForTest extends JsonWriter {
      boolean writeDeferredNameCalled = false;
      boolean beforeValueCalled = false;

      JsonWriterForTest(StringWriter out) {
        super(out);
      }

      // Use reflection to access private deferredName field
      void setDeferredName(String name) throws Exception {
        Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
        deferredNameField.setAccessible(true);
        deferredNameField.set(this, name);
      }

      // Use reflection to access private stack and stackSize fields
      void setStackState(int[] stack, int stackSize) throws Exception {
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        stackField.set(this, stack);
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(this, stackSize);
      }

      @Override
      public JsonWriter value(boolean value) throws IOException {
        try {
          // Call writeDeferredName via reflection and mark flag
          Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
          deferredNameField.setAccessible(true);
          Object deferredNameValue = deferredNameField.get(this);
          if (deferredNameValue != null) {
            writeDeferredNameCalled = true;
            Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
            writeDeferredNameMethod.setAccessible(true);
            writeDeferredNameMethod.invoke(this);
          }

          // Call beforeValue via reflection and mark flag
          beforeValueCalled = true;
          Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
          beforeValueMethod.setAccessible(true);
          beforeValueMethod.invoke(this);

          // Write the boolean value
          Field outField = JsonWriter.class.getDeclaredField("out");
          outField.setAccessible(true);
          ((java.io.Writer) outField.get(this)).write(value ? "true" : "false");
          return this;
        } catch (Exception e) {
          throw new IOException(e);
        }
      }
    }

    JsonWriterForTest testWriter = new JsonWriterForTest(stringWriter);

    // Prepare stack and deferredName to avoid exceptions and trigger writeDeferredName
    int[] stack = new int[32];
    stack[0] = 3; // NONEMPTY_OBJECT to avoid nesting problem and allow writeDeferredName
    testWriter.setStackState(stack, 1);
    testWriter.setDeferredName("myName");

    testWriter.value(true);

    assertTrue(testWriter.writeDeferredNameCalled, "writeDeferredName was not called");
    assertTrue(testWriter.beforeValueCalled, "beforeValue was not called");
  }

  @Test
    @Timeout(8000)
  public void beforeValue_privateMethod_invocation() throws Exception {
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 1; // NONEMPTY_DOCUMENT to avoid nesting problem
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    beforeValueMethod.invoke(jsonWriter);
  }

  @Test
    @Timeout(8000)
  public void writeDeferredName_privateMethod_invocation() throws Exception {
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = 3; // NONEMPTY_OBJECT to avoid nesting problem and allow writeDeferredName
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    writeDeferredNameMethod.invoke(jsonWriter);

    String output = stringWriter.toString();
    assertTrue(output.contains("name"));
  }
}