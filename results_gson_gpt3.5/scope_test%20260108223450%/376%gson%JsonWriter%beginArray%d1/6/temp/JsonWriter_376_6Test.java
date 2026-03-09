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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_376_6Test {
  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldOpenEmptyArrayAndWriteDeferredName() throws IOException {
    // Arrange
    // Instead of setting deferredName directly (which causes nesting problem),
    // use beginObject() and name() to establish proper state before beginArray.
    jsonWriter.beginObject();
    jsonWriter.name("name");

    // Act
    JsonWriter returned = jsonWriter.beginArray();

    // Assert
    // deferredName should now be null after beginArray
    String deferredNameAfter = (String) getField(jsonWriter, "deferredName");
    assertNull(deferredNameAfter);

    assertSame(jsonWriter, returned);

    // The internal stack should have EMPTY_ARRAY on top (4 according to JsonScope)
    int[] stack = (int[]) getField(jsonWriter, "stack");
    int stackSize = (int) getField(jsonWriter, "stackSize");
    int top = stack[stackSize - 1];
    assertEquals(JsonScope.EMPTY_ARRAY, top);

    // The output should have {"name":[ appended
    String output = stringWriter.toString();
    assertEquals("{\"name\":[", output);
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldWorkWithNullDeferredName() throws IOException {
    // Arrange
    setField(jsonWriter, "deferredName", null);

    // Act
    JsonWriter returned = jsonWriter.beginArray();

    // Assert
    assertSame(jsonWriter, returned);

    int[] stack = (int[]) getField(jsonWriter, "stack");
    int stackSize = (int) getField(jsonWriter, "stackSize");
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);

    String output = stringWriter.toString();
    assertEquals("[", output);
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldThrowIOException_whenOpenThrows() throws IOException {
    // We create a JsonWriter with a Writer that throws IOException on write
    Writer throwingWriter = new Writer() {
      @Override
      public void write(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("forced");
      }
      @Override public void flush() throws IOException {}
      @Override public void close() throws IOException {}
    };
    JsonWriter writer = new JsonWriter(throwingWriter);

    // Act & Assert
    IOException ex = assertThrows(IOException.class, writer::beginArray);
    assertEquals("forced", ex.getMessage());
  }

  // Helper to set private fields via reflection
  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to get private fields via reflection
  private static Object getField(Object target, String fieldName) {
    try {
      Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to find field in class hierarchy
  private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }
}