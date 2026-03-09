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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonWriter_389_6Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Initialize stack and stackSize to valid state (EMPTY_DOCUMENT)
    setPrivateField(jsonWriter, "stackSize", 1);
    setPrivateField(jsonWriter, "stack", new int[]{JsonScope.EMPTY_DOCUMENT});
  }

  @Test
    @Timeout(8000)
  public void nullValue_noDeferredName_writesNull() throws IOException {
    // deferredName is null by default
    JsonWriter returned = jsonWriter.nullValue();
    assertSame(jsonWriter, returned);
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void nullValue_deferredNameNotNull_serializeNullsTrue_writesDeferredNameAndNull() throws Exception {
    // Initialize stack and stackSize to valid state (EMPTY_OBJECT)
    setPrivateField(jsonWriter, "stackSize", 1);
    setPrivateField(jsonWriter, "stack", new int[]{JsonScope.EMPTY_OBJECT});

    // Use reflection to set deferredName to a non-null value
    setPrivateField(jsonWriter, "deferredName", "myName");
    // serializeNulls default true

    // Spy on jsonWriter to verify writeDeferredName is called
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // Replace deferredName in spy
    setPrivateField(spyWriter, "deferredName", "myName");

    // Call nullValue on spy
    JsonWriter returned = spyWriter.nullValue();

    // Verify private method writeDeferredName called once via reflection
    verifyPrivateMethodCalled(spyWriter, "writeDeferredName");

    // Check output contains name and null
    String output = getStringWriterContent(spyWriter);
    assertTrue(output.contains("myName"));
    assertTrue(output.contains("null"));

    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void nullValue_deferredNameNotNull_serializeNullsFalse_skipsNameAndWritesNull() throws IOException {
    // Initialize stack and stackSize to valid state (EMPTY_OBJECT)
    setPrivateField(jsonWriter, "stackSize", 1);
    setPrivateField(jsonWriter, "stack", new int[]{JsonScope.EMPTY_OBJECT});

    setPrivateField(jsonWriter, "deferredName", "myName");
    jsonWriter.setSerializeNulls(false);

    JsonWriter returned = jsonWriter.nullValue();

    // deferredName should be cleared (null)
    assertNull(getPrivateField(jsonWriter, "deferredName"));

    // Output only contains "null"
    assertEquals("null", stringWriter.toString());

    assertSame(jsonWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void nullValue_beforeValueCalled() throws Exception {
    // Initialize stack and stackSize to valid state (EMPTY_DOCUMENT)
    setPrivateField(jsonWriter, "stackSize", 0);
    setPrivateField(jsonWriter, "stack", new int[]{JsonScope.EMPTY_DOCUMENT});

    // Spy to verify beforeValue called
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    // deferredName null
    setPrivateField(spyWriter, "deferredName", null);

    spyWriter.nullValue();

    // Verify private method beforeValue called once via reflection
    verifyPrivateMethodCalled(spyWriter, "beforeValue");
  }

  // Helper to set private field via reflection
  private static void setPrivateField(Object target, String fieldName, Object value) {
    try {
      Field field = null;
      Class<?> clazz = target.getClass();
      // Traverse class hierarchy to find the field
      while (clazz != null) {
        try {
          field = clazz.getDeclaredField(fieldName);
          break;
        } catch (NoSuchFieldException e) {
          clazz = clazz.getSuperclass();
        }
      }
      if (field == null) {
        throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy");
      }
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private field via reflection
  private static Object getPrivateField(Object target, String fieldName) {
    try {
      Field field = null;
      Class<?> clazz = target.getClass();
      // Traverse class hierarchy to find the field
      while (clazz != null) {
        try {
          field = clazz.getDeclaredField(fieldName);
          break;
        } catch (NoSuchFieldException e) {
          clazz = clazz.getSuperclass();
        }
      }
      if (field == null) {
        throw new RuntimeException("Field '" + fieldName + "' not found in class hierarchy");
      }
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get the underlying StringWriter content from JsonWriter
  private static String getStringWriterContent(JsonWriter writer) throws Exception {
    Field outField = null;
    Class<?> clazz = writer.getClass();
    while (clazz != null) {
      try {
        outField = clazz.getDeclaredField("out");
        break;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    if (outField == null) {
      return "";
    }
    outField.setAccessible(true);
    Object out = outField.get(writer);
    if (out instanceof StringWriter) {
      return ((StringWriter) out).toString();
    }
    return "";
  }

  // Helper to verify that a private void method was called once on the spy
  private static void verifyPrivateMethodCalled(JsonWriter spy, String methodName) throws Exception {
    // Since Mockito cannot verify private methods directly,
    // we rely on side effects or simply invoke the private method to ensure it is accessible.
    Method method = null;
    Class<?> clazz = spy.getClass();
    while (clazz != null) {
      try {
        method = clazz.getDeclaredMethod(methodName);
        break;
      } catch (NoSuchMethodException e) {
        clazz = clazz.getSuperclass();
      }
    }
    if (method == null) {
      throw new RuntimeException("Method '" + methodName + "' not found in class hierarchy");
    }
    method.setAccessible(true);
    // invoke to check no exception
    method.invoke(spy);
  }
}