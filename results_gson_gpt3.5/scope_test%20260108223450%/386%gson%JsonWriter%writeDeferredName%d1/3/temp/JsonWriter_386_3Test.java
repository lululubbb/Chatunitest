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

public class JsonWriter_386_3Test {
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  private Method writeDeferredNameMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testWriteDeferredName_NullDeferredName() throws Throwable {
    // deferredName is null by default, so writeDeferredName should do nothing
    writeDeferredNameMethod.invoke(jsonWriter);
    assertEquals("", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testWriteDeferredName_WithDeferredName() throws Throwable {
    // Begin object to set correct state and avoid nesting problem
    jsonWriter.beginObject();

    // Set deferredName via reflection
    setDeferredName(jsonWriter, "testName");

    // Invoke private method
    writeDeferredNameMethod.invoke(jsonWriter);

    // Check output contains the name string properly quoted
    String output = stringWriter.toString();
    assertTrue(output.contains("\"testName\""), "Output should contain the deferred name quoted");

    // Verify deferredName is cleared
    assertNull(getDeferredName(jsonWriter));

    // Because writeDeferredName only writes the name but not the value,
    // we must write a value before ending the object to avoid nesting problem
    jsonWriter.value("value");

    // End object to clean up
    jsonWriter.endObject();
  }

  private void setDeferredName(JsonWriter writer, String value) throws Exception {
    Field field = JsonWriter.class.getDeclaredField("deferredName");
    field.setAccessible(true);
    field.set(writer, value);
  }

  private String getDeferredName(JsonWriter writer) throws Exception {
    Field field = JsonWriter.class.getDeclaredField("deferredName");
    field.setAccessible(true);
    return (String) field.get(writer);
  }
}