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

public class JsonWriter_390_1Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testValue_true() throws IOException {
    // Cannot mock private methods, so just call value(true) directly
    JsonWriter returned = jsonWriter.value(true);

    assertEquals("true", stringWriter.toString());
    assertSame(jsonWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void testValue_false() throws IOException {
    // Cannot mock private methods, so just call value(false) directly
    JsonWriter returned = jsonWriter.value(false);

    assertEquals("false", stringWriter.toString());
    assertSame(jsonWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void testWriteDeferredName_and_BeforeValue_privateMethods()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
    // Use reflection to invoke private writeDeferredName and beforeValue to cover those branches

    // Setup deferredName to non-null to test writeDeferredName path that writes the name
    Method writeDeferredName = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredName.setAccessible(true);

    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    // Initially deferredName is null, writeDeferredName should do nothing
    writeDeferredName.invoke(jsonWriter);
    assertEquals("", stringWriter.toString());

    // Set deferredName via reflection
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "name");

    // Setup stack to simulate inside object with dangling name state
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Set stack[0] to DANGLING_NAME before invoking writeDeferredName to avoid nesting problem
    stack[0] = JsonScope.DANGLING_NAME;

    // Invoke writeDeferredName should write the name and separator
    writeDeferredName.invoke(jsonWriter);
    assertEquals("\"name\":", stringWriter.toString());

    // Clear stringWriter for beforeValue test
    stringWriter.getBuffer().setLength(0);

    // Setup stack for beforeValue testing different states
    stack[0] = JsonScope.EMPTY_DOCUMENT;
    beforeValue.invoke(jsonWriter);
    assertEquals("", stringWriter.toString()); // EMPTY_DOCUMENT does not write anything

    stringWriter.getBuffer().setLength(0);
    stack[0] = JsonScope.EMPTY_ARRAY;
    beforeValue.invoke(jsonWriter);
    assertEquals("", stringWriter.toString()); // EMPTY_ARRAY does not write anything

    stringWriter.getBuffer().setLength(0);
    stack[0] = JsonScope.NONEMPTY_ARRAY;
    beforeValue.invoke(jsonWriter);
    assertEquals(",", stringWriter.toString()); // NONEMPTY_ARRAY writes ','

    stringWriter.getBuffer().setLength(0);
    stack[0] = JsonScope.NONEMPTY_DOCUMENT;
    // beforeValue should throw IllegalStateException on NONEMPTY_DOCUMENT when writing value
    Exception exception = assertThrows(InvocationTargetException.class, () -> beforeValue.invoke(jsonWriter));
    assertTrue(exception.getCause() instanceof IllegalStateException);

    stringWriter.getBuffer().setLength(0);
    stack[0] = JsonScope.DANGLING_NAME;
    beforeValue.invoke(jsonWriter);
    assertEquals("", stringWriter.toString()); // DANGLING_NAME does not write anything

    stringWriter.getBuffer().setLength(0);
    stack[0] = JsonScope.NONEMPTY_OBJECT;
    beforeValue.invoke(jsonWriter);
    assertEquals(",", stringWriter.toString()); // NONEMPTY_OBJECT writes ','
  }
}