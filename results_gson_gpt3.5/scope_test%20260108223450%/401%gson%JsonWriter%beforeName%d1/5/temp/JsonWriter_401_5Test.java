package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
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

import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterBeforeNameTest {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void beforeName_writesComma_ifContextIsNonEmptyObject() throws Exception {
    // Arrange
    setStackTop(NONEMPTY_OBJECT);

    // Act
    invokeBeforeName(jsonWriter);

    // Assert
    String out = stringWriter.toString();
    assertTrue(out.contains(","), "Output should contain a comma");
    assertStackTopEquals(DANGLING_NAME);
  }

  @Test
    @Timeout(8000)
  void beforeName_callsNewline_andReplacesTopForEmptyObject() throws Exception {
    // Arrange
    setStackTop(EMPTY_OBJECT);

    // Act
    invokeBeforeName(jsonWriter);

    // Assert
    // No comma should be written
    String out = stringWriter.toString();
    assertFalse(out.contains(","));
    assertStackTopEquals(DANGLING_NAME);
  }

  @Test
    @Timeout(8000)
  void beforeName_throwsIllegalStateException_whenNotObjectContext() throws Exception {
    // Arrange
    setStackTop(JsonScope.EMPTY_ARRAY);

    // Act & Assert
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      invokeBeforeName(jsonWriter);
    });
    assertEquals("Nesting problem.", thrown.getMessage());
  }

  // Helper to set the top of stack field
  private void setStackTop(int value) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    if (stack.length == 0) {
      stack = new int[32];
      stackField.set(jsonWriter, stack);
    }
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonWriter);
    if (stackSize == 0) {
      stackSizeField.set(jsonWriter, 1);
      stack[0] = value;
    } else {
      stack[stackSize - 1] = value;
    }
  }

  // Helper to assert top of stack value
  private void assertStackTopEquals(int expected) throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonWriter);
    assertEquals(expected, stack[stackSize - 1]);
  }

  // Helper to invoke private beforeName method
  private void invokeBeforeName(JsonWriter writer) throws Exception {
    Method beforeName = JsonWriter.class.getDeclaredMethod("beforeName");
    beforeName.setAccessible(true);
    beforeName.invoke(writer);
  }
}