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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

public class JsonWriter_394_3Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() throws Exception {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Initialize stack and stackSize properly to avoid nesting problems
    setPrivateField(jsonWriter, "stackSize", 1);
    int[] stack = (int[]) getPrivateField(jsonWriter, "stack");
    stack[0] = 1; // EMPTY_DOCUMENT
  }

  @Test
    @Timeout(8000)
  public void testValue_long_basic() throws IOException {
    jsonWriter.value(123L);
    jsonWriter.flush();
    assertEquals("123", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_withDeferredNameAndStack() throws Exception {
    // Set stackSize to 2 to simulate inside an object context
    setPrivateField(jsonWriter, "stackSize", 2);

    // Prepare stack with EMPTY_OBJECT (1) and DANGLING_NAME (5) at top to allow deferredName writing
    int[] stack = (int[]) getPrivateField(jsonWriter, "stack");
    stack[0] = 1; // EMPTY_OBJECT
    stack[1] = 5; // DANGLING_NAME

    // Set deferredName to a name
    setPrivateField(jsonWriter, "deferredName", "myName");

    // Invoke value(long)
    jsonWriter.value(456L);
    jsonWriter.flush();

    // The output should be: "myName":456 because writeDeferredName writes the name with quotes and colon
    // beforeValue changes EMPTY_DOCUMENT(1) to NONEMPTY_DOCUMENT(2)
    String output = stringWriter.toString();
    assertTrue(output.contains("\"myName\""));
    assertTrue(output.endsWith("456"));
  }

  @Test
    @Timeout(8000)
  public void testBeforeValue_privateMethod_behavior() throws Exception {
    // Access private beforeValue method to test its branches

    Method beforeValue = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValue.setAccessible(true);

    int[] stack = (int[]) getPrivateField(jsonWriter, "stack");

    // Setup stack with EMPTY_ARRAY (3)
    stack[0] = 3; // EMPTY_ARRAY
    setPrivateField(jsonWriter, "stackSize", 1);

    // Call beforeValue, it should replace top with NONEMPTY_ARRAY (4)
    beforeValue.invoke(jsonWriter);
    int top = ((int[]) getPrivateField(jsonWriter, "stack"))[0];
    assertEquals(4, top);

    // Setup stack with NONEMPTY_ARRAY (4)
    stack[0] = 4;
    beforeValue.invoke(jsonWriter);
    // No exception means it handled the comma output (which writes ',') correctly

    // Setup stack with EMPTY_DOCUMENT (1)
    stack[0] = 1;
    setPrivateField(jsonWriter, "stackSize", 1);
    beforeValue.invoke(jsonWriter);
    top = ((int[]) getPrivateField(jsonWriter, "stack"))[0];
    assertEquals(2, top); // NONEMPTY_DOCUMENT

    // Setup stack with NONEMPTY_DOCUMENT (2)
    stack[0] = 2;
    setPrivateField(jsonWriter, "lenient", true);
    beforeValue.invoke(jsonWriter); // Should not throw in lenient mode

    // Setup stack with DANGLING_NAME (5) to test replaceTop to NONEMPTY_OBJECT(6)
    stack[0] = 5;
    beforeValue.invoke(jsonWriter);
    top = ((int[]) getPrivateField(jsonWriter, "stack"))[0];
    assertEquals(6, top);

    // Setup stack with NONEMPTY_OBJECT (6)
    stack[0] = 6;
    beforeValue.invoke(jsonWriter); // Should write ','

    // Reset stringWriter to avoid issues with multiple writes
    stringWriter.getBuffer().setLength(0);

    // Setup stack with invalid state (e.g., 0) to test exception
    stack[0] = 0;
    setPrivateField(jsonWriter, "lenient", false);
    Exception ex = assertThrows(Exception.class, () -> beforeValue.invoke(jsonWriter));
    assertTrue(ex.getCause() instanceof IllegalStateException);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }

  private void setPrivateField(Object instance, String fieldName, Object value) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}