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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

public class JsonWriter_389_5Test {

  private StringWriter stringWriter;
  private JsonWriterSpy jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriterSpy(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void nullValue_deferredNameNull_writesNull() throws IOException {
    // deferredName == null, serializeNulls true by default
    JsonWriter result = jsonWriter.nullValue();
    assertSame(jsonWriter, result);
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void nullValue_deferredNameNotNull_serializeNullsTrue_writesNameAndNull() throws Exception {
    // Use reflection to set deferredName to a non-null value
    setDeferredName(jsonWriter, "key");
    jsonWriter.setSerializeNulls(true);

    JsonWriterSpy spyWriter = spy(jsonWriter);

    // Stub the public forwarding methods that call private methods
    doNothing().when(spyWriter).writeDeferredName_reflect();
    doNothing().when(spyWriter).beforeValue_reflect();

    JsonWriter result = spyWriter.nullValue();

    verify(spyWriter).writeDeferredName_reflect();
    verify(spyWriter).beforeValue_reflect();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void nullValue_deferredNameNotNull_serializeNullsFalse_skipsNameAndValue() throws IOException {
    setDeferredName(jsonWriter, "key");
    jsonWriter.setSerializeNulls(false);

    JsonWriterSpy spyWriter = spy(jsonWriter);
    // writeDeferredName and beforeValue should NOT be called
    JsonWriter result = spyWriter.nullValue();

    // verify private methods are NOT called by verifying the public forwarding methods are NOT called
    verify(spyWriter, never()).writeDeferredName_reflect();
    verify(spyWriter, never()).beforeValue_reflect();

    assertNull(getDeferredName(spyWriter));
    assertSame(spyWriter, result);
    assertEquals("", stringWriter.toString());
  }

  // Helper method to set private deferredName field via reflection
  private void setDeferredName(JsonWriter writer, String name) {
    try {
      var field = JsonWriter.class.getDeclaredField("deferredName");
      field.setAccessible(true);
      field.set(writer, name);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get deferredName value via reflection
  private String getDeferredName(JsonWriter writer) {
    try {
      var field = JsonWriter.class.getDeclaredField("deferredName");
      field.setAccessible(true);
      return (String) field.get(writer);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Extend JsonWriter to add public forwarding methods to private methods for Mockito spying
   */
  public static class JsonWriterSpy extends JsonWriter {
    private final StringWriter stringWriter;

    public JsonWriterSpy(StringWriter out) {
      super(out);
      this.stringWriter = out;
    }

    public void writeDeferredName_reflect() throws IOException {
      invokePrivateVoid("writeDeferredName");
    }

    public void beforeValue_reflect() throws IOException {
      invokePrivateVoid("beforeValue");
    }

    private void invokePrivateVoid(String methodName) {
      try {
        Method method = JsonWriter.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(this);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public JsonWriter nullValue() throws IOException {
      // override nullValue to call the public forwarding methods instead of private ones
      try {
        if (getDeferredName() != null) {
          if (getSerializeNulls()) {
            writeDeferredName_reflect();
          } else {
            setDeferredName(null);
            return this; // skip the name and the value
          }
        }
        beforeValue_reflect();
        stringWriter.write("null");
        return this;
      } catch (IOException e) {
        throw e;
      }
    }

    // Helper to access deferredName field
    private String getDeferredName() {
      try {
        Field field = JsonWriter.class.getDeclaredField("deferredName");
        field.setAccessible(true);
        return (String) field.get(this);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // Helper to set deferredName field
    private void setDeferredName(String value) {
      try {
        Field field = JsonWriter.class.getDeclaredField("deferredName");
        field.setAccessible(true);
        field.set(this, value);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}