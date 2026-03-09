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
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_384_5Test {

  private JsonWriter jsonWriter;
  private Method replaceTopMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    jsonWriter = new JsonWriter(new StringWriter());
    replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testReplaceTop_replacesTopOfStack() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Access and set stackSize to 1
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);

    // Access and set stack array with initial value
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = 5;
    stackField.set(jsonWriter, stack);

    // Call replaceTop with new value
    replaceTopMethod.invoke(jsonWriter, 10);

    // Verify stack[stackSize - 1] is updated
    int[] updatedStack = (int[]) stackField.get(jsonWriter);
    int updatedStackSize = stackSizeField.getInt(jsonWriter);
    assertEquals(1, updatedStackSize);
    assertEquals(10, updatedStack[updatedStackSize - 1]);
  }

  @Test
    @Timeout(8000)
  public void testReplaceTop_withMultipleStackSize() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Set stackSize to 3
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 3);

    // Initialize stack with values
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = new int[32];
    stack[0] = 1;
    stack[1] = 2;
    stack[2] = 3;
    stackField.set(jsonWriter, stack);

    // Replace top with 99
    replaceTopMethod.invoke(jsonWriter, 99);

    // Verify only top element changed
    int[] updatedStack = (int[]) stackField.get(jsonWriter);
    assertEquals(1, updatedStack[0]);
    assertEquals(2, updatedStack[1]);
    assertEquals(99, updatedStack[2]);
  }

}