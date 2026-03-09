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

class JsonWriter_384_4Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testReplaceTopReplacesTopOfStack() throws Exception {
    // Use reflection to set private fields stack and stackSize
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);

    // Initialize stack with size 3 and stackSize = 3
    int[] stackArray = new int[] {1, 2, 3};
    stackField.set(jsonWriter, stackArray);
    stackSizeField.setInt(jsonWriter, 3);

    // Get the private method replaceTop(int)
    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);

    // Call replaceTop with new top value 42
    replaceTopMethod.invoke(jsonWriter, 42);

    // Verify the last element of stack is replaced with 42
    int[] modifiedStack = (int[]) stackField.get(jsonWriter);
    assertEquals(42, modifiedStack[modifiedStack.length - 1]);
    // Verify other elements remain unchanged
    assertEquals(1, modifiedStack[0]);
    assertEquals(2, modifiedStack[1]);
  }

  @Test
    @Timeout(8000)
  void testReplaceTopWithStackSizeOne() throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);

    int[] stackArray = new int[] {5};
    stackField.set(jsonWriter, stackArray);
    stackSizeField.setInt(jsonWriter, 1);

    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);

    replaceTopMethod.invoke(jsonWriter, 99);

    int[] modifiedStack = (int[]) stackField.get(jsonWriter);
    assertEquals(99, modifiedStack[0]);
  }

  @Test
    @Timeout(8000)
  void testReplaceTopDoesNotChangeStackSize() throws Exception {
    Field stackField = JsonWriter.class.getDeclaredField("stack");
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackField.setAccessible(true);
    stackSizeField.setAccessible(true);

    int[] stackArray = new int[] {7, 8, 9, 10};
    stackField.set(jsonWriter, stackArray);
    stackSizeField.setInt(jsonWriter, 4);

    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);

    replaceTopMethod.invoke(jsonWriter, 55);

    int[] modifiedStack = (int[]) stackField.get(jsonWriter);
    int stackSize = stackSizeField.getInt(jsonWriter);

    assertEquals(4, stackSize);
    assertEquals(55, modifiedStack[3]);
  }
}