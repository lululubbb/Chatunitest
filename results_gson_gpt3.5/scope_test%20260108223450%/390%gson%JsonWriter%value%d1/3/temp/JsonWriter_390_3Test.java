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

public class JsonWriter_390_3Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValueTrueWritesTrue() throws Exception {
    // No spying or stubbing private methods needed; just test value(true) directly.
    JsonWriter returned = jsonWriter.value(true);
    assertSame(jsonWriter, returned);
    assertEquals("true", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValueFalseWritesFalse() throws IOException {
    JsonWriter writer = new JsonWriter(stringWriter);
    JsonWriter returned = writer.value(false);
    assertSame(writer, returned);
    assertEquals("false", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValueCallsPrivateMethodsCorrectly() throws Exception {
    JsonWriter writer = new JsonWriter(stringWriter);

    // Use reflection to push a state to stack to avoid IllegalStateException
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 0);

    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(writer);

    // Set stackSize to 1 and stack[0] to EMPTY_DOCUMENT to simulate initial state
    stackSizeField.setInt(writer, 1);
    stack[0] = EMPTY_DOCUMENT; // Use constant to match JsonWriter's EMPTY_DOCUMENT state

    // Access private method writeDeferredName
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    // Call writeDeferredName (safe in EMPTY_DOCUMENT state)
    writeDeferredName.invoke(writer);

    // Now call value(true) normally to ensure it writes "true"
    JsonWriter returned = writer.value(true);
    assertSame(writer, returned);
    assertEquals("true", stringWriter.toString());
  }

  private static final int EMPTY_DOCUMENT = 0; // replicate constant to avoid import issues
}